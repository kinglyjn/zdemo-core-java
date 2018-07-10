package test03_thread_and_juc;

import java.util.concurrent.TimeUnit;

/**
 * 线程的等待唤醒机制
 * 
 * 等待-通知机制，是指一个线程A调用了对象O的wait方法，进入等待状态，而另一个线程B调用了O对象的notify或notifyAll
 * 方法，线程A接收到通知以后从对象O的wait方法返回，进而执行后续的操作。上述两个线程通过对象O来完成交互，而对象上的
 * wait和notify或notfyAll的关系就好像开关信号一样，用来完成等待方和通知方的交互工作。
 * 
 * wait()：调用该方法进入WAITING状态，只有等待另外线程的通知或被中断才会返回，需要注意的是，调用该方法会释放对象的锁（而sleep则不会释放锁）。
 * wait(long)：超时等待一段时间（ms），如果没有通知就返回。
 * wait(long, int)：对于超时时间更加精准的控制，可以达到ns。
 * notify()：通知一个在对象上等待的线程，使其从wait()方法返回，而返回的前提是该线程获取到了对象的锁。
 * notifyAll()：通知所有等待在该对象上的线程。
 * 
 * 需要注意的细节如下：
 * 1、使用wait、notify、notifyAll时需要先调用对象加锁，并且等待-通知两个线程需要使用同一个锁对象
 * 2、调用wait方法以后，线程状态由RUNNING变为WAITING，并将当前线程放置到对象的等待队列
 * 3、notify或notifyAll方法被调用后，等待线程依旧不会从wait返回，只有调用notify或notifyAll的线程释放锁之后，
 *    等待线程才有机会抢到锁从wait返回。
 * 4、notify方法是将等待队列中的一个等待线程从等待队列中移到同步队列中，而notifyAll方法则是将等待队列中所有的线
 *    程都移到同步队列中，被移动的线程由WAITING状态变为BLOCKED状态。
 *    
 *    
 * 等待-通知模式的经典范式：
 * 等待方遵循以下原则：
 * 1. 获取对象的锁
 * 2. 如果条件不满足，那么就调用对象的wait方法，被通知后仍要检查条件
 * 3. 满足条件则执行对应的逻辑
 * synchronized (A对象) {
 *     while (条件不满足) {
 *         A对象.wait()
 *     }
 *     条件满足时的逻辑
 * }
 * 
 * 通知方遵循如下原则：
 * 1. 获得对象的锁
 * 2. 改变条件
 * 3. 通知所有等待在对象上的线程
 * synchronized (A对象) {
 *     改变条件
 *     A对象.notifyAll();
 * }
 * 
 * 注意上述语法中： 
 * 等待方调用A对象等待方法的时候可以不强制加锁
 * 通知方调用A对象的通知方法的时候必须加A对象的锁
 * 
 */
public class Test12OfWaitNotify {
	
	public static void main(String[] args) {
		Thread waitThread = new Thread(new Wait(), "WaitThread");
		waitThread.start();
		MyLock.sleep(1);
		
		Thread notifyThread = new Thread(new Notify(), "NotifyThread");
		notifyThread.start();
	}
	
	//
	static class MyLock {
		static volatile boolean ok = false;
		static Object lock = new Object();
		
		public static void sleep(long seconds) {
			try {
				TimeUnit.SECONDS.sleep(seconds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	static class Wait implements Runnable {
		@Override
		public void run() {
			synchronized (MyLock.lock) {
				// 当条件不满足时，继续wait，同时释放lock的锁。
				while (!MyLock.ok) {
					// 这里判断条件最好使用while，以达到被通知后再次校验的目的
					try {
						System.out.println(Thread.currentThread().getName() + ", ok=" + MyLock.ok);
						MyLock.lock.wait();
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				// 当条件满足时，完成工作
				System.out.println(Thread.currentThread().getName() + ", ok=" + MyLock.ok);
			}
		}
	}
	
	static class Notify implements Runnable {
		@Override
		public void run() {
			//
			synchronized (MyLock.lock) {
				System.out.println(Thread.currentThread().getName() + " hold the lock, notify...");
				MyLock.ok = true;
				MyLock.lock.notify();
				MyLock.sleep(5);
			}
			//再次加锁
			synchronized (MyLock.lock) {
				System.out.println(Thread.currentThread() + " hold the lock again. sleeping...");
				MyLock.sleep(5);
			}
		}
	}
}
