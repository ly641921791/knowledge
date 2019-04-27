线程
-

### 相关API

获得当前Thread执行的类名、方法名、行号

通过该对象`Thread.currentThread().getStackTrace()[1]`，可以得到。





##### FAQ

###### start()和run()方法的区别

start()方法会创建新线程，通过新线程异步执行run()方法

run()方法使用当前线程同步执行内部逻辑

