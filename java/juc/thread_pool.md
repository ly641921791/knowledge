线程池
-

###### 为什么使用

1. 降低资源消耗。重复利用已创建线程，可以降低线程创建和销毁造成的消耗
2. 提高响应速度。无需创建线程就可以立即执行任务
3. 提高线程的可管理性。无限创建线程，不仅消耗系统资源，还降低系统稳定性，使用线程池可以进行统一分配、调优、监控

###### 工作原理

每一个任务都被封装成`ThreadPoolExecutor.Worker`对象放入任务队列，工作线程不断从任务队列拿任务执行run方法

###### 核心属性

线程池有以下核心属性

- corePoolSize 线程池基本大小
- maximumPoolSize 最大线程数
- keepAliveTime 空闲线程的存活时间，当超过基本大小后，回收
- workQueue 待执行的工作队列
- threadFactory 线程工厂
- handler 拒绝策略

工作队列

- ArrayBlockingQueue 基于数组的FIFO的队列
- LinkedBlockingQueue 基于链表的FIFO的队列，吞吐高于ArrayBlockingQueue。Executors.newFixedThreadPool使用
- SynchronousBlockingQueue 不存储元素阻塞队列，吞吐高于LinkedBlockingQueue。Executors.newCachedThreadPool使用
- PriorityBlockingQueue 优先级队列

拒绝策略

- AbortPolicy 直接抛出异常。默认策略，抛出RejectedExecutionException异常
- CallerRunsPolicy 使用调养者所在线程执行任务
- DiscardOldestPolicy 丢弃最近的任务，执行当前任务
- DiscardPolicy 丢弃任务

###### JDK提供的线程池

- FixedThreadPool 固定线程池的线程池
- SingleThreadPool 单线程的线程池
- CachedThreadPool 可缓存的线程池
- ScheduledThreadPool 定时任务线程池

###### 扩展

回调接口：提供了任务之前、任务之后、任务结束等回调函数

拒绝策略

线程工厂

###### ForkJoin

##### FAQ

###### 如何配置线程池最佳线程数

任务一般可分为：CPU密集型、IO密集型、混合型，根据不同的任务类型配置

CPU密集型，最佳线程数N+1。CPU使用率极高，在尽量避免切换线程的同时，配置一个额外线程，
保证某个线程由于其他原因暂停时，CPU的时钟周期不会浪费。

IO密集型，最佳线程数2N+1。CPU使用率不高，可以在CPU等待IO时，切换到其他线程处理其他任务，
充分利用CPU的时间。

混合型任务。将任务拆分为CPU密集型和IO密集型，分别使用不同的线程池处理。

##### FAQ

###### submit() VS execute()
       
两个方法都可以向线程池提交任务

execute()方法的返回类型是void，它定义在Executor接口中

submit()方法可以返回持有计算结果的Future对象，它定义在ExecutorService接口中，它扩展了Executor接口，
其它线 程池类像ThreadPoolExecutor和ScheduledThreadPoolExecutor都有这些方法。

异常处理

execute()方法会直接抛出异常

submit()方法在get()时抛出异常

> 如何优雅的使用和理解线程池 https://mp.weixin.qq.com/s?__biz=MzIyMzgyODkxMQ==&mid=2247483842&idx=1&sn=af7514a1e132ba84075d5ddc2f1c6b0b&chksm=e8190f02df6e8614f13ec04cee11f035fe23a29cd9c682ef18cbe52716c90d84f9581815f054&scene=21#wechat_redirect

> 线程池中你不容错过的一些细节 https://mp.weixin.qq.com/s?__biz=MzIyMzgyODkxMQ==&mid=2247484144&idx=1&sn=d4b992f4a0c66bafb906683936d204d9&chksm=e8190c30df6e85266d98a4aabf24be2b96775db7baa47634418a640465d4c35c31b2157c7b99&scene=21#wechat_redirect