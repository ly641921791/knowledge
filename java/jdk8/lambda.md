Lambda表达式
-

### 介绍

Jdk1.8新增，用于简化代码的API。下面是使用案例

```
// 创建线程
new Thread(()->System.out.println("线程执行"));

// 遍历集合
Arrays.asList("a1","a2").forEach(string->System.out.println(string));

// 删除元素
Arrays.asList("a1","a2").removeIf(string->string.endsWith("1"));
```

### 使用

支持使用Lambda的参数必须实现函数式接口，函数式接口有以下特点：

1. 添加@FunctionalInterface注解
2. 有且只有一个抽象方法

举个例子

```java
@FunctionalInterface
public interface Foo {
    void foo();
}
```

Lambda表达式语法：(param...) -> {optional...}

(param...)表示入参，当参数数量为1时，括号可以省略
{optional...}表示操作语句，当操作语句数量为1时，括号可以省略

常见写法

```
// 0参数
new Thread(()->System.out.println("线程执行"));

// 1参数
new ArrayList().forEach(e->System.out.println(e));

// 多参数
new HashMap().forEach((k,v)->{
	System.out.println(k);
	System.out.println(v);
});
```

若方法有多个重载可以接收不同的Lambda，可以在表达式通过强转指定类型，如下：

```
@FunctionalInterface
public interface Bar {
    void bar();
}

public class Demo {
	public void demo(Foo foo){};
	public void demo(Bar bar){};
	
	public static void main(String[] args){
		Demo demo = new Demo()
		demo.demo((Foo)()->{});
		demo.demo((Bar)()->{});
	}
}
```

