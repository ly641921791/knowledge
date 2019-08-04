并发工具类
-

###### ThreadLocal

以空间换时间，将线程数据隔离

以当前ThreadLocal为key，将value存储在Thread的threadLocals中，类型为ThreadLocal.ThreadLocalMap

可以用ThreadLocal让SimpleDateFormat变成线程安全的，因 为那个类创建代价高昂且每次调用都需要创建不同的实例所以不值得在局部范围使用它，如果为每个线程提供一个自己独有的变量拷贝，将大大提高效率。首先，通 过复用减少了代价高昂的对象的创建个数。其次，你在没有使用高代价的同步或者不变性的情况下获得了线程安全。

线程局部变量的另一个不错的例子是 ThreadLocalRandom类，它在多线程环境中减少了创建代价高昂的Random对象的个数。

局限性 ： 仅仅可以在当前线程共享数据，父子线程不能

###### InheritableThreadLocal 子线程共享父线程数据

使用 ThreadLocal.ThreadLocalMap inheritableThreadLocals = null; 共享数据

原理是创建子线程时，init方法中，将父线程的inheritableThreadLocals复制到子线程

局限性 ： 使用线程池时，会导致所有的线程都共享该数据，导致数据混乱

###### TransmittableThreadLocal 阿里巴巴为解决InheritableThreadLocal实现的

``` java
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>transmittable-thread-local</artifactId>
        <version>[2.10.2,)</version>
    </dependency>
```