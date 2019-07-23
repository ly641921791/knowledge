Lombok
-

Lombok是一款用于简化代码的工具。通过注解的方式，在代码编译器动态加入指定功能。

下面介绍几个我常用的 lombok 注解：

	@val		类似js中var，可以代替声明任何类型的final变量
				val a = "";	->	final String a = "";
	@NotNull	放在方法参数中， 自动为方法生成null检查，抛出空指针异常
				public NonNullExample(@NonNull Person person) {}
	@SneakyThrows		放在方法上，抛出指定异常。代替方法声明上的throws Exception
				@SneakyThrows(UnsupportedEncodingException.class)
	@Cleanup	作用在局部变量上，在作用域结束时会自动调用close方法释放资源。
			@Cleanup InputStream in = new FileInputStream(args[0]);
	@Getter		作用在字段上，会自动生成字段的Getter；作用在类上，会自动生成该类所有非静态字段的Getter，还能控制Getter的访问级别
	@Setter		基本同上。
	@ToString	类注解，自动生成类的toString方法，可以做一些定制，比如不使用某个字段，不调用Getters等。
	@EqualsAndHashCode	类注解，自动生成类中所有非静态非瞬时字段的equals方法和hashCode方法。
	@NoArgsConstructor	类注解，自动生成一个无参构造函数。
	@AllArgsConstructor	类注解，生成一个初始化所有字段的构造函数。
	@RequiredArgsConstructor	类注解，为final字段和标记了@NotNull的字段生成构造函数。
	@Data 类注解，相当于同时应用了@Getter、@Setter、@ToString、@EqualsAndHashCode、@RequiredArgsConstructor。如果已经定义了一个构造方法，就不会再自动生成构造方法了。
	@Value 类注解，和@Data类似，但是用于不可变类型。生成的类和所有字段都设置为final，所有字段都为private，自动生成Getter但是没有Setter，会生成初始化所有字段的构造函数。相当于同时应用了final @ToString、 @EqualsAndHashCode、 @AllArgsConstructor 、@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)和 @Getter。

		@Synchronized ：在方法中加锁
		@SneakyThrows ：在方法中检查异常并抛出SneakyThrows异常

日志相关
	@CommonsLog 
	Creates private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(LogExample.class); 
	@JBossLog 
	Creates private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(LogExample.class); 
	@Log 
	Creates private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(LogExample.class.getName()); 
	@Log4j 
	Creates private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LogExample.class); 
	@Log4j2 
	Creates private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LogExample.class); 
	@Slf4j 
	Creates private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class); 
	@XSlf4j 
	Creates private static final org.slf4j.ext.XLogger log = org.slf4j.ext.XLoggerFactory.getXLogger(LogExample.class);

[@Accessors](accessors.md)

> 官方网站 https://www.projectlombok.org/

> GitHub https://github.com/rzwitserloot/lombok

> 实现原理 http://ipython.mythsman.com/2017/12/19/1/