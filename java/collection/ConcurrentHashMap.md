ConcurrentHashMap
-

线程安全且高效的Map

HashMap和HashTable实现原理几乎相同，不同在于是否允许null，是否线程安全，HashTable线程安全策略代价比较大

##### 对比

###### JDK1.7

采用 Segment + HashEntry + ReentrantLock 实现

基于Segment加锁，一个Segment包含多个HashEntry
 
###### JDK1.8

采用 Node + CAS + Synchronized 实现

基于HashEntry的首节点加锁，使用synchronized加锁，粒度降低，复杂度提高

使用红黑色优化链表

##### 方法

###### get




[Java 经典面试题：为什么 ConcurrentHashMap 的读操作不需要加锁？](https://mp.weixin.qq.com/s/Id29MyFHddStZTznSKn3uQ)