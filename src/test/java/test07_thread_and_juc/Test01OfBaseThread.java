package test07_thread_and_juc;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * 基础线程测试
 * @author zhangqingli
 *
 */
public class Test01OfBaseThread {
	
	/**
	 * 测试01 Runnable
	 */
	static class RunnableImpl01 implements Runnable {
		private int number;
		public RunnableImpl01(int number) {
			this.number = number;
		}
		@Override
		public void run() {
			while (true) {
				System.out.println(Thread.currentThread() + ": number=" + number);
				try { Thread.sleep(1000); } catch(Exception e) {}
			}
		}
	}
	@Test
	public void test01() throws InterruptedException {
		for (int i = 0; i < 3; i++) {
			Thread t = new Thread(new RunnableImpl01(i));
			t.start();
		}
		Thread.sleep(10000); //
	}
	
	
	
	/**
	 * 测试02 获取和设置线程信息
	 * 
	 */
	static class RunnableImpl02 implements Runnable {
		private int number;
		public RunnableImpl02(int number) {
			this.number = number;
		}
		@Override
		public void run() {
			try {Thread.sleep(3000);} catch (InterruptedException e) {}
			System.out.println(Thread.currentThread() + ": number=" + number);
		}
		
		public static void printCurrentThread(Thread thread, Thread.State oldState) {
			System.out.println(
				"name: " + thread.getName() 
				+ ", priority: " + thread.getPriority() 
				+ ", old state: " + oldState
				+ ", new state: " + thread.getState());
			System.out.println("----------------------");
		}
	}
	@Test
	public void test02() throws InterruptedException {
		int length = 10;
		Thread[] threads = new Thread[length];
		Thread.State[] states = new Thread.State[length];
		
		// 创建线程
		for (int i=0; i<length; i++) {
			threads[i] = new Thread(new RunnableImpl02(i));
			if (i%2==0) {
				threads[i].setPriority(Thread.MAX_PRIORITY);
			} else {
				threads[i].setPriority(Thread.MIN_PRIORITY);
			}
			threads[i].setName("Thread-" + i);
			
			RunnableImpl02.printCurrentThread(threads[i] , states[i]); // old state=null, new state=NEW
			states[i] = threads[i].getState();
		}
		
		// 启动线程
		for (int i = 0; i < 10; i++) {
			threads[i].start();
			
			RunnableImpl02.printCurrentThread(threads[i] , states[i]); // old state=NEW, new state=RUNNABLE
			states[i] = threads[i].getState();
		}
		
		// 线程运行中
		boolean finish=false;
		while (!finish) {
			for (int i=0; i<length; i++){
				if (states[i] != threads[i].getState()) {
					RunnableImpl02.printCurrentThread(threads[i] , states[i]);
					states[i]=threads[i].getState();
					// 或者 old state: RUNNABLE, new state: TIMED_WAITING
					// 或者 old state: TIMED_WAITING, new state: RUNNABLE
					// 或者 old state: TIMED_WAITING, new state: TERMINATED
					// 或者 old state: TIMED_WAITING, new state: BLOCKED
					// 或者 old state: BLOCKED, new state: TERMINATED
				}
			}
			//
			finish=true;
			for (int i=0; i<10; i++){
				finish=finish && (threads[i].getState()==Thread.State.TERMINATED);
			}
		}
		//
		Thread.sleep(100000);
	}
	
	
	
	/**
	 * 测试03 线程的中断
	 * 1. java中提供 thread.interrupt() 和 thread.isInterrupted() 来控制线程的中断，不过可能会出现难以预料的结果，一般不用这种方法
	 * 2. 线程的中断我们一般自己写程序判断和控制
	 * 
	 */
	static class Runnable03 implements Runnable {
		public volatile boolean interrupted = false;
		
