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

可以传入Lambda表达式的方法，参数类型必须是函数式接口，以Runnable接口为例

```java
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}
```

1. 添加@FunctionalInterface注解
2. 有且只有一个抽象方法

Lambda表达式语法：(param...) -> {optional...}

(param...)表示入参，当参数数量为1时，括号可以省略
{optional...}表示操作语句，当操作语句数量为1时，括号可以省略

常见写法

```
new Thread(()->System.out.println("线程执行"));

new ArrayList().forEach(e->System.out.println(e));

new HashMap().forEach((k,v)->{
	System.out.println(k);
	System.out.println(v);
});
```


