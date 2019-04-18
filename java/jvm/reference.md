引用
-

JDK 1.2开始，在强引用（Strong Reference）的基础上，新增了软引用（Soft Reference）、弱引用（Weak Reference）、
虚引用（Phantom Reference）。

- 强引用

Object o = new Object();

只要存在，不会回收。

- 软引用

SoftReference\<Object> sf = new SoftReference(new Object());

有用但非必须对象，只要内存足够就不回收，在内存溢出前进行回收，若内存还不足才会抛出异常。

主要用于实现缓存，代码如下：

```java
public class Foo {
    public static void main(String[] args){
        // 创建
        Image image = new Image();
        // 使用 略
        
        // 缓存
        SoftReference cache = new SoftReference(image);
        image = null;
        
        // 再次使用
        if(cache!=null){
            image = cache.get();
        }else {
            image = new Image();
        }
    }
}
```

- 弱引用

WeakReference\<Object> wf = new WeakReference(new Object());

非必须对象，收集器工作时，无论内存是否足够，都被回收

- 虚引用

PhantomReference\<Object> pf = new PhantomReference(new Object);

每次垃圾回收都被回收