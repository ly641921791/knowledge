JVM工具

https://mp.weixin.qq.com/s/hDQiWdHdVuuWvAoknD-ODg


###### jps（虚拟机进程状态工具：JVM Process Status Tool）

默认显示当前系统中Java进程的PID和启动类名

格式 ：jps [options] [hostid]

jps -q ：只输出PID

jps -l ：输出启动类全类名

jps -m ：启动时传递给main函数的参数

jps -v ：启动时的JVM参数

###### jstack

先通过jps获得进程ID，比如999，再通过jstack 999可以查看999进程的线程状态


