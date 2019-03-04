[TOC]

# 反射

## Class类

Class用于封装一个类的类信息，包括属性、构造函数、方法等。

主要作用是运行时动态加载类实现其功能。

### Class类的获取

- 通过类获取 ： Class c = Example.class;
- 通过对象获取 ：Class c = new Example().getClass();
- 通过全类名获取 ：Class c = Class.forName("com.example.reflect.Example");

### Class类的使用

- 创建对象

Example example = Example.class.newInstance();// 需要无参构造函数

- 获取属性列表

Field[] fields = Example.class.getFields();
Field[] fields = Example.class.getDeclaredFields();

- 获取构造函数

Constructor c = Example.class.getConstructor();
Constructor c = Example.class.getDeclaredConstructor();

- 获取方法列表

Method[] methods = Example.class.getMethods();// 获取全部public方法，包括继承得到的
Method[] methods = Example.class.getDeclaredMethods(); // 获取全部当前类声明的方法
Method m = c.getDeclaredMethod("方法名"，可变参数列表（参数类型.class）);

## Field类

- 获取属性名 ：String name = field.getName();
- 获取属性类型 ：Class c = field.getType();

## Constructor类

- 获取参数列表 ：Class[] params = constructor.getParameterTypes();

## Method类

Method类用于封装方法的信息

### Method类的使用

- 获取方法名 ：String name = method.getName();
- 获取参数列表 ：Class[] params = method.getParameterTypes();
- 获取返回值类型 ：Class returnType = method.getReturnType();

- 执行 ：Object result = method.invoke(this,args[]);






关于Java类加载器内容的详解
1类的加载
当程序要使用某个类时，如果该类还未被加载到内存中，则系统会通过加载，连接，初始化三步来实现对这个类进行初始化
·加载：
       就是指将class文件读入内存，并为之创建一个Class对象，任何类被使用时系统都会建立一个Class对象
·连接：
       验证：确保被加载类的正确性
       准备：负责为类的静态成员分配内存，并设置默认初始化值
       解析：将类中的符号引用替换为直接引用
·初始化：
        局部变量保存在栈区：必须手动初始化
        new 的对象保存在堆区：虚拟机会进行默认初始化，基本数据类型初始化值为0，引用类型初始化值为null

2.类加载的时机（只加载一次）
以下时机仅表示第一次的时候
① 创建类的实例的时候
② 访问类的静态变量的时候
③ 调用类的静态方法的时候
④ 使用反射方式来强制创建某个类或接口对应的java.lang.Class对象
⑤ 初始化某个类的子类的时候
⑥ 直接使用java.exe命令来运行某个主类

3.类加载器
负责将.class文件加载到内存中，并为之生成对应的Class对象
虽然我们在开发过程中不需要关心类加载机制，但是了解这个机制我们就能更好的理解程序的运行

4.类加载器的组成
①Bootstrap ClassLoader 根类加载器
也被称为引导类加载器，负责Java核心类的加载，比如System类，在JDK中JRE的lib目录下rt.jar文件中的类。

②Extension ClassLoader 扩展类加载器
负责JRE的扩展目录中jar包的加载，在JDK中JRE的lib目录下ext目录。

③System ClassLoader 系统类加载器
负责在JVM启动时加载来自java命令的class文件，以及classpath环境变量所指定的jar包和类路径，主要是我们开发者自己写的类。


### 桥接方法

Method.isBridge()

JDK1.5引入泛型时加入的方法。原因如下：

泛型类在编译时会泛型擦除，以SuperClass为例

```java
// 泛型擦除前
public interface SuperClass<T>{
    void method(T t);
}
// 泛型擦除后
public interface SuperClass{
    void method(Object t);
}
```

编译器会对子类做处理，保证子类中有参数为Object的method方法

```java
// 编译前
public class SubClass implements SuperClass<String>{
    public void method(String t){}
}
// 编译后
public class SubClass implements SuperClass{
    public void method(Object t){
        this.method((String)t);
    }
    public void method(String t){}
}
```

这种情况下，编译器新增的方法就是桥接方法，桥接方法可以通过反射获取到，若泛型类型为Object，则不会新增桥接方法。







