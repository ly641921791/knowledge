线程
-

###### 创建线程

1. 继承Thread类
2. 实现Runnable接口
3. 使用Executor框架创建线程池

### 相关API

获得当前Thread执行的类名、方法名、行号

通过该对象`Thread.currentThread().getStackTrace()[1]`，可以得到。



###### Callable & Future/FutureTask
	
Callable用于执行

public interface Callable<V>{
	V call() throws Exception;
}


ExecutorService{
	<T> Future<T> submit(Callable<T> task);
	<T> Future<T> submit(Runnable task,T result);
	Future<?> submit(Runnable task);
}



Callable<Integer> callable = new Callable<Integer>() {
	@Override
	public Integer call() throws Exception {
		Thread.sleep(5000);// 可能做一些事情
		return new Random().nextInt(100);
	}
};
FutureTask<Integer> future = new FutureTask<Integer>(callable);
new Thread(future).start();
try {
	System.out.println(future.get());
} catch (InterruptedException e) {
	e.printStackTrace();
} catch (ExecutionException e) {
	e.printStackTrace();
}


##### FAQ

###### start()和run()方法的区别

start()方法会创建新线程，通过新线程异步执行run()方法

run()方法使用当前线程同步执行内部逻辑

