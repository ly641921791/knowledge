[TOC]

# Spring IoC By Annotation

## 组件注册

向容器注册组件

- 包扫描（配合@Component注解及子注解使用，一般用于导入自定义组件）
- @Bean（一般用于导入第三方jar包组件）
- @Import
- FactoryBean接口

### @Configuration

将标记的类注册到Spring容器，并声明组件类型为配置类。

**示例代码**

```java
// 声明配置类
@Configuration
public class Config {}
// 读取配置类
public class Application {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
	}
}
```

### @Bean

将标记的方法返回值注册到Spring容器。Bean类型为返回值类型，id默认方法名。

**示例代码**

```java
@Configuration
public class Config {
    @Bean
    public MyBean myBean(){
        return new MyBean();
    }
}
```

### @ComponentScan

标记在配置类上，扫描指定包的组件注册到Spring容器。

**示例代码**

```java
@Configuration
@ComponentScan(value="com.example.bean")
public class Config {}
```

### @Conditional

根据条件注册Bean。可以实现自定义条件，满足条件的Bean才会被注册到Spring容器。

**示例代码**

```java
// 匹配条件类
public class MyConditional implements Condition{
    @Override
    public boolean matches(ConditionContext context,AnnotatedTypeMetadata metadata){
        return true;
    }
}
// Bean组件指定生效条件
@Configuration
@Conditional(MyConditional.class)
public class Config {}
```

### @Import

导入一个组件，id默认全类名

**示例代码**

```java
@Configuration
@Import(MyConditional.class)
public class Config {}
```

### FactoryBean

通过id获得的是创建的对象，&id才是FactoryBean

## 组件属性

### @Scope

配置Bean组件的作用域，默认单例模式。

**示例代码**

```java
@Configuration
public class Config {
    @Bean
    @Scope
    public MyBean myBean(){
        return new MyBean();
    }
}
```

### @Lazy

配置Bean组件懒加载，单例组件默认在容器启动创建，标记该注解后，改为第一次使用创建。

**示例代码**

```java
@Configuration
public class Config {
    @Bean
    @Lazy
    public MyBean myBean(){
        return new MyBean();
    }
}
```

## 生命周期

## 自动装配