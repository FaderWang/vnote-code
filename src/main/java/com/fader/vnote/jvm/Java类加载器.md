
[知乎](https://www.zhihu.com/question/46719811)
### 自定义类加载器使用场景

热部署、代码保护、加解密、包隔离技术

### ClassLoader是用来做什么的？
它负责将Class的字节码形式（正常来说是.class文件）转换成内存形式的Java对象。
字节码可以来自于磁盘文件 *.class，也可以是 jar 包里的 *.class，也可以来自远程服务器提供的字节流，
字节码的本质就是一个字节数组 []byte，它有特定的复杂的内部格式。

通俗的将就是将.class文件加载到JVM内存中，并创建一个Class对象实例。

每个Class内部都有一个classLoader属性来标识自己事由哪个ClassLoader加载的。
```java
class Class<T> {
  ...
  private final ClassLoader classLoader;
  ...
}
```

### 延迟加载
JVM 运行并不是一次性加载所需要的全部类的，它是按需加载，也就是延迟加载。程序在运行的过程中会逐渐遇到很多不认识的新类，
这时候就会调用 ClassLoader 来加载这些类。加载完成后就会将 Class 对象存在 ClassLoader 里面，下次就不需要重新加载了。

### 内置加载器
JVM 中内置了三个重要的 ClassLoader，分别是 BootstrapClassLoader、ExtensionClassLoader 和 AppClassLoader。
- BootStrapClassLoader负责加载核心类，位于这些类位于 JAVA_HOME/lib/rt.jar。
其中rt.jar中包含了sun.misc.Launcher类。其中ExtClassLoader和AppClassLoader都是它的内部类。所以这两个加载器都是由
BootStrapClassLoader加载的。

AppClassLoader 才是直接面向我们用户的加载器，它会加载 Classpath 环境变量里定义的路径中的 jar 包和目录。我们自己编写的
代码以及使用的第三方 jar 包通常都是由它来加载的。

那些位于网络上静态文件服务器提供的 jar 包和 class文件，jdk 内置了一个 URLClassLoader，用户只需要传递规范的网络路径给构造器，
就可以使用 URLClassLoader 来加载远程类库了。URLClassLoader 不但可以加载远程类库，还可以加载本地路径的类库，取决于构造器中不同的地址形式。
ExtensionClassLoader 和 AppClassLoader 都是 URLClassLoader 的子类，它们都是从本地文件系统里加载类库。

AppClassLoader 可以由 ClassLoader 类提供的静态方法 getSystemClassLoader() 得到，
它就是我们所说的「系统类加载器」，我们用户平时编写的类代码通常都是由它加载的。当我们的 main 方法执行的时候，这第一个用户类的加载器就是 AppClassLoader。

### 传递性
程序在运行过程中，遇到了一个未知的类，它会选择哪个 ClassLoader 来加载它呢？虚拟机的策略是使用调用者 Class 对象的 ClassLoader 来加载当前未知的类。
因为 ClassLoader 的传递性，所有延迟加载的类都会由初始调用 main 方法的这个 ClassLoader 全全负责，它就是 AppClassLoader。

### 双亲委派
AppClassLoader 在加载一个未知的类名时，它并不是立即去搜寻 Classpath，它会首先将这个类名称交给 ExtensionClassLoader 来加载，
如果 ExtensionClassLoader 可以加载，那么 AppClassLoader 就不用麻烦了。否则它就会搜索 Classpath 。

而 ExtensionClassLoader 在加载一个未知的类名时，它也并不是立即搜寻 ext 路径，它会首先将类名称交给 BootstrapClassLoader 来加载，
如果 BootstrapClassLoader 可以加载，那么 ExtensionClassLoader 也就不用麻烦了。否则它就会搜索 ext 路径下的 jar 包。

这三个加载器的父子关系不是通过继承实现的，而是通过parent属性，parent属性为null则代表是BootstrapClassLoader。

双亲委派模式确保了被加载类的唯一性。例如用户自定义一个java.lang.String，加载器并不会加载。

### 自定义加载器
ClassLoader 里面有三个重要的方法 loadClass()、findClass() 和 defineClass()。
loadClass() 方法是加载目标类的入口，它首先会查找当前 ClassLoader 以及它的双亲里面是否已经加载了目标类，
如果没有找到就会让双亲尝试加载，如果双亲都加载不了，就会调用 findClass() 让自定义加载器自己来加载目标类。
ClassLoader 的 findClass() 方法是需要子类来覆盖的，不同的加载器将使用不同的逻辑来获取目标类的字节码。
拿到这个字节码之后再调用 defineClass() 方法将字节码转换成 Class 对象。

一般自定义类加载器重写findClass即可，loadClass定义了双亲委派逻辑。

### Class.forName和ClassLoader.loadClass
这两个方法都可以用来加载目标类，它们之间有一个小小的区别，那就是 Class.forName() 方法可以获取原生类型的 Class，
而 ClassLoader.loadClass() 则会报错。

### 钻石依赖
项目管理上有一个著名的概念叫着「钻石依赖」，是指软件依赖导致同一个软件包的两个版本需要共存而不能冲突。

使用 ClassLoader 可以解决钻石依赖问题。不同版本的软件包使用不同的 ClassLoader 来加载，
位于不同 ClassLoader 中名称一样的类实际上是不同的类。

ClassLoader 固然可以解决依赖冲突问题，不过它也限制了不同软件包的操作界面必须使用反射或接口的方式进行动态调用。
Maven 没有这种限制，它依赖于虚拟机的默认懒惰加载策略，运行过程中如果没有显示使用定制的 ClassLoader，
那么从头到尾都是在使用 AppClassLoader，而不同版本的同名类必须使用不同的 ClassLoader 加载，所以 Maven 不能完美解决钻石依赖。
如果你想知道有没有开源的包管理工具可以解决钻石依赖的，我推荐你了解一下 sofa-ark，它是蚂蚁金服开源的轻量级类隔离框架。

### 分工与合作
这里我们重新理解一下 ClassLoader 的意义，它相当于类的命名空间，起到了类隔离的作用。位于同一个 ClassLoader 里面的类名是唯一的，
不同的 ClassLoader 可以持有同名的类。ClassLoader 是类名称的容器，是类的沙箱。

不同的 ClassLoader 之间也会有合作，它们之间的合作是通过 parent 属性和双亲委派机制来完成的。parent 具有更高的加载优先级。
除此之外，parent 还表达了一种共享关系，当多个子 ClassLoader 共享同一个 parent 时，那么这个 parent 里面包含的类可以认为
是所有子 ClassLoader 共享的。这也是为什么 BootstrapClassLoader 被所有的类加载器视为祖先加载器，JVM 核心类库自然应该被共享。

### Thread.contextClassLoader
contextClassLoader 是从父线程那里继承过来的，所谓父线程就是创建了当前线程的线程。程序启动时的 main 线程的 contextClassLoader 
就是 AppClassLoader。这意味着如果没有人工去设置，那么所有的线程的 contextClassLoader 都是 AppClassLoader。

它可以做到跨线程共享类，只要它们共享同一个 contextClassLoader。父子线程之间会自动传递 contextClassLoader，所以共享起来将是自动化的。
如果不同的线程使用不同的 contextClassLoader，那么不同的线程使用的类就可以隔离开来。
如果我们对业务进行划分，不同的业务使用不同的线程池，线程池内部共享同一个 contextClassLoader，线程池之间使用不同的 contextClassLoader，
就可以很好的起到隔离保护的作用，避免类版本冲突。
如果我们不去定制 contextClassLoader，那么所有的线程将会默认使用 AppClassLoader，所有的类都将会是共享的。

总结来说可以在线程之间共享和隔离相应的类。

#### Java SPI机制中借助该类
启动类加载器加载SPI，配置中读取到实现类，需要加载实现类，实现类不在JDK目录下，所以这时候从Thread.contextClassLoader获取
到AppClassLoader。（bootstrapClassLoader不能逆向获取到appClassLoader）。

### 使用场景
- Tomcat容器应用的隔离，每个WebApp有自己的ClassLoader,加载每个WebApp的ClassPath路径上的类，一旦遇到Tomcat自带的
Jar包就委托给CommonClassLoader加载；
- 对于公司的一些核心类库，可能会把字节码加密，这样加载类的时候就必须对字节码进行解密，可以通过findClass读取URL中的字节码，
然后加密，最后把字节数组交给defineClass()加载。
- 热加载

#### Tomcat、JVM、Spring关系
1、集群环境可能是多个jvm 
2、一个java进程就是一个jvm，main方法启动的，同一个tomcat的多个web应用都在一个jvm里 
3、jvm包含tomcat运行环境，tomcat加载了应用上下文，应用上下文加载spring运行环境 
4、一个tomcat的各个项目之间是独立的上下文环境，如果通过http访问，也相当于跨jvm，不是引用调用

应用跟线程没什么关系，线程是个计算执行的概念，而应用上下文占用的是内存。你如果打印线程名称，就能看到应用线程是动态分配的，线程1可以在应用1出现，也可以在应用2出现。其实Tomcat是JVM的main进程启动的一个Socket服务，同时会加载应用的上下文环境、初始化执行线程池 

项目之间的调用看你用什么方式，如果通过http访问，就相当于跨jvm；项目虽然都在一个虚拟机里，但属于不同的类加载器环境，除非定义SystemClassloader级别的静态变量，没想到有其他办法能引用调用，呵呵。 
 tomcat线程之间不可以相互调用，除非是自定义的多线程。