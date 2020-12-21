### 准备

**ConfigurationClassPostProcessor**

`@Configutarion`注解能够实现的核心类。该类实现了`BeanFactoryPostProcessor`后置处理接口，该接口是Spring提供的一个扩展钩子类，通过回调来对BeanDefinition进行修改，从而定制bean的创建。

### 注册`ConfigurationClassPostProcessor`组件

```java
if (!registry.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {
    RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);
    def.setSource(source);
    beanDefs.add(registerPostProcessor(registry, def, CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME));
}
```

该类是Spring容器的内部组件，容器在启动时，会调用`registerAnnotationConfigProcessors`方法主动注册一个该对象的bean实例。**注意：xml扫描方式的容器和annotation方式的容器注册的时机稍微不同。**

### 入口

`refresh`容器启动方法中会调用`invokeBeanFactoryPostProcessors`回调容器中注册的内部后处理器。然后委托给`PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors`静态方法处理。伪代码：

```java
public static void invokeBeanFactoryPostProcessors(
    ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

    // 省略了前面的无关逻辑
    List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();

    // 根据类型获取容器中注册的BeanDefinitionRegistryPostProcessor
    String[] postProcessorNames =
        beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
    // 进行筛选，这里分了两步。这里对PriorityOrdered、Ordered类型的分别进行处理。
    for (String ppName : postProcessorNames) {
        if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
            currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
            processedBeans.add(ppName);
        }
    }
    // 排序
    sortPostProcessors(currentRegistryProcessors, beanFactory);
    registryProcessors.addAll(currentRegistryProcessors);
    // 回调筛选后的BeanDefinitionRegistryPostProcessor
    invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
    currentRegistryProcessors.clear();

    // 同上
    postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
    for (String ppName : postProcessorNames) {
        if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
            currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
            processedBeans.add(ppName);
        }
    }
    sortPostProcessors(currentRegistryProcessors, beanFactory);
    registryProcessors.addAll(currentRegistryProcessors);
    invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
}
```

### 解析

上面的方法最后会回调我们核心类的`postProcessBeanDefinitionRegistry`方法。开始进入解析过程。

`processConfigBeanDefinitions`方法部分代码：

```java
ConfigurationClassParser parser = new ConfigurationClassParser(
				this.metadataReaderFactory, this.problemReporter, this.environment,
				this.resourceLoader, this.componentScanBeanNameGenerator, registry);
Set<BeanDefinitionHolder> candidates = new LinkedHashSet<>(configCandidates);
Set<ConfigurationClass> alreadyParsed = new HashSet<>(configCandidates.size());
do {
    parser.parse(candidates);
    parser.validate();

    // 获取解析配置类得到的bean对象类型。
    Set<ConfigurationClass> configClasses = new LinkedHashSet<>(parser.getConfigurationClasses());
    configClasses.removeAll(alreadyParsed);

    // Read the model and create bean definitions based on its content
    if (this.reader == null) {
        this.reader = new ConfigurationClassBeanDefinitionReader(
            registry, this.sourceExtractor, this.resourceLoader, this.environment,
            this.importBeanNameGenerator, parser.getImportRegistry());
    }
    // 注册beanDefinition
    this.reader.loadBeanDefinitions(configClasses);
    alreadyParsed.addAll(configClasses);

    candidates.clear();
    if (registry.getBeanDefinitionCount() > candidateNames.length) {
        String[] newCandidateNames = registry.getBeanDefinitionNames();
        Set<String> oldCandidateNames = new HashSet<>(Arrays.asList(candidateNames));
        Set<String> alreadyParsedClasses = new HashSet<>();
        for (ConfigurationClass configurationClass : alreadyParsed) {
            alreadyParsedClasses.add(configurationClass.getMetadata().getClassName());
        }
        for (String candidateName : newCandidateNames) {
            if (!oldCandidateNames.contains(candidateName)) {
                BeanDefinition bd = registry.getBeanDefinition(candidateName);
                if (ConfigurationClassUtils.checkConfigurationClassCandidate(bd, this.metadataReaderFactory) &&
                    !alreadyParsedClasses.contains(bd.getBeanClassName())) {
                    candidates.add(new BeanDefinitionHolder(bd, candidateName));
                }
            }
        }
        candidateNames = newCandidateNames;
    }
}
while (!candidates.isEmpty());
```

创一个`ConfigurationClassParser`解析器，然后在一个while循环中，调用parse方法解析。这里为什么要循环呢，因为Configuration配置类不仅仅可以配置bean，配置类上还可以加@import注解导入其它配置类，这里加上循环就相当于递归调用。

具体解析逻辑在`doProcessConfigurationClass`方法中：

