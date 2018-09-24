[TOC]

# SpringApplication

## 摘要

　　Spring Boot 启动过程分析（二）SpringApplication源码分析。

　　源码版本 ：2.0.1.RELEASE

## 正文

　　首先，从静态方法run开始分析Spring Boot的启动都做了哪些事情。代码如下

```java
public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
	return run(new Class[]{primarySource}, args);
}

public static ConfigurableApplicationContext run(Class<?>[] primarySources, String[] args) {
	return new SpringApplication(primarySources).run(args);
}
```

　　分析上面四行代码，可知：静态方法run做了下面两件事

1. 将启动类作为参数，实例化SpringApplication对象
2. 将命令行参数作为参数，调用实例方法run，并返回ConfigurableApplicationContext对象

### 实例化SpringApplication对象

　　下面是SpringApplication的构造方法。

```java
public SpringApplication(Class<?>... primarySources) {
	this(null, primarySources);
}

public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
    // 1 this.resourceLoader 设置为null
    this.resourceLoader = resourceLoader;
    // 2 this.primarySource 初始化并添加启动类
    Assert.notNull(primarySources, "PrimarySources must not be null");
    this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
    // 3 根据classpath下是否存在指定类判断application类型(servlet、reactive、none)
    this.webApplicationType = deduceWebApplicationType();
    //*4 this.initializers 读取配置并初始化
    setInitializers((Collection) getSpringFactoriesInstances(
        ApplicationContextInitializer.class));
    // 5 this.listeners 读取配置并初始化
    setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
    // 6 根据栈信息找到main方法所在类，并赋值给 this.mainApplicationClass
    this.mainApplicationClass = deduceMainApplicationClass();
}
```

　　过程4、5会读取所有jar包下的META-INF/spring.factories文件，将其作为配置文件读取配置类。这里分析一个核心读取方法。

```java
// SpringFactoriesLoader.java
private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
    // 先读取缓存，若存在缓存则直接返回。
    MultiValueMap<String, String> result = cache.get(classLoader);
    if (result != null) {
        return result;
    }
    try {
        // 读取全部jar文件下的META-INF/spring.factories，返回URL
        Enumeration<URL> urls = (classLoader != null ? 
                                 classLoader.getResources(FACTORIES_RESOURCE_LOCATION) : 
                                 ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
        result = new LinkedMultiValueMap<>();
        while (urls.hasMoreElements()) {
            // 读取URL返回Properties对象，将每个文件中的配置解析并存入用于汇总的Map中。
            URL url = urls.nextElement();
            UrlResource resource = new UrlResource(url);
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            for (Map.Entry<?, ?> entry : properties.entrySet()) {
                List<String> factoryClassNames = Arrays.asList(
                    StringUtils.commaDelimitedListToStringArray((String) entry.getValue()));
                result.addAll((String) entry.getKey(), factoryClassNames);
            }
        }
        // 返回结果之前，将结果缓存。
        cache.put(classLoader, result);
        return result;
    }
    catch (IOException ex) {
        throw new IllegalArgumentException("Unable to load factories from location [" +
                                           FACTORIES_RESOURCE_LOCATION + "]", ex);
    }
}
```

### 实例方法run

　　下面是实例方法run的源码，大致过程已经通过注释标注，重点代码会在下面详解。

```java
public ConfigurableApplicationContext run(String... args) {
    // 容器启动的准备
    // 1 实例化并启动计时器
    // 2 声明一个ConfigurableApplicationContext类型的ApplicationContext
    // 3 创建集合，用于存放异常记录者
    // 4 检查系统属性java.awt.headless是否配置，并没有设置则设置true。该属性作用我不了解。
    //*5 声明并创建SpringApplicationRunListeners，用于监听容器各阶段。
    // 6 发布容器启动事件
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    ConfigurableApplicationContext context = null;
    Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
    configureHeadlessProperty();
    SpringApplicationRunListeners listeners = getRunListeners(args);
    listeners.starting();
    try {
        // 解析命令行参数并包装成对象
        ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
        //*准备环境
        // 1 根据应用类型创建实现类
        // 2 处理多环境下的配置文件生效问题
        // 3 发布环境准备事件
        // 4 绑定应用与环境
        ConfigurableEnvironment environment = prepareEnvironment(
            listeners,applicationArguments);
        configureIgnoreBeanInfo(environment);
        Banner printedBanner = printBanner(environment);
        // 根据应用类型决定实例化什么类型的ApplicationContext
        context = createApplicationContext();
        // 实例化异常记录者
        exceptionReporters = getSpringFactoriesInstances(
            SpringBootExceptionReporter.class,
            new Class[] { ConfigurableApplicationContext.class }, context);
        //*准备上下文
        // 1 配置环境
        // 2 后置处理上下文
        // 3*初始化操作
        // 4 发布环境准备事件
        // 5 将参数和打印注册到beanFactory
        // 6 准备IOC环境
        // 7 发布环境加载事件
        prepareContext(context, environment, listeners, applicationArguments,
                       printedBanner);
        // 1.刷新上下文
        //  1.1准备刷新
        //  1.2获取beanFactory
        //  1.3准备beanFactory
        //  1.4后置处理beanFactory
        //  1.5调用beanFactory后置处理器
        //  1.6注册bean后置处理器
        //  1.7 onRefresh 初始化特殊的bean。例如：tomcat
        //  1.8注册监听器
        refreshContext(context);
        afterRefresh(context, applicationArguments);
        stopWatch.stop();
        if (this.logStartupInfo) {
            new StartupInfoLogger(this.mainApplicationClass)
                .logStarted(getApplicationLog(), stopWatch);
        }
        listeners.started(context);
        callRunners(context, applicationArguments);
    }
    catch (Throwable ex) {
        handleRunFailure(context, ex, exceptionReporters, listeners);
        throw new IllegalStateException(ex);
    }

    try {
        listeners.running(context);
    }
    catch (Throwable ex) {
        handleRunFailure(context, ex, exceptionReporters, null);
        throw new IllegalStateException(ex);
    }
    return context;
}
```