		@Override
		public void run() {
			int n = 1;
			while (true) {
				if (isPrime(n)) {
					System.out.printf("Num %d is Prime\n", n);
				}
				if (this.interrupted) {
					System.out.printf("The Prime Generator has been Interrupted\n");
					break;
				}
				n++;
				try {Thread.sleep(500);} catch (InterruptedException e) {}
			}
		}
		
		// 判断一个数字是否是素数
		public boolean isPrime(int n) {
			if (n < 2) {
				return true;
			}
			for (int i = 2; i < n; i++) {
				if (n%i==0) {
					return false;
				}
			}
			return true;
		}
	}
	@Test
	public void test03() throws InterruptedException {
		// 创建和开启素数生成线程
		Runnable03 r03 = new Runnable03();
		new Thread(r03).start();
		// 等待5秒，然后中断素数生成线程
		Thread.sleep(5000);
		r03.interrupted = true;
		Thread.sleep(500000);
	}
	
	
	
	/**
	 * 测试04 线程的睡眠与恢复
	 * 
	 */
	@Test
	public void test04() throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					try {
						TimeUnit.SECONDS.sleep(1);
						System.out.println(i);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
		}).start();
		TimeUnit.SECONDS.sleep(10);
	}
	
	
	
	/**
	 * 测试05 线程的插入 
	 * thread.join(): 在当前线程中调用该方法时，当前线程会被暂停，直到调用join方法的线程执行完毕！
	 * 
	 */
	@Test
	public void test05() throws InterruptedException {
		// 模拟线程1
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("["+ new Date() +"] datasource loading...");
				try {TimeUnit.SECONDS.sleep(10);} catch (InterruptedException e) {} //资源加载10s
				System.out.println("["+ new Date() +"] datasource loading has finished!");
			}
		});
		// 模拟线程2
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("["+ new Date() +"] networkConnections loading...");
				try {TimeUnit.SECONDS.sleep(15);} catch (InterruptedException e) {} //网络连接15s
				System.out.println("["+ new Date() +"] networkConnections loading has finished!");
			}
		});
		t1.start();
		t2.start();
		
		for (int i = 0; i < 10; i++) {
			System.out.println("["+ i +"] " + Thread.currentThread().getName());
			if (i==5) {
				t1.join();
				t2.join();
			}
			TimeUnit.SECONDS.sleep(1);
		}
	}
	
	
	
	/**
	 * 测试06 守护线程的创建和运行
	 * Java有一种特别的线程叫做守护线程。这种线程的优先级非常低，通常在程序里没有其他线程运行时才会执行它。
	 * 当进程中不存在非守护线程了，则守护线程自动销毁。典型的守护线程就是垃圾回收线程，当进程中没有非守护
	 * 线程了，则垃圾回收线程也就没有存在的必要了，自动销毁。用个比较通俗的比喻来解释一下守护线程，就是：
	 * 任何一个守护线程都是整个JVM非守护线程的“保姆”。
	 * 
	 * 根据这些特点，守护线程通常用于在同一程序里给普通线程（也叫使用者线程）提供服务。
	 * 它们通常无限循环的等待服务请求或执行线程任务。它们不能做重要的任务，因为我们不
	 * 知道什么时候会被分配到CPU时间片，并且只要没有其他线程在运行，它们可能随时被终止。
	 * JAVA中最典型的这种类型代表就是垃圾回收器。
	 * 
	 * 在这个测试中, 我们将学习如何创建一个守护线程，开发一个用2个线程的例子；
	 * 我们的使用线程会写事件到queue, 守护线程会清除queue里10秒前创建的事件。
	 * 
	 */
	static class Event {
		public Date date;
		public String event;
		public Event(Date date, String event) {
			this.date = date;
			this.event = event;
		}
	}
	static class WriterTask implements Runnable {
		private Deque<Event> deque; //双向队列，可实现栈的数据结构
		public WriterTask(Deque<Event> deque) {
			this.deque = deque;
		}
		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				Event event = new Event(new Date(), String.format("The thread %s has generated an event", Thread.currentThread().getId()));
				deque.addFirst(event);
				try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {}
			}
		}
	}
	static class CleanerTask extends Thread {
		private Deque<Event> deque;
		public CleanerTask(Deque<Event> deque) {
			//设置为守护线程，只能在start()方法之前可以调用setDaemon()方法。因为一旦线程运行了，就不能修改守护状态。
			super.setDaemon(true);
			this.deque = deque;
		}
		@Override
		public void run() {
			while (true) {
				clean();
			}
		}
		
		public void clean() {
			//它获取最后的事件，如果它在10秒前被创建，就删除它并查看下一个事件。
			//如果一个事件被删除，它会写一个事件信息和queue的新的大小，为了让你看到变化过程
			if (deque.size()==0) {
				return;
			}
			
			long time = 0;
			boolean deleteFlag = false;
			do {
				Event event = deque.getLast();
				time = new Date().getTime() - event.date.getTime();
				if (time > 10000) {
					System.out.printf("Cleaner: %s\n", event.event); 
					deque.removeLast();
					deleteFlag = true;
				}
			} while (time > 10000);
			
			if (deleteFlag) {
				System.out.printf("Cleaner: Size of the queue: %d\n", deque.size());
			}
		}
	}
	@Test
	public void test06() throws InterruptedException {
		//工作线程向双队列中写事件
		Deque<Event> deque = new ArrayDeque<Event>();
		for (int i = 0; i < 3; i++) {
			Thread t = new Thread(new WriterTask(deque));
			t.start();
		}
		//守护线程清除10s之前的时间
		CleanerTask cleanerTask = new CleanerTask(deque);
		cleanerTask.start();
		//
		TimeUnit.SECONDS.sleep(100);
	}
	
	
	
	/**
	 * 测试07 在线程里处理不受控制的异常：
	 * 当在一个线程里抛出一个异常，但是这个异常没有被捕获（这肯定是非检查异常），JVM 检查线程的相关方法是否有
	 * 设置一个未捕捉异常的处理者 。如果有，JVM 使用Thread 对象和 Exception 作为参数调用此方法 。如果线程
	 * 没有捕捉未捕获异常的处理者， 那么 JVM会把异常的 stack trace 写入操控台并结束任务。
	 * 
	 * Thread 类有其他相关方法可以处理未捕获的异常。静态方法 setDefaultUncaughtExceptionHandler() 
	 * 为应用里的所有线程对象建立异常 handler 。当一个未捕捉的异常在线程里被抛出，JVM会寻找此异常的3种
	 * 可能潜在的处理者（handler）。
	 * 首先, 它寻找这个未捕捉的线程对象的异常handle，如我们在在这个指南中学习的。如果这个handle 不存在，
	 * 那么JVM会在线程对象的ThreadGroup里寻找非捕捉异常的handler，如此例。如果此方法不存在，那么 JVM 
	 * 会寻找默认非捕捉异常handle。如果没有一个handler存在, 那么 JVM会把异常的 stack trace 写入操控
	 * 台并结束任务。
	 * @throws InterruptedException 
	 * 
	 */
	@Test
	public void test07() throws InterruptedException {
		// 创建线程
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					System.out.println("[" + i + "]");
					if (i==5) {
						Integer.parseInt("TTT"); //未检查异常（RuntimeException），这里没有将它捕获处理
					}
				}
			}
		});
		// 为线程设置为检查异常处理对象
		t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("---------------异常信息-----------------");
				System.out.println("tname=" + t.getName() + ", tstate=" + t.getState() + ", tisalive=" + t.isAlive() + ", exception=" + e);
				// tname=Thread-0, tstate=RUNNABLE, tisalive=true, exception=java.lang.NumberFormatException: For input string: "TTT"
			}
		});
		// 启动线程
		t.start();
		//
		Thread.sleep(10000);
	}
	
	
	
	
}
