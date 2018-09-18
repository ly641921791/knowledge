[TOC]

# SpringApplication

## 摘要

　　Spring Boot 启动过程分析（二）SpringApplication源码分析。

　　源码版本 ：spring-boot-2.0.0.RELEASE

## 正文

　　首先，从静态方法run开始分析Spring Boot的启动都做了哪些事情。代码如下

```java
	public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
		return run(new Class[]{primarySource}, args);
	}
	
	public static ConfigurableApplicationContext run(Class<?>[] primarySources, String[] args) {
		return (new SpringApplication(primarySources)).run(args);
	}
```

　　分析上面四行代码，可知：静态方法run做了下面两件事

1. 将启动类作为参数，实例化SpringApplication对象
2. 将命令行参数作为参数，调用实例方法run，并返回ConfigurableApplicationContext对象

### 实例化SpringApplication对象

```java
	public SpringApplication(Class... primarySources) {
		this((ResourceLoader)null, primarySources);
	}

	public SpringApplication(ResourceLoader resourceLoader, Class... primarySources) {
		// 1 初始化属性
		this.sources = new LinkedHashSet();
		this.bannerMode = Mode.CONSOLE;
		this.logStartupInfo = true;
		this.addCommandLineProperties = true;
		this.headless = true;
		this.registerShutdownHook = true;
		this.additionalProfiles = new HashSet();
		// 2 保存ResourceLoader，此处保存的是null
		this.resourceLoader = resourceLoader;
		// 3 保存启动类
		Assert.notNull(primarySources, "PrimarySources must not be null");
		this.primarySources = new LinkedHashSet(Arrays.asList(primarySources));
		// 4 根据是否存在web相关的类推断并保存application类型
		this.webApplicationType = this.deduceWebApplicationType();
		// 5.1 读取classpath下和所有jar中META-INF/spring.factories文件
		// 5.2 找到全部ApplicationContextInitializer对应的类并实例化保存
		this.setInitializers(this.getSpringFactoriesInstances(ApplicationContextInitializer.class));
		// 6 实例化并保存ApplicationListener，过程同5
		this.setListeners(this.getSpringFactoriesInstances(ApplicationListener.class));
		this.mainApplicationClass = this.deduceMainApplicationClass();
}
```

```java
public SpringApplication(Class<?>... primarySources) {
	this(null, primarySources);
}
public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
    this.resourceLoader = resourceLoader;
    Assert.notNull(primarySources, "PrimarySources must not be null");
    // 将传入的App.class保存
    this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
    // 推断web应用的类型
    this.webApplicationType = deduceWebApplicationType();
    // 实例化配置文件META-INF/spring.factories中ApplicationContextInitializer.class的实现类
    setInitializers((Collection) getSpringFactoriesInstances(
        ApplicationContextInitializer.class));
    // 实例化配置文件META-INF/spring.factories中ApplicationListener的实现类
    setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
    // 推断main方法所在类并保存
    this.mainApplicationClass = deduceMainApplicationClass();
}
```


### 实例方法run

```java
public ConfigurableApplicationContext run(String... args) {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    ConfigurableApplicationContext context = null;
    Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
    // 设置系统java.awt.headless的属性
    configureHeadlessProperty();
    // 实例化配置文件中SpringApplicationRunListener的实现类，包装成listeners
    SpringApplicationRunListeners listeners = getRunListeners(args);
    listeners.starting();
    try {
        ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
        // 准备环境
        ConfigurableEnvironment environment = prepareEnvironment(listeners,
                                                                 applicationArguments);
        configureIgnoreBeanInfo(environment);
        Banner printedBanner = printBanner(environment);
        // 创建上下文
        context = createApplicationContext();
        exceptionReporters = getSpringFactoriesInstances(
            SpringBootExceptionReporter.class,
            new Class[] { ConfigurableApplicationContext.class }, context);
        // 1.为上下文配置环境
        // 2.后置处理上下文(代码内条件不满足，因此未做任何操作)
        //*3.调用所有initializer的initialize方法
        //*4.SpringApplicationRunListeners的contextPrepared方法
        // 5.将参数和打印注册到beanFactory
        // 6.准备IOC环境
        //*7.SpringApplicationRunListeners的contextLoaded方法
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
        handleRunFailure(context, listeners, exceptionReporters, ex);
        throw new IllegalStateException(ex);
    }
    listeners.running(context);
    return context;
}
```



- ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
  - 创建环境并打印springboot标示上面三行内容
- prepareContext(context, environment, listeners, applicationArguments,printedBanner);
  - 打印springboot下面的第一行
- refreshContext(context);
  - 执行((AbstractApplicationContext) applicationContext).refresh(); 
    - 创建了内置的tomcat服务器，并且启动
- afterRefresh(context, applicationArguments);
- 








java8

::用法



