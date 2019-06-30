锁
- 

本文目录

- synchronized
- Lock
- synchronized与Lock对比

###### synchronized

JDK1.5之前只能通过synchronized关键字实现锁

根据锁的对象可以分为三种锁

- 类锁
	- synchronized修饰静态方法
	- synchronized(xxx.class)代码块
- 方法锁
	- synchronized修饰非静态方法
- 对象锁
	- synchronized(this)代码块
	- synchronized(object)代码块

根据锁的粒度可以分为两种锁

- 粗粒度锁 ：同步方法
- 细粒度锁 ：同步代码块

注意：

1. 对特定对象/属性加锁可以提高效率
2. 多个synchronized嵌套使用对象锁，加锁解锁顺序需要一致，不然容易引发死锁

###### Lock

JDK1.5新增了Lock接口

Lock不能像synchronized那样自动释放锁，因此必须在finally代码块中手动释放

Lock虽然提高了锁的复杂度，但也因此拥有了synchronized没有的特性





接口
	Lock
	Condition
	ReadWriteLock
类
	ReentrantLock
		implements Lock, java.io.Serializable
	ReentrantReadWriteLock
		ReadWriteLock, java.io.Serializable
		
		


### 语言层面的锁

Java的synchronized、Lock都是单机锁，集群环境下失效

### 分布式锁

#### 基于Mysql的分布式锁

方案一 唯一性约束

通过锁名的唯一性约束，保证同时只有一个操作可以成功

```mysql
-- 创建锁表
CREATE TABLE locks (
  id INT NOT NULL AUTO_INCREMENT,
  lock_name VARBINARY(64) NOT NULL COMMENT '锁名',
  other_column VARBINARY(64) COMMENT '其他字段',
  PRIMARY KEY (id),
  UNIQUE KEY (lock_name)
);
-- 加锁
INSERT INTO locks VALUES ('lock_name');
-- 解锁
DELETE FROM locks WHERE lock_name = 'lock_name';
```

缺点：重量级锁，加锁需要重复执行插入，不能自动释放锁

方案二 行锁

通过`行锁`解决方案一加锁需要重复执行插入的问题，当提交或回滚锁自动释放

```mysql
-- 在数据库中预先插入锁
INSERT INTO locks VALUES ('lock_name');
-- 加锁
SELECT * FROM locks WHERE lock_name = 'lock_name' FOR UPDATE;
```

缺点：重量级锁，一个线程加锁时间过长，会导致其他线程阻塞

方案三 CAS

CAS（Compare And Swap）实现加锁

下面是扣减库存操作的伪代码，更新失败则重复执行`查询-扣减-更新`操作直到库存不足

```
// 商品id 扣减数量
int productId = 1 , saleNum = 10;
// 查询库存
int oldNum = execute("SELECT num FROM stock WHERE product_id = #{productId}");
// 扣减库存，校验略过
int newNum = oldNum - saleNum;
// 更新库存
int updateCount = execute("UPDATE stock SET num = #{newNum} WHERE product_id = #{productId} AND num = #{oldNum}");
```

缺点：并发高的情况下，执行SQL的次数过多

#### 基于Redis的分布式锁

与MySQL锁同理，但是操作内存更快，且可以设置锁的过期时间

#### 基于Zookeeper的分布式锁

