#### JDK1.5-1.8特性总结

#### JDK1.5

- 自动装箱与拆箱
- 枚举
- 静态导入
- 可变参数
- 内省（Introspector）
- 泛型
- For-Each循环

#### JDK1.6

- 

#### JDK1.7

#### JDK1.8

- [Lambda表达式](./lambda.md)
- [Stream](./stream.md)
- [Optional](./jdk8_optional.md)
- 接口默认方法





ABA问题

ABA问题是由CAS引起的数据问题


有迁移关系的状态 使用state，如：TCP状态
没有迁移关系的状态，使用status，如：HTTP status code




CopyOnWrite

已空间换时间

写操作加锁，写入时复制当前元素得到副本，改写副本，写完成后，将当前元素指向副本。

适合多读少写的场景