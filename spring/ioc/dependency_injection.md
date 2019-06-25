依赖注入
-

控制反转（Inversion of Control，缩写为IoC），是面向对象编程中的一种设计原则，可以用来减低计算机代码之间的耦合度。其中最常见的方式叫做依
赖注入（Dependency Injection，简称DI），还有一种方式叫“依赖查找”（Dependency Lookup）。通过控制反转，对象在被创建的时候，由一个调控系
统内所有对象的外界实体将其所依赖的对象的引用传递给它。也可以说，依赖被注入到对象中。

依赖注入的前提是，将Bean注册到Bean容器中

###### Bean容器

开启组件扫描

- XML配置开启 ：`<context:component-scan base-package=""/>`
- Annotation开启 ：`@ComponentScan`

标记被扫描组件，可以指定名字。默认名类名首字母小写。

- @Component 通用
- @Named 通用
- @Repository 持久层
- @Service 业务层
- @Controller 控制层

beans属性
	@Scope("")
	@DependsOn({"",""})
	@Lazy

方法注解
	@PostConstruct初始化方法
	@PreDestroy销毁方法

###### 依赖注入

依赖关系注入
	@Resource
		用在字段或Setter方法上，首先按名字注入，然后按类型注入
		可以显示指定名称@Resource(name="")
	@AutoWired/@Qualifier(合格者)
		@AutoWired用在字段或Setter方法上，首先按名字注入，然后按类型注入。放在构造器上就是构造器注入
		可以显示指定名称例子如下	Bean3
	@Inject(注入)/@Named		使用同AutoWired

注入表达式
	@Value
		放在字段或Setter方法上，如：Value("#{}")
		
@Value用法

- 注入基本数据类型
    - @Value("abc") private String value;
    - @Value("123") private int value;
- 注入环境变量，可以设置默认值
    - @Value("${server.port}") private int value;
    - @Value("${user.dir}") private String value;
    - @Value("${server.port:8080}") private int value;  // 设置默认值
- 注入SpringEL表达式
    - @Value("#{1 > 0}") private boolean value;
    - @Value("#{${server.port} == 8080}") private boolean value;


@Resource和@Autowired的区别

- @Resource是JSR250注解，默认注入策略是先根据名称再根据类型，可以通过name或type属性指定注入策略
- @Autowired是Spring原生注解，默认注入策略是根据类型，通过与@Qualifier注解配合指定名称注入
