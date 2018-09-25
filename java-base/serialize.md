# Java 序列化

　　实现Serializable后，可以通过ObjectInputStream和ObjectOutputStream进行对象的读写，[例子](./src/main/java/com/example/SerializeDemo.java)。

　　静态字段不支持序列化，因为是属于类的，不属于对象。

　　还可以通过添加writeObject和readObject方法自定义序列化规则，为字段加密。原理 ：在序列化过程中，虚拟机会试图调用对象类里的 writeObject 和 readObject 方法，进行用户自定义的序列化和反序列化，如果没有这样的方法，则默认调用是 ObjectOutputStream 的 defaultWriteObject 方法以及 ObjectInputStream 的 defaultReadObject 方法。用户自定义的 writeObject 和 readObject 方法可以允许用户控制序列化的过程，比如可以在序列化的过程中动态改变序列化的数值。

　　若将一个对象两次写入同一文件，第二次只会写入引用，节省空间且读取两次后，两个对象指向一个地址。这种情况，第一次和第二次间若发生字段值变化，会导致读取不到第二次的记录。




> 还有其他序列化方式
> https://blog.csdn.net/huzhigenlaohu/article/details/51674513