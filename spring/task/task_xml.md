Spring Task（XML实现）
-

###### XML实现

1. 任务类

```java
@Component
public class TaskJob {
    public execute(){
        System.out.println(" 任务执行。时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
    }
}
```

2. 扫描组件 & 配置任务

```xml
<beans 
	xmlns="http://www.springframework.org/schema/beans"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:task="http://www.springframework.org/schema/task" 
	xsi:schemaLocation="
		http://www.springframework.org/schema/beans   http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd 
		http://www.springframework.org/schema/task    http://www.springframework.org/schema/task/spring-task-3.0.xsd">
	
	<!-- 扫描组件 -->
	<context:component-scan base-package="xxx"/>

	<!-- 配置任务 -->
	<task:scheduled-tasks>
		<task:scheduled ref="taskJob" method="execute" cron="0 * * * * ?"/>
	</task:scheduled-tasks>

</beans>
```

###### XML & Annotation 实现

1. 任务类

```java
@Component
public class TaskJob{
    @Scheduled(cron="0 * * * * ?")
    public execute(){
        System.out.println(" 任务执行。时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
    }
}
```

2. 扫描组件 & 配置任务

```xml
<beans 
	xmlns="http://www.springframework.org/schema/beans"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:task="http://www.springframework.org/schema/task" 
	xsi:schemaLocation="
		http://www.springframework.org/schema/beans   http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd 
		http://www.springframework.org/schema/task    http://www.springframework.org/schema/task/spring-task-3.0.xsd">

	<context:component-scan base-package="xxx"/>
	
	<task:annotation-driven/>

</beans>
```