synchronized
-

synchronized
	类锁
		static synchronized方法
		synchronized(xxx.class)方法块
	对象锁
		synchronized方法
		synchronized(this)方法块
		synchronized(object)方法块

	

同步方法和同步代码块

方法级别(粗粒度锁)
代码块级别(细粒度锁)。

静态、非静态

代码块可以指定锁对象

使用：
	//针对特定属性进行加锁，可以增加效率，提高性能。
	public class Synchronized {

		private List<String> someFields;

		public void add(String someText) {
			synchronized (someFields) {
				someFields.add(someText);
			}
		}
		
		public Object[] getSomeFields() {
			synchronized (someFields) {
				return someFields.toArray();
			}
		}
	}
	
	//lock对象可以使用this代替
	public class Synchronized {
		
		private Object lock = new Object();

		private List<String> someFields1;
		private List<String> someFields2;
		
		public void add(String someText) {
			//some code
			synchronized (lock) {
				someFields1.add(someText);
				someFields2.add(someText);
			}
			//some code
		}
		
		public Object[] getSomeFields() {
			//some code
			Object[] objects1 = null;
			Object[] objects2 = null;
			synchronized (lock) {
				objects1 = someFields1.toArray();
				objects2 = someFields2.toArray();
			}
			Object[] objects = new Object[someFields1.size() + someFields2.size()];
			System.arraycopy(objects1, 0, objects, 0, objects1.length);
			System.arraycopy(objects2, 0, objects, objects1.length, objects2.length);
			return objects;
		}
	}
	
	//顺序必须正确， 不然会死锁
	public class Synchronized {
		
		private List<String> someFields1;
		private List<String> someFields2;
		
		public void add(String someText) {
			//some code
			synchronized (someFields1) {
				synchronized (someFields2) {
					someFields1.add(someText);
					someFields2.add(someText);
				}
			}
			//some code
		}
		
		public Object[] getSomeFields() {
			//some code
			Object[] objects1 = null;
			Object[] objects2 = null;
			synchronized (someFields1) {
				synchronized (someFields2) {
					objects1 = someFields1.toArray();
					objects2 = someFields2.toArray();
				}
			}
			Object[] objects = new Object[someFields1.size() + someFields2.size()];
			System.arraycopy(objects1, 0, objects, 0, objects1.length);
			System.arraycopy(objects2, 0, objects, objects1.length, objects2.length);
			return objects;
		}
	}
	
	
[synchronized底层实现](https://github.com/farmerjohngit/myblog/issues/12)