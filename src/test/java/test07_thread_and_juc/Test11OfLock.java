package test07_thread_and_juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock锁测试
 * 
 * 解决多线程安全问题的三种方式：
 * 1. synchronized同步代码块
 * 2. synchronized同步方法
 * 3. lock同步锁（>=jdk1.5）
 * 注意：lock是一个显式锁，需要通过lock方法上锁，且必须通过unlock方法释放锁！
 * 
 */
public class Test11OfLock {
	
	public static void main(String[] args) {
		TicketTask ticketTask = new TicketTask();
		for (int i = 0; i < 3; i++) {
			new Thread(ticketTask).start();
		}
	}
	
	static class TicketTask implements Runnable {
		// 公共资源
		private Integer ticketNum = 100;
		// 可重入锁
		private Lock lock = new ReentrantLock();
		
		@Override
		public void run() {
			while (true) {
				try {
					lock.lock();	//上锁	
					if (ticketNum < 0) {
						break;
					}
					System.out.println(Thread.currentThread() + "，余票为：" + --ticketNum);
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.unlock(); 	//释放锁
				}	
			}
		}
	}
}


