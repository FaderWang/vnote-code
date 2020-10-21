
### 什么是Spring Boot?
详细推荐阅读[什么是Spring Boot](https://mp.weixin.qq.com/s/jWLcPxTg9bH3D9_7qbYbfw)
Spring Boot是基于Spring创建的一个全新的框架，其设计目的是简化Spring应用的搭建和开发。其本质还是Spring

### Spring Boot有哪些特点？
- 独立运行
    Spring Boot内嵌了各种Servlet容器，Tomcat、Jetty、Undertow等，不需要打包成war文件部署，只需要打包成一个可执行的jar文件就能独立运行，所有的依赖都在jar包内
- 简化配置
    Spring Boot提供了很多starter依赖，自动依赖了很多其他组件，简少了maven配置
- 自动配置
    Spring Boot提供了自动配置，能够扫描类路径下的类、jar包，自动配置相应的Bean，减少了很多xml、javaConfig配置
- 无代码生成和XML配置
    Spring Boot自动配置过程中无代码生成，利用JavaConfig能做到无xml配置。
- 应用监控
    Spring Boot提供一系列端点可以监控服务及应用，做健康检测。

### Spring Boot的核心配置文件是哪几个？它们的区别是什么？
Spring Boot的核心配置文件是application和bootstrap配置文件
- application配置文件主要用于Spring Boot的自动话配置
- bootstrap配置文件有一下几个使用场景
  - 使用Spring Cloud Config配置中心时，需要在bootstrap配置文件中添加连接到配置中心的
    配置属性来加载外部配置中心的信息
  - 一些固定的不能覆盖的配置
  - 一些加密/解密的场景