依赖注入
-



控制反转（Inversion of Control，缩写为IoC），是面向对象编程中的一种设计原则，可以用来减低计算机代码之间的耦合度。其中最常见的方式叫做依
赖注入（Dependency Injection，简称DI），还有一种方式叫“依赖查找”（Dependency Lookup）。通过控制反转，对象在被创建的时候，由一个调控系
统内所有对象的外界实体将其所依赖的对象的引用传递给它。也可以说，依赖被注入到对象中。


开启组件扫描
	<context:component-scan base-package=""/>

声明扫描组件
	可以指定名字。默认名类名首字母小写。
	@Component	通用
	@Named		通用
	@Repository	持久层
	@Service	业务层
	@Controller	控制层

beans属性
	@Scope("")
	@DependsOn({"",""})
	@Lazy

方法注解
	@PostConstruct初始化方法
	@PreDestroy销毁方法

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