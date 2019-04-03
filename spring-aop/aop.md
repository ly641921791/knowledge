# Spring Aop


1. 切入指定方法
	public int com.example.ClassName.MethodName(int,int)
2. 切入指定类全部方法
	public int com.example.ClassName.*(int,int)
	public int com.example.ClassName.*(..)
	
	


开启

​	@EnableAspectJAutoProxy

原理

​	利用`@Import(AspectJAutoProxyRegistrar.class) `向容器中注入`AnnotationAwareAspectJAutoProxyCreator `

​	

​	`AnnotationAwareAspectJAutoProxyCreator `实现了后置处理器和自动装配Bean工厂接口





## Spring AOP

```text
五种advice
	前置通知	方法前			org.springframework.aop.MethodBeforeAdvice
	
		public class Advice implements MethodBeforeAdvice{
			@Override
			public void before(Method method,Object[] args,Object target){}
		}
		
	后置通知	方法后			org.springframework.aop.AfterReturningAdvice
	
		public class Advice implements AfterReturningAdvice{
			@Override
			public void afterReturning(Object target,Method method,Object[] args){}
		}
		
	异常抛出					org.springframework.aop.ThrowsAdvice
	
		public class Advice implements ThrowsAdvice{
			@Override
			public void afterThrowing(IllegalArgumentException e){}
		}
	
	环绕通知					org.aopalliance.intercept.MethodInterceptor
		
		//唯一可以控制方法是否执行的通知
		public class Advice implements MethodInterceptor{
			@Override
			public Object invoke(MethodInvocation methodInvocation) throws Throwable{
				return methodInvocation.proceed();
			}
		}
		
	引介通知	类中新增方法	org.springframework.aop.IntroductionInterceptor
	
		使用方法和上面四种不同:详细使用自行百度

第一种
	ProxyFactory factory = new ProxyFactory();
	factory.setTarget(Object target);
	factory.addAdvice(Advice advice);//可以添加多个Advice
	factory.getProxy();

第二种
	ProxyFactoryBean message=new ProxyFactoryBean();
	message.setTarget(Object target); 
	message.addAdvice(Advice advice); 
	message.getObject();
	
基于IOC配置

第一种
	<bean id="xxxProxy" class="org.springframework.aop.framework.ProxyFactoryBean">
		<property name="target" ref="被代理对象Bean"/>
		<property name="interceptorNames">
			<list>
				<value>adviceBean<value/>
			</list>
		</property>
	</bean>
	
第二种
	<!-- p:interceptorNames通知数组 -->
	<!-- p:target-ref被代理对象 -->
	<!-- p:proxyTargetClass被代理对象是否是一个类，是则cglib代理，否则jdk动态代理 -->
	<bean id="xxxProxy" class="org.springframework.aop.framework.ProxyFactoryBean"
		p:interceptorNames="advice" p:target-ref="hello" p:proxyTargetClass="true" />
```





##声明式事物







# 拓展原理

### BeanPostProcessor

- bean后置处理器
- bean创建对象初始化前后进行拦截

### BeanFactoryPostProcessor

- beanFactory后置处理器
- 在beanFactory标准初始化后调用,所有的bean定义已经保存加载到beanFactory,但bean未创建对象。

### BeanDefinitionRegistryPostProcessor

- bean注册中心后置处理器
- 继承了BeanFactoryPostProcessor
- 在bean定义信息将被加载，但bean实例未创建。在BeanFactoryPostProcessor之前使用

###监听器

### - ApplicationListener

### - @EventListener









BeanDefinitionRegisiry

```java
BeanDefinitionRegisiry registry;

//方法1
RootBeanDefinition bean = new RootBeanDefinition(String.class)
//方法2
AbstractBeanDefinition bean = 	BeanDefinitionBuilder.rootBeanDefinition(String.class).getBeanDefinition();

registry.registryBeanDefinition("hello",bean);
```







# 查找Java进程中占用CPU最多的线程

1. 确定进程ID，使用`jps -v`或`top`查看

2. 查看该进程哪个线程占用大量CPU，`top -H -p  [PID]`

3. 将进程中所有线程输出到文件，`jstack [PID] > jstack.txt`

4. 在进程中查找对应的线程ID，`cat jstack.txt | grep -i [TID]`。 

   TID是线程id的16进制表示







## 切点函数


切点函数
	方法限定表达式	execution
	类型限定表达式	within(包名.类型)
		within(包名..*)包下所有类
	Bean名称限定表达式	bean("bean的id或name属性")
	
	
	
	execution(<修饰符模式>?<返回类型模式><方法名模式>(<参数模式>)<异常模式>?)
	返回类型、方法名、参数必须，其他可选
	
	方法名匹配
		
		execution(public * *(..))
		匹配public方法
		
		execution(* *To(..))
		匹配To结尾的方法
			
	类名匹配
	
		execution(*com.baobaotao.Waiter.*(..))
		匹配Waiter所有方法
	 
		execution(*com.baobaotao.Waiter+.*(..))
		匹配Waiter及所有实现类方法
		
	包名匹配
	
		execution(* com.*(..))
		匹配com包下所有类方法
		
		execution(* com..*(..))
		匹配com包及子孙包下所有类方法
		
		execution(* com..*.*Dao.find*(..))
		匹配com包及子孙包下Dao结尾的类，find开始的方法
		
	参数类型匹配
	
		*表示任意参数，..表示任意参数且参数个数不限
		java.lang包下的类，可以直接使用类名，否则必须使用全限定类名
		
		
		execution(* joke(String,int)))		
		execution(* joke(String,*)))
		execution(* joke(String,..)))
		
		execution(* joke(Object+)))
		匹配参数类型是Object及子类的方法
		
		
	其他略
	args()
	@args()
	within()
	target()
	@within()
	@target()
	this()
	
	
## Spring AOP




第一步：写Advice类，如：
	//执行顺序：前置、环绕前、目标方法、返回、环绕后、后置
	//执行顺序：前置、环绕前、目标方法、异常、后置
	//参数JoinPoint jp都可以省略
	public class Advices{
		
		public void beforeMethod(JoinPoint jp) {}
		
		public void afterMethod(JoinPoint jp) {}
		
		//参数名与配置需要一致。若只有一个JoinPoint参数。配置可以不写returning节点
		public void afterReturning(JoinPoint jp, Object result) {}
		
		//参数名与配置需要一致
		public void afterThrowing(JoinPoint jp, Exception exp) {}
		
		//可以控制方法是否执行。若只有一个JoinPoint参数。配置可以不写throwing节点
		public Object aroundMethod(ProceedingJoinPoint pjd) {
			return pjd.proceed();
		}
	}
	
第二步：配置XML文件。
	
	<bean id="advices" class="Advices/>
	
	<!-- proxy-target-class：被代理类是否是一个没有实现接口的类，是则cglib代理，否则jdk动态代理。可以省略 -->
	<aop:config proxy-target-class="true">
		<aop:aspect ref="advices">
			<aop:pointcut id="cut1" expression="execution(* 目标类.*(..))"/>
			
			<aop:before method="" pointcut-ref="cut1"/>
			<aop:after method="" pointcut-ref="cut1"/>
			<aop:around method="" pointcut-ref="cut1"/>
			<aop:after-throwing method="" pointcut="execution(* 目标类.*(..))" throwing="exp"/>
			<aop:after-returning method="" pointcut="execution(* 目标类.*(..))" returning="result"/>
		</aop:aspect>
	</aop:config>
	
第三部：直接获取该类的bean即可

