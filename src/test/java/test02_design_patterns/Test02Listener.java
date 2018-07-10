package test02_design_patterns;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 测试 监听者模式
 *
 */
public class Test02Listener {
	
	 /*
	 * main
	 * 
	 */
	public static void main(String[] args) {
		Child child = new Child();
		
		child.setEvent(new WakeupEvent(new Date(), "卧室"));
		child.addListener(new Listener1()).addListener(new Listener2()).addListener(new Listener3());
		
		new Thread(child).start();
	}
	
	/**
	 * 事件抽象类
	 * 
	 */
	static abstract class Event {}
	/**
	 * 叫醒事件
	 * 
	 */
	static class WakeupEvent extends Event {
		private Date time;
		private String location;
		public WakeupEvent(Date time, String location) {
			this.time = time;
			this.location = location;
		}
		public Date getTime() {
			return time;
		}
		public void setTime(Date time) {
			this.time = time;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		@Override
		public String toString() {
			return "WakeupEvent [time=" + time + ", location=" + location + "]";
		}
	}
	
	/**
	 * 监听者接口
	 * 
	 */
	static interface Listener {
		void action(Event e);
	}
	/**
	 * 监听者1 2 3
	 * 
	 */
	static class Listener1 implements Listener {
		@Override
		public void action(Event e) {
			System.out.println("listener1 " + e);
		}
	}
	static class Listener2 implements Listener {
		@Override
		public void action(Event e) {
			System.out.println("listener2 " + e);
		}
	}
	static class Listener3 implements Listener {
		@Override
		public void action(Event e) {
			System.out.println("listener3 " + e);
		}
	}
	
	/**
	 * 事件源
	 * 
	 */
	static class Child implements Runnable {
		private WakeupEvent event;
		private List<Listener> listeners = new ArrayList<Listener>();
		
		public WakeupEvent getEvent() {
			return event;
		}
		public void setEvent(WakeupEvent event) {
			this.event = event;
		}
		public List<Listener> getListeners() {
			return listeners;
		}
		public void setListeners(List<Listener> listeners) {
			this.listeners = listeners;
		}
		public Child addListener(Listener listener) {
			this.listeners.add(listener);
			return this;
		}
		
		@Override
		public void run() {
			System.out.println("睡着了。。。");
			try {
				Thread.sleep(5000);
				System.err.println("醒了。。。");
				for (Listener listener : listeners) {
					listener.action(event);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