[锁](https://www.cnblogs.com/JJJ1990/p/10496850.html)






ReentrantLock
	可重入的锁：可以被递归函数调用的锁
	
	可以通过final Condition condition1 = lock.newCondition();创建状态接口的实现类对象
	
	解决了synchronized关键字同步的部分问题
		1：synchronized关键字同步的时候，等待的线程将无法控制，只能死等
			ReentrantLock使用tryLock(timeout, unit)方法去控制等待获得锁的时间，也可以使用无参数的tryLock方法立即返回，这就避免了死锁出现的可能性。
		2：synchronized关键字同步的时候，不保证公平性，因此会有线程插队的现象。
			ReentrantLock使用构造方法ReentrantLock(fair)来强制使用公平模式，保证线程按照等待的顺序获得锁
			synchronized进行同步的时候，是默认非公平模式的
	缺点
		需要开发人员手动在finally块中释放锁
		
使用
	public class Lock {
		private ReentrantLock nonfairLock = new ReentrantLock();	//非公平模式：无序的锁竞争
		private ReentrantLock fairLock = new ReentrantLock(true);	//公平模式：有序的锁竞争

		private List<String> someFields;

		public void add(String someText) {
			nonfairLock.lock();
			try {
				someFields.add(someText);
			} finally {
				nonfairLock.unlock();
			}
		}

		public void addTimeout(String someText) {
			// 尝试获取，如果10秒没有获取到则立即返回
			try {
				if (!fairLock.tryLock(10, TimeUnit.SECONDS)) {
					return;
				}
			} catch (InterruptedException e) {
				return;
			}
			try {
				someFields.add(someText);
			} finally {
				fairLock.unlock();
			}
		}
	}

	Condition
		解决了wait方法的部分问题
			1：wait(timeout)和wait(timeout,nanos)，不能反馈是被唤醒还是到达了时间
				Condition使用返回值标识是否达到了超时时间
			2：wait,notify,notifyAll方法都需要获得当前对象的锁，当出现多个条件等待时，则需要依次获得多个对象的锁，麻烦且繁琐
				Condition只需要获得Lock的锁即可，一个Lock可以拥有多个条件
		使用
			public class ConditionTest {
				private static ReentrantLock lock = new ReentrantLock();
				
				public static void main(String[] args) throws InterruptedException {
					final Condition condition1 = lock.newCondition();
					final Condition condition2 = lock.newCondition();
					Thread thread1 = new Thread(new Runnable() {
						public void run() {
							lock.lock();
							try {
								System.out.println("等待condition1被通知！");
								condition1.await();
								System.out.println("condition1已被通知，马上开始通知condition2！");
								condition2.signal();
								System.out.println("通知condition2完毕！");
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							} finally {
								lock.unlock();
							}
						}
					});
					Thread thread2 = new Thread(new Runnable() {
						public void run() {
							lock.lock();
							try {
								System.out.println("马上开始通知condition1！");
								condition1.signal();
								System.out.println("通知condition1完毕，等待condition2被通知！");
								condition2.await();
								System.out.println("condition2已被通知！");
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							} finally {
								lock.unlock();
							}
						}
					});
					thread1.start();
					Thread.sleep(1000);
					thread2.start();
				}
			}
			
			
ReentrantReadWriteLock
	写时，其他线程无法读写。读时，其他线程可读不可写。
	
	一般用于：缓存、大数据量的并发访问（读操作明显多余写）

	writeLock支持condition
	
	降级：writeLock变成readLock成为锁降级
		具体操作为
			writeLock.lock
			readLock.lock
			writeLock.unlock
	
	
使用：
	//缓存
	class CacheMap {

		// 缓存的数据的Map
		private Map<String, String> map = new HashMap<String, String>();
		// 创建读写锁
		private ReadWriteLock rwl = new ReentrantReadWriteLock();

		public void put(String key, String value) {
			try {
				rwl.writeLock().lock(); // 加锁
				map.put(key, value);
			} finally {
				rwl.writeLock().unlock(); // 解锁
			}
		}

		public String get(String key) {
			try {
				rwl.readLock().lock();
				return map.get(key);
			} finally {
				rwl.readLock().unlock();
			}
		}
	}






[synchronized底层实现](https://github.com/farmerjohngit/myblog/issues/12)






###### 无锁编程

其实就是乐观锁，通过CAS实现的无锁。

##### ReentrantLock

- 可重入
- 可中断
- 可限时
- 公平锁

###### 可重入

因为锁是可重入的，可以加锁多次，同时必须释放多次，因为会记录进入锁的次数

```java
public class Demo {
    public static void main(String[] args){
    	ReentrantLock lock = new ReentrantLock();
    	lock.lock();
    	lock.lock();
    	try {
    	    // 逻辑处理
    	} finally{
    	    lock.unlock();
    	    lock.unlock();
    	}
    }
}
```

###### 可中断

加锁的同时，可以响应中断，通过添加守护线程，定时将可以用来防止死锁

```java
public class Demo {
    public static void main(String[] args){
        ReentrantLock lock = new ReentrantLock();
        try {
            // 响应中断的加锁
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            
        } finally {
            // 检查当前线程是否获得锁，再解锁
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
    
    private void checker() {
        // 获得线程管理器
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 查找死锁id
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
        // 查找死锁信息
        ThreadInfo[] threadInfo = threadMXBean.getThreadInfo(deadlockedThreads);
        // 遍历当前栈，将死锁线程中断
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            for (int i = 0; i < threadInfo.length; i++) {
                if (thread.getId() == threadInfo[i].getThreadId()) {
                    thread.interrupt();
                }
            }
        }
    }
}
```

###### 可限时

在给定的时间内加锁，加锁失败直接返回

```java
public class Demo {
    public static void main(String[] args){
    	ReentrantLock lock = new ReentrantLock();
    	try {
    	    if (lock.tryLock(5,TimeUnit.SECONDS)){
    	        // 加锁成功
    	    } else {
    	        // 加锁失败
    	    }
    	} catch (InterruptedException e) {
    	    //
    	} finally {
    	    if (lock.isHeldByCurrentThread()) {
    	        lock.unlock();
    	    }
    	}
    }
}
```

###### 公平锁

线程先到先得，防止某些线程得不到锁，性能会比非公平锁差很多，通过构造函数`public ReentrantLock(boolean fair)`控制

###### Condition

类似于synchronized锁，加锁对象提供了wait()、notify()、notifyAll()方法用于线程通信

Condition就是ReentrantLock的锁对象，也提供了类似方法

##### Semaphore

信号量，用来控制最大并发线程

```java
public class Demo {
    public static void main(String[] args){
    	Semaphore semaphore = new Semaphore(5);
    	semaphore.acquire();
    	try {
    	    
    	} finally {
    		semaphore.release();
    	}
    }
}
```

##### ReadWriteLock

读写锁

##### CountDownLatch

##### CyclicBarrier

cyclic [ˈsaɪklɪk] 循环的

barrier [ˈbæriə(r)] 屏障

线程组需要做A、B、C...几件事，但是前一件事必须都做完才可以做下一件事，此时使用

##### LockSupport

park() 暂停

unpark(Thread) 继续 

#### 并发容器

ConcurrentHashMap

BlockingQueue

ConcurrentLinkedQueue