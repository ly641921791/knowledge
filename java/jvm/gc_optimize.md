GC 调优参数
-

###### 栈参数

|参数|描述|
|---|---|
|-Xss|栈容量|

###### 堆参数

|参数|描述|
|---|---|
|-Xms|堆的内存初始值，若设置跟最大值一样，可以避免自动扩容|
|-Xmx|堆的内存最大值|
|-Xmn|年轻代的容量，剩余为老年代|
|-XX:SurvivorRatio|新生代中Eden区域和Survivor区域的比例，默认8|
|-XX:newRatio|老年代（不含永久代）和新生代（Eden+2*Survivor）比例，默认2。
|JDK1.8废弃|
|-XX:PermGen|永久代内存初始值，（JDK1.8废弃，加入元空间）|
|-XX:MaxPermGen|永久代内存最大值|

###### 回收器参数

|参数|描述|
|---|---|
|-XX:+UseSerialGC|串行，Young和Old都使用串行，复制算法回收|
|-XX:+UseParallelGC|并行，在Young使用Parallel scavenge算法，通过-XX:ParallelGCThreads=n设置线程数，默认CPU核数。Old单线程|
|-XX:+UseParallelOldGC|并行，在Young和Old都并行收集|
|-XX:+UseConcMarkSweepGC|并发，短暂停顿的并发收集。Young使用普通或并行垃圾收集算法，由参数-XX:UseParNewGC控制，Old只能使用Concurrent Mark Sweep|
|-XX:+UseG1GC|并行、并发的增量式压缩短暂停顿的收集器。不区分Young和Old，将堆划分为多个大小相等的区域，垃圾收集优先收集存货对象少的区域

主要区分为串行、并行、并发三种，大内存为了保证性能，使用并行和并发。通常Young选择并行（耗时间），Old选择并发（耗CPU）
