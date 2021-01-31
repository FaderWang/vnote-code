[参考](http://www.iocoder.cn/Spring-Boot/autoconfigure/)
Spring Boot自动配置需要解决三个问题：
- 满足什么样的条件？
- 创建哪些Bean？
- 创建Bean的属性？

分别对应：
- 条件注解
- 配置类
- 配置属性

自动配置类都是以`AutoConfiguration`作为后缀的类。我们引入相应框架的starter，jar包中都会有相应的
自动配置类。


**条件注解**
Spring Boot `Condition`包下提供了一系列条件注解，只有满足相应的条件配置类才会生效
`@ConditionalOnClass`：表示当前配置类需要在当前项目有指定类的条件下，才能生效
`@ConditionalOnWebApplication`：表示当前配置类需要在当前项目是 Web 项目的条件下，才能生效。

**配置属性**

使用 `@EnableConfigurationProperties` 注解，让 ServerProperties 配置属性类生效。
`@ConfigurationProperties` 注解，声明将 server 前缀的配置项，设置到 ServerProperties 配置属性类中。
例如ServerProperties代码如下：
```java
@ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)
public class ServerProperties
		implements EmbeddedServletContainerCustomizer, EnvironmentAware, Ordered {

	/**
	 * Server HTTP port.
	 */
	private Integer port;

	/**
	 * Context path of the application.
	 */
	private String contextPath;
	
	// ... 省略其它属性
	
}
```

**自动配置**
在 Spring Boot 的 spring-boot-autoconfigure 项目，提供了大量框架的自动配置。
在我们启动Spring Boot应用的时候，会使用`SpringFactoriesLoader`类，读取`META-INF`目录下的Spring.factories文件，获取`EnableAutoConfiguration`
对应的自动配置类列表。注意的是这里会读取所有jar包该目录下的文件。
获得每个框架定义的需要自动配置的类。
如此，原先 @Configuration 注解的配置类，就升级成类自动配置类。这样，Spring Boot 在获取到需要自动配置的配置类后，就可以自动创建相应的 Bean，完成自动配置的功能。

每个框架对应的Starter都会有在该目录文件下定义相应的自动配置类。

SpringFactoriesLoader加载机制可以看作Spring Boot的SPI机制。