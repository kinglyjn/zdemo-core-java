package test03_thread_and_juc;

import java.util.concurrent.TimeUnit;

/**
 * volatile 关键字测试
 * 
 * 1. volatile关键字解决的是数据在多个线程之间的可见性（不保证被操作数据的原子一致性），
 *    而synchronized关键字解决的是操作的原子性，而且synchronized除了保证数据操作的
 *    原子性，也将线程工作内存中的私有变量与主内存中的公共变量同步。
 * 2. 主内存（=物理内存），线程工作内存（=CPU高速缓存）
 *    被volatile修饰的变量保证了数据在多个不同线程之间的同步性，原理如下：
 *    volatile修饰符使被它修饰的变量对应的CPU高速缓存区（L1、L2）失效，
 *    当volatile变量在线程A中被修改后，线程A会将其工作内存中的被修改
 *    后的volatile变量写入主内存中，然后发送信号通知其他线程从主内存中
 *    同步最新的值到工作内存，这样就保证了线程间变量数据的可见性。
 *    
 * 当多个线程操作共享数居时，volatile可以保证内存中的数据可见。通常情况下，JVM为每个线程分配独立的缓存空间用于提高效率。
 * 可以将volatile修饰的共享变量理解成 每个线程修改该变量直接在主存中直接修改，因而该变量在每个线程中是相互可见的。
 * 			主存
 * 			flag=false
 * 		----------------------
 * 		线程1		main线程
 *  	flag=true	flag=false
 * 
 * 注意：
 * 1.volatile不具有互斥性
 * 2.volatile不能保证变量的原子性
 *
 */
public class Test05OfVolatile {
	
	public static void main(String[] args) {
		MyTask task = new MyTask();
		for (int i = 0; i < 10; i++) {
			new Thread(task).start();
		}
		//
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//
		task.setInterrupted(true);
	}
	

	static class MyTask implements Runnable {
		private volatile boolean isInterrupted = false;
		
		public boolean isInterrupted() {
			return isInterrupted;
		}
		public void setInterrupted(boolean isInterrupted) {
			this.isInterrupted = isInterrupted;
		}

		@Override
		public void run() {
			while (true) {
				System.out.println("Thread: " + Thread.currentThread().getId() + " running!");
				if (isInterrupted) {
					break;
				}
			}
			System.out.println("Thread: " + Thread.currentThread().getId() + " end!");
		}
	}
}
