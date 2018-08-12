package test03_thread_and_juc;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 测试 读写锁 ReentrantReadWriteLock
 * 读锁 - 如果没有线程锁定ReadWriteLock进行写入，则多线程可以访问读锁。
 * 写锁 - 如果没有线程正在读或写，那么一个线程可以访问写锁。
 * 
 * 读写锁维护一对锁对象，即读锁和写锁
 * 读-读 能共存，读-写、写-写不能共存
 * 
 */
public class Test16ReadWriteLock {
	
	 /*
	 * main
	 * 
	 */
	public static void main(String[] args) {
		ReadWriteLockDemo rw = new ReadWriteLockDemo();
		for (int i = 0; i < 3; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try { Thread.sleep(200); } catch(Exception e){}
						rw.setState(new Random().nextInt(100));
					}
				}
			}).start();
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try { Thread.sleep(200); } catch(Exception e){}
						rw.getState();
					}
				}
			}).start();
		}
	}
	
	static class ReadWriteLockDemo {
		private int state = 0;
		private ReadWriteLock lock = new ReentrantReadWriteLock();
		
		/**
		 * 读
		 */
		public void getState() {
			lock.readLock().lock();
			try {
				System.out.println(Thread.currentThread().getName() + ": " + state);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * 写
		 */
		public void setState(int state) {
			lock.writeLock().lock();
			try {
				System.out.println(Thread.currentThread().getName() + ": write" );
				this.state = state;
			} finally {
				lock.writeLock().unlock();
			}
		}
	}
}
