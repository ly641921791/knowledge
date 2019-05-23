观察者模式
-



	一个对象变化时，依赖的对象会收到通知，随着变化
	监听器模式

	Java提供的观察者模式相关的实现类和接口
		java.util.Observer
		java.util.Observable
	    
    //观察者接口
    public interface Observer{
        public void update();
    }
    //观察者
    public class Observer1{
        public void update(){}
    }
    public class Observer2{
        public void update(){}
    }
    //被观察者接口
    public interface Subject{
        public void add(Observer observer);
        public void remove(Observer observer);
        //通知观察者
        public void notifyObserver();
        //自身操作
        public void operation();
    }
    //被观察者
    public abstract class ASubject implements Subject{
        private Vector<Observer> vector=new Vector<Observer>();
        public void add(Observer observer){
            vector.add(observer);
        }
        public void remove(Observer observer){
            vector.remove(observer);
        }
        public void notifyObserver(){
            Enumeration<Observer> enum=vector.elements();
            while(enum.hasMoreElements()){
                enum.nextElement().update();
            }
        }
    }
    public class MySubject extends ASubject{
        public void operation(){
            //做改变后调用通知方法
            notifyObserver();
        }
    }
	
	
	
监听器模式
	事件源经过事件的封装传给监听器，当事件源触发事件后，监听器接收到事件对象可以回调事件的方法
	
	//事件源对象
	public class Source{
		private Vector listener = new vector();		//监听自己的监听器队列
		
		public void addListener(Listener listener){
			listener.addElement(listener);
		}
		
		public void removeListener(Listener listener){
			listener.removeElement(listener);
		}
		
		public void notifyListener(){
			Enumeration enum = listener.elements();
			while(enum.hasMoreElements()){
				enume.nextElement().handleEvent(new Event(this));
			}
		}
	}
	//事件
	public class Event extends java.util.EventObject{
		public Event(Object source){
			super(source);
		}
		//事件处理
		public void handle(){
		}
	}
	//监听器接口
	public interface Listener extends java.util.EventListener {         
		public void handleEvent(Event e);     
	}
	//监听器实现类
	public class ListenerImpl implements Listener {
		public void handleEvent(Event e) {
			e.handle();
		}
	}