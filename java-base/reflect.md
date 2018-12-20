


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