```java
protected final SourceClass doProcessConfigurationClass(
    ConfigurationClass configClass, SourceClass sourceClass, Predicate<String> filter)
    throws IOException {

    if (configClass.getMetadata().isAnnotated(Component.class.getName())) {
        // Recursively process any member (nested) classes first
        processMemberClasses(configClass, sourceClass, filter);
    }

    //处理@PropertySource注解
    for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(
        sourceClass.getMetadata(), PropertySources.class,
        org.springframework.context.annotation.PropertySource.class)) {
        if (this.environment instanceof ConfigurableEnvironment) {
            processPropertySource(propertySource);
        }
        else {
            logger.info("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() +
                        "]. Reason: Environment must implement ConfigurableEnvironment");
        }
    }

    // 处理@ComponentScan注解
    Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(
        sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);
    if (!componentScans.isEmpty() &&
        !this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationPhase.REGISTER_BEAN)) {
        for (AnnotationAttributes componentScan : componentScans) {
            // The config class is annotated with @ComponentScan -> perform the scan immediately
            Set<BeanDefinitionHolder> scannedBeanDefinitions =
                this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
            // Check the set of scanned definitions for any further config classes and parse recursively if needed
            for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
                BeanDefinition bdCand = holder.getBeanDefinition().getOriginatingBeanDefinition();
                if (bdCand == null) {
                    bdCand = holder.getBeanDefinition();
                }
                if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {
                    parse(bdCand.getBeanClassName(), holder.getBeanName());
                }
            }
        }
    }

    // 处理@Import注解
    processImports(configClass, sourceClass, getImports(sourceClass), filter, true);

    // 处理@ImportResource注解
    AnnotationAttributes importResource =
        AnnotationConfigUtils.attributesFor(sourceClass.getMetadata(), ImportResource.class);
    if (importResource != null) {
        String[] resources = importResource.getStringArray("locations");
        Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
        for (String resource : resources) {
            String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
            configClass.addImportedResource(resolvedResource, readerClass);
        }
    }

    // 处理@Bean注解的方法，即定义的bean
    Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);
    for (MethodMetadata methodMetadata : beanMethods) {
        configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
    }

    // 处理实现接口的default方法
    processInterfaces(configClass, sourceClass);

    // 处理父类，如果有父类，则返回父类，外层while循环会递归处理
    if (sourceClass.getMetadata().hasSuperClass()) {
        String superclass = sourceClass.getMetadata().getSuperClassName();
        if (superclass != null && !superclass.startsWith("java") &&
            !this.knownSuperclasses.containsKey(superclass)) {
            this.knownSuperclasses.put(superclass, configClass);
            // Superclass found, return its annotation metadata and recurse
            return sourceClass.getSuperClass();
        }
    }

    // 没有父类返回null，外层循环结束。
    return null;
}
```

该方法中分别分别处理了一下几个类型的注解：

- @Component：组件定义
- @PropertySources：处理properties资源文件
- @ComponentScans：组件扫描
- @Import：处理导入配置类或配置扫描类
- @ImportResource：处理导入的配置文件
- @Bean：处理bean实例定义，相当于xml配置文件的<Bean>标签

其中@Bean是作用在方法上的。

这里最复杂的是@Import，因为@Import可以导入其它Configuration配置类，这里着重分析下@Import处理。

**@Import介绍**

value值是一个Class<?>数组，从注释中我们可以发现，value的class类型可以有三种：

