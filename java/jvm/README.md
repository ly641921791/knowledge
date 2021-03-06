

[引用](reference.md)


[Code Cache](https://mp.weixin.qq.com/s/mmrltf3vqrVOgBPGoC2bCQ)

[JVM内存模型](memory_model.md)

[垃圾回收算法](gc_algorithm.md)

[垃圾回收-HotSpot虚拟机](gc_hotspot.md)

[GC调优参数](gc_optimize.md)







## JVM工具

jps ：显示当前系统全部java进程
jstack ：加上进程号，可以查询进程内部线程的状态及堆栈信息
jmap ：








## 面试题

### 一个进程有三个线程，如果一个线程抛出OOM，其他两个线程还可以运行吗

　　A ：当该线程OOM后，占据的内存资源会被全部释放，不影响其他线程运行。





### JVM调优

#### 目的

　　从性能的角度看，关注三个方面：内存占用（footprint）、延时（latency）、吞吐量（throughput）。一般情况调优会侧重一个或两个方面，很少兼顾三个方面。还可以能考虑其他方面。




## 常见问题解决方案

### CPU占用100%

可能由于死循环导致

1 进程 查看占用CPU最多的进程
	
	top
	
2 堆空间 每ms抽样一次pid的堆空间信息
		
	jstat -gcutil pid ms
		
	liuyangdeMBP-2:~ ly$ jstat -gcutil 911 1000
	  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT   
	 99.44   0.00   6.96   4.75  96.35  90.91      4    0.027     1    0.009    0.036
	 99.44   0.00   6.96   4.75  96.35  90.91      4    0.027     1    0.009    0.036
	
3 栈内存 

3.1 列出java进程的线程信息
		
	top -H -p pid
		
	[root@iz2zehlhx1j2g3bk5jxtp5z ~]# top -H -p 6636
	
	top - 14:56:21 up 179 days,  4:35,  1 user,  load average: 0.00, 0.01, 0.05
	Threads:  52 total,   0 running,  52 sleeping,   0 stopped,   0 zombie
	%Cpu(s):  0.0 us,  0.2 sy,  0.0 ni, 99.8 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
	KiB Mem :  8010456 total,  2301432 free,  3042880 used,  2666144 buff/cache
	KiB Swap:        0 total,        0 free,        0 used.  4659292 avail Mem 
	
	  PID USER      PR  NI    VIRT    RES    SHR S %CPU %MEM     TIME+ COMMAND                                                                                                          
	 6636 root      20   0 4793872 1.100g  14388 S  0.0 14.4   0:00.00 java                                                                                                             
	 6637 root      20   0 4793872 1.100g  14388 S  0.0 14.4   0:13.81 java
	 
3.2 保存指定线程日志

	jstack -l pid >> stack.log  
	
	日志中pid是十六进制，top中pid是十进制，转换后查看
	
### 查找Java进程中占用CPU最多的线程

1. 确定进程ID，使用`jps -v`或`top`查看

2. 查看该进程哪个线程占用大量CPU，`top -H -p  [PID]`

3. 将进程中所有线程输出到文件，`jstack [PID] > jstack.txt`

4. 在进程中查找对应的线程ID，`cat jstack.txt | grep -i [TID]`。 

   TID是线程id的16进制表示
   
   
   
   



[系统运行缓慢，CPU 100%，以及Full GC次数过多问题的排查思路](https://mp.weixin.qq.com/s/_tWm2G57vLgomvpNNHKAMA)

[内存泄漏排查](https://mp.weixin.qq.com/s/M02Qk5OQ13xRytTK97SaFw)

[JVM内存常见问题](http://www.cnblogs.com/QG-whz/p/9636366.html)