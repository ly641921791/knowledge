
Spring事务管理
	支持
		JDBC
		Hibernate
		JPA
		JTA
	方式
		声明式事务：基于AOP，通过XML配置实现
		编程式事务：通过TransactionTemplate，使用回调实现
	事务属性
		传播行为：方法直接相互调用时，事务的处理方式。
		隔离级别：解决并发引起的数据问题
		回滚规则
		事务超时
		是否只读

	
spring的配置文件:
	一、引用外部属性文件;
	二、常用数据源的配置;
	三、配置事务管理器;
	四、context:component-scan
		<!-- 对包中的所有类进行扫描，以完成Bean创建和自动依赖注入的功能 -->;
	五、aop注解支持;
	六、缓存配置;
	七、<!-- Spring、MyBatis的整合，需要在 Spring 应用上下文中定义至少两样东西：一个SqlSessionFactory和至少一个数据映射器类（UserMapper->iocContext.xml）。 -->;

	
编程式事务
	使用TransactionTemplate，编程式事务管理推荐
	直接使用PlatformTransactionManager实现
	
TransactionTemplate类似Spring中的JdbcTemplate，使用回调机制将事务代码和业务代码分离

	示例代码：
	public class SimpleService implements Service{
	
		private final TransactionTemplate transactionTemplate;
		
		public SimpleService (PlatformTransactionManager tm){
			this.transactionTemplate=new TransactionTemplate(tm);
		}
		
		//需要返回值
		public Object method(){
			return transactionTemplate.execute(new TransactionCallback(){
				public Object doInTransaction(TransactionStatus status){
					updateOperation1();
					return resultOfUpdateOperation2();
				}
			});
		}
		//不需要返回值
		public Object method(){
			return transactionTemplate.execute(new TransactionCallbackWithoutResult(){
				protected void doInTransactionWithoutResult(TransactionStatus status){
					try{
						updateOperation1();
						updateOperation2();
					}catch(SomeBusinessException e){
						status.setRollbackOnly();
					}
				}
			});
		}
	}
	
	
声明式事务
	通过AOP实现，推荐使用
	1：声明事务管理组件	
		<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
			<property name="dataSource" ref=""/>
		</bean>
	2：开启事务注解扫描
		<tx:annotation-driven transaction-manager="txManager" proxy-target-class="true"/>
			JDBC、Mybatis用DataSourceTransactionManager
			Hibernate用HibernateTransactionManager
	3：@Transactional声明类或方法。方法事务优先类
		注解属性
		propagation	设置事务传播。默认PROPAGATION_REQUIRED
		isolation	设置隔离级别。默认ISOLATION_DEFAULT
		readOnly	设置只读。可读写
		rollbackFor	设置遇到哪些异常回滚。依赖事务或超时未被支持
		noRollbackFor	设置遇到那些异常不回滚。RuntimeException触发，除了Checked Exception（需要指定异常类型才能回滚）
	XML配置声明事务
		声明事务管理组件	
		<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
			<property name="dataSource" ref=""/>
		</bean>
		声明事务范围及类型
		<tx:advice id="txAdvice" transaction-manager="txManager">
			<tx:attributes>
				<tx:method name="find*" read-only="true"/>
				...
			</tx:attributes>
		</tx:advice>
		<aop:config proxy-target-class="true">
			<aop:advisor advice-ref="txAdvice" pointcut="within(xxxx..*)"/>
		</aop:config>
		

传播行为：七种。
	PROPAGATION_REQUIRED		支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。
	PROPAGATION_SUPPORTS		支持当前事务，如果当前没有事务，就以非事务方式执行。
	PROPAGATION_MANDATORY		支持当前事务，如果当前没有事务，就抛出异常。
	PROPAGATION_REQUIRED_NEW	新建事务，如果当前存在事务，把当前事务挂起。
	PROPAGATION_NOT_SUPPORTED	以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
	PROPAGATION_NEVER			以非事务方式执行，如果当前存在事务，则抛出异常。
	PROPAGATION_NESTED			如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则进行与REQUIRED类似的操作。拥有多个可以	
	
	
隔离级别：
	
	ISOLATION_DEFAULT			使用后端数据库默认的隔离级别
	ISOLATION_READ_UNCOMMITTED	最低的隔离级别，允许读取尚未提交的数据变更，可能会导致脏读、幻读或不可重复读。并发性能最高。
	ISOLATION_READ_COMMITTED	允许读取并发事务已经提交的数据，可以阻止脏读，但是幻读或不可重复读仍有可能发生
	ISOLATION_REPEATABLE_READ	阻止脏读和不可重复读，但幻读仍有可能发生
	ISOLATION_SERIALIZABLE		最高的隔离级别，阻止脏读、不可重复读以及幻读通常是通过完全锁定事务相关的数据库表来实现的

	
###### Spring Boot 事务

通过DataSourceTransactionManagerAutoConfiguration引入了TransactionManager

通过TransactionAutoConfiguration引入了EnableTransactionManagement

##### 事务失效

TODO 调用b()，a的事务失效，待解决

``` java
public class DemoService {
    
    @Transaction
    public void a(){
    }
    
    public void b(){
        ((DemoService)AopContext().currentProxy()).a();
    }
    
}
```

> 参考文档 https://docs.spring.io/spring/docs/5.1.8.RELEASE/spring-framework-reference/data-access.html#spring-data-tier