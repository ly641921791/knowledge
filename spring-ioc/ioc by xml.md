[TOC]

# Spring IoC By Xml



## XML配置文件

全局控制
​    default-init-method:所有Bean的初始化回调方法
​    default-destory-method:所有Bean的销毁回调方法
​    default-lazy-init:所有Bean是否延迟实例化
​	
​	default-autowire:
​	default-dependency-check：none/objects/simple/all是否依赖注入检查，默认none

Bean属性
​	scope:Bean的作用域
​		singleton:单例，默认
​		prototype:对应多个实例
​		request:在一次HTTP请求中，单例，仅限于web环境
​		session:在一个HTTPSession中，单例，仅限于web环境
​		global Session:在一个全局HTTPSession中，单例，详细使用需要查询
​	autowire:自动装配
​		no
​		byName
​		byType
​		constructor
​		autodetect：自动决定，存在默认构造器就byType
​	init-method:初始化回调方法
​    destory-method:销毁回调方法，singleton模式
​    lazy-init:singleton是否延迟实例化，默认否
​	
​	dependency-check：none/objects/simple/all是否依赖注入检查，默认none
​	parent：继承bean属性
​	abstract：true/false是否模版配置。不会创建对象
​	lookup-method
​	replaced-method

Bean关系
​	depends-on:依赖关系的Bean使用，多个Bean使用,分隔
​	
别名
​	<alias name="" alias=""/>
​	
实例化
​	构造器实例化
​		<bean id="" class=""/>
​	静态工厂实例化(参数使用constructor-arg)
​		<bean id="" class="factoryClass" factory-method=""/>
​	实例工厂实例化
​		<bean id="" factory-bean="" factory-method=""/>
​		
setter注入
​	<bean id="" class="">
​		<property name="" value=""/>			注入值
​		<property name="">						注入值
​			<value></value>
​		</property>
​		<property name="" ref=""/>				注入bean
​		<property name="">						注入内部bean
​			<bean class=""/>
​		</property>
​	</bean>
​	
构造器注入
​	<bean id="" class="">
​		<constructor-arg index="0" value=""/>
​		<constructor-arg name="" value=""/>
​	</bean>
​	
自动装配：略





构造器注入比Setter注入的优点
​	1：在构造期间创建了完整合理的对象
​	2：避免了繁琐的setter方法编写，所有依赖关系在构造函数中展现，依赖关系集中呈现，更加易读
​	3：没有setter方法，创建完成后处于相对稳定的状态，无需担心调用过程中执行setter方法产生破坏，对系统产生影响
​	4：关联关系在构造器中表达，对于使用者，依赖关系处于黑盒之中，屏蔽了不必要的信息
​	5：可以决定依赖关系的注入顺序