- 被@Configuration注解的Class类：直接导入一个Configuration配置类
- ImportSelecor类型：通过回调改类的selectImports方法，得到一组configuration配置类。
- ImportBeanDefinitionRegistrar类型：回调registerBeanDefinitions方法直接注册beanDefinition

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {

	/**
	 * {@link Configuration @Configuration}, {@link ImportSelector},
	 * {@link ImportBeanDefinitionRegistrar}, or regular component classes to import.
	 */
	Class<?>[] value();

}
```

回头看`processImports`方法

```java
private void processImports(ConfigurationClass configClass, SourceClass currentSourceClass,
                            Collection<SourceClass> importCandidates, Predicate<String> exclusionFilter,
                            boolean checkForCircularImports) {

    if (importCandidates.isEmpty()) {
        return;
    }

    if (checkForCircularImports && isChainedImportOnStack(configClass)) {
        this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
    }
    else {
        // 当前处理的类入栈
        this.importStack.push(configClass);
        try {
            // 遍历import的value属性
            for (SourceClass candidate : importCandidates) {
                // 当前是ImportSelector， 调用selectImports，得到一组配置类，递归处理
                if (candidate.isAssignable(ImportSelector.class)) {
                    Class<?> candidateClass = candidate.loadClass();
                    ImportSelector selector = ParserStrategyUtils.instantiateClass(candidateClass, ImportSelector.class,
                                                                                   this.environment, this.resourceLoader, this.registry);
                    Predicate<String> selectorFilter = selector.getExclusionFilter();
                    if (selectorFilter != null) {
                        exclusionFilter = exclusionFilter.or(selectorFilter);
                    }
                    if (selector instanceof DeferredImportSelector) {
                        this.deferredImportSelectorHandler.handle(configClass, (DeferredImportSelector) selector);
                    }
                    else {
                        String[] importClassNames = selector.selectImports(currentSourceClass.getMetadata());
                        Collection<SourceClass> importSourceClasses = asSourceClasses(importClassNames, exclusionFilter);
                        processImports(configClass, currentSourceClass, importSourceClasses, exclusionFilter, false);
                    }
                }
                else if (candidate.isAssignable(ImportBeanDefinitionRegistrar.class)) {
                    // 如果是ImportBeanDefinitionRegistrar类型，直接回调注册方法注册beanDefinition
                    Class<?> candidateClass = candidate.loadClass();
                    ImportBeanDefinitionRegistrar registrar =
                        ParserStrategyUtils.instantiateClass(candidateClass, ImportBeanDefinitionRegistrar.class,
                                                             this.environment, this.resourceLoader, this.registry);
                    configClass.addImportBeanDefinitionRegistrar(registrar, currentSourceClass.getMetadata());
                }
                else {
                    // 第三种情况，处理Configuration配置类，第一中importselector最终也会转换成处理Configuration配置类。区别就是一个是直接处理一个配置类，一个通过回调方法扫描出一组配置类
                    this.importStack.registerImport(
                        currentSourceClass.getMetadata(), candidate.getMetadata().getClassName());
                    // 这里processConfigurationClass方法就是一开始我们处理Configuration的入口，可以发现这里也是一个递归。
                    processConfigurationClass(candidate.asConfigClass(configClass), exclusionFilter);
                }
            }
        }
        catch (BeanDefinitionStoreException ex) {
            throw ex;
        }
        catch (Throwable ex) {
            throw new BeanDefinitionStoreException(
                "Failed to process import candidates for configuration class [" +
                configClass.getMetadata().getClassName() + "]", ex);
        }
        finally {
            this.importStack.pop();
        }
    }
}
```

总结下来，就是利用递归解析出一个个configuration配置类进行处理。

### 注册Bean

配置类最终目的都是配置bean，递归解析完configuration对象后，所有的配置类会被保存在一个set集合中，遍历该集合，调用`loadBeanDefinitionsForConfigurationClass`方法，为@Bean注解的方法，解析为BeanDefinition，注册到容器中。

处理逻辑在`loadBeanDefinitionsForBeanMethod`方法中。大致步骤就是创建一个ConfigurationClassBeanDefinition类型的BeanDefinition对象，设置`FactoryMethodName`参数，即@Bean所作用的方法名。后面实例话bean的时候通过该方法名获取`Method`反射对象，通过反射生成Bean实例。

```java
ConfigurationClassBeanDefinition beanDef = new ConfigurationClassBeanDefinition(configClass, metadata);
beanDef.setResource(configClass.getResource());
beanDef.setSource(this.sourceExtractor.extractSource(metadata, configClass.getResource()));

if (metadata.isStatic()) {
    // static @Bean method
    if (configClass.getMetadata() instanceof StandardAnnotationMetadata) {
        beanDef.setBeanClass(((StandardAnnotationMetadata) configClass.getMetadata()).getIntrospectedClass());
    }
    else {
        beanDef.setBeanClassName(configClass.getMetadata().getClassName());
    }
    beanDef.setUniqueFactoryMethodName(methodName);
}
else {
    // instance @Bean method
    beanDef.setFactoryBeanName(configClass.getBeanName());
    beanDef.setUniqueFactoryMethodName(methodName);
}
```

### 总结一下@Configuration注解解析过程

1. 注册`ConfigurationClassPostProcessor`Spring内部组件。
2. 容器启动时，回调`ConfigurationClassPostProcessor`的`postProcessBeanDefinitionRegistry`进行后置处理
3. 从一个入口配置类开始解析，并递归处理所有@Configuration注解作用的类。主要就是扫描@Bean注解作用的方法，然后解析为一个`ConfigurationClassBeanDefinition`类型的BeanDefinition对象，注册到容器中。




### `@Import注解`
@Import的javadoc文档的说明：
1. @Import就是用来向容器中导入bean的，导入的方式有三种：导入@Configuration、导入ImportSelector、导入ImportBeanDefinitionRegistrar。
2. 被Import的类是被加载到了Spring容器当中，因此无论是类本身还是类里面用@Bean注解定义的bean都可以被@Autowired注入。
3. @Import可以加在类上面，也可以作为元注解加在注解上面

### `@Conditional条件注解`
条件注解的value属性表示匹配条件，是`Condition`类型的数组。
