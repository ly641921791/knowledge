Spring AOP 专题
-

AOP（Aspect Oriented Programming）面向切面编程

##### 基本概念

- Aspect ：切面
- Join point ：加入点
- Advice ：通知，在某个加入点的操作
- Pointcut ：切入点
- Introduction
- Target object
- AOP proxy
- Weaving

###### Advice

当目标方法执行时，执行的通知操作，AspectJ支持以下五种通知：

|通知方法|注解|说明|
|---|---|---|
|前置通知|@Before|目标方法执行前执行|
|后置通知|@After|目标方法执行后执行，无论正常还是异常，类似finally语句|
|返回通知|@AfterReturning|目标方法正常返回后执行|
|异常通知|@AfterThrowing|目标方法出现异常执行|
|环绕通知|@Around|环绕目标方法执行，可以控制是否执行，可以修改参数，该通知可以完全替代前面4个通知|

当方法执行未抛出异常时，通知顺序：环绕（前）->前置通知->目标方法->环绕（后）->后置通知->返回通知
当方法执行抛出异常时，通知顺序：环绕（前）->前置通知->后置通知->异常通知
当环绕通知未执行目标方法，通知顺序：环绕通知->后置通知->返回通知

###### Aspect

AspectJ中，Aspect=PointCut+Advice。

###### Pointcut

切入点，被拦截的方法。可以使用切点函数匹配目标方法。

###### Join point

通知操作执行时的状态，包括通知类型、方法名、参数等信息。可以通过将任何一个通知方法的第一个参数改为`JoinPoint`类型获取连接点，
环绕通知的连接点类型是`ProceedingJoinPoint`，继承于`JoinPoint`。

切入点的切入状态，

``` java
public interface Joinpoint {

    // 执行
	Object proceed() throws Throwable;

    // 目标对象。静态方法则返回null
	Object getThis();

    // 目标方法
	AccessibleObject getStaticPart();

}

public interface Invocation extends Joinpoint {

	// 目标方法参数
	Object[] getArguments();

}

public interface MethodInvocation extends Invocation {

	// 目标方法
	Method getMethod();

}
```

##### 使用教程

###### 开启AOP

``` java
@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
    
}
```

``` xml
<aop:aspectj-autoproxy/>
```

###### 声明切面

```java
@Aspect
@Component
public class LogAspect{
    
}
```

###### 声明切入点

```java
@Aspect
@Component
public class LogAspect{
    
    @Pointcut("切点函数")
    private void logPointcut() {}
    
}
```

###### 通知方法

## 使用方法

大致流程如下

1. 定义切面类
2. 开启AOP功能
3. 测试AOP

**定义切面类**

```java

/**
 * 1 使用@Aspect注解定义切面类，使用@Component注解注册到Spring容器
 *
 * @author ly
 */
@Aspect
@Component
public class ExampleAspect {

    /**
     * 2 定义切入点，使用切点函数指定com.example包下任意类的任意方法。
     * 每个通知都支持切点函数，但统一定义pointCut()，可以统一管理切点
     */
    @Pointcut("execution(* com.example..*(..))")
    public void pointCut() {}

    /**
     * 前置通知
     */
    @Before("pointCut()")
    public void before() {
        System.out.println("before advice");
    }

    /**
     * 后置通知
     */
    @After("pointCut()")
    public void after() {
        System.out.println("after advice");
    }

    /**
     * 返回通知。通过JoinPoint参数获得连接点，通过returning指定返回值参数名，
     */
    @AfterReturning(value = "pointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        System.out.printf("afterReturning advice。Method：%s，Args：%s，result：%s\n",
                joinPoint.getSignature().getName(),
                Arrays.asList(joinPoint.getArgs()),
                result);
    }

    /**
     * 异常通知。通过JoinPoint参数获得连接点，通过throwing指定异常参数名，
     */
    @AfterThrowing(value = "pointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        System.out.printf("afterThrowing advice。Method：%s，Args：%s，exception：%s\n",
                joinPoint.getSignature().getName(),
                Arrays.asList(joinPoint.getArgs()),
                e);
    }

    /**
     * 环绕通知。通过ProceedingJoinPoint参数除了JoinPoint的功能外，还可以控制方法是否执行。
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        System.out.println("around advice start");
        // 该方法有重载，可以修改传入参数
        Object result = point.proceed();
        System.out.println("around advice end");
        return result;
    }
}

```


**测试AOP**

创建目标类和测试类

```java
@Data
public class Target {

	// 通过参数，简单的控制是否抛出异常
    public void target(RuntimeException e) {
        if (e != null) {
            throw e;
        }
        System.out.println("target 正常执行");
    }

}
@Import(Target.class)
@SpringBootApplication
public class AopApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AopApplication.class, args);
        Target target = context.getBean(Target.class);
        target.target(null);
    }

}
```

运行结果如下

```log
around advice start
before advice
target 正常执行
around advice end
after advice
afterReturning advice。Method：target，Args：[null]，result：null
```


[开启AOP](enable.md)

[源码分析](source_code.md)

[](https://mp.weixin.qq.com/s?__biz=MzU3NzczMTAzMg==&mid=2247484781&idx=2&sn=217306a6f418e30cec6c5fbc8cd6ddb7&chksm=fd0165daca76eccca5d59401b33784babc98e6a4094cbf10342f46178ff4a2db8d430f596a16&scene=21#wechat_redirect)
[](https://www.cnblogs.com/xrq730/p/4919025.html)
[](https://www.cnblogs.com/xrq730/p/7003082.html)



> 官网文档 https://docs.spring.io/spring/docs/5.1.8.RELEASE/spring-framework-reference/core.html#aop

> AspectJ https://www.eclipse.org/aspectj/doc/released/progguide/index.html

> AspectJ https://www.eclipse.org/aspectj/doc/released/adk15notebook/index.html