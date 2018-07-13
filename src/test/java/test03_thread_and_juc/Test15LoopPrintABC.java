package test03_thread_and_juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程间通信-三个线程循环打印ABC
 * 此处使用 锁的等待唤醒机制 实现
 *
 */
public class Test15LoopPrintABC {
	
	 /*
	 * main
	 * 
	 */
	public static void main(String[] args) {
		LoopPrinter loopPrinter = new LoopPrinter();
		final int loopCount = 5;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < loopCount; i++) {
					loopPrinter.printA();
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < loopCount; i++) {
					loopPrinter.printB();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < loopCount; i++) {
					loopPrinter.printC();
				}
			}
		}).start();
	}
	
	/**
	 * LoopPrinter
	 *
	 */
	static class LoopPrinter {
		private int state = 1;
		private int percount = 3;
		private Lock lock = new ReentrantLock(false);
		private Condition condition1 = lock.newCondition();
		private Condition condition2 = lock.newCondition();
		private Condition condition3 = lock.newCondition();
		
		public void printA() {
			lock.lock();
			try {
				// 1.判断
				while (state != 1) {
					condition1.await();
				}
				// 2.打印
				for (int i = 0; i < percount; i++) {
					Thread.sleep(200);
					System.out.println(Thread.currentThread().getName() + ": A");
				}
				// 3.唤醒
				state = 2;
				condition2.signal();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
		
		public void printB() {
			lock.lock();
			try {
				// 1.判断
				while (state != 2) {
					condition2.await();
				}
				// 2.打印
				for (int i = 0; i < percount; i++) {
					Thread.sleep(200);
					System.out.println(Thread.currentThread().getName() + ": B");
				}
				// 3.唤醒
				state = 3;
				condition3.signal();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
		
		public void printC() {
			lock.lock();
			try {
				// 1.判断
				while (state != 3) {
					condition3.await();
				}
				// 2.打印
				for (int i = 0; i < percount; i++) {
					Thread.sleep(200);
					System.out.println(Thread.currentThread().getName() + ": C");
				}
				System.out.println("----------");
				// 3.唤醒
				state = 1;
				condition1.signal();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
	
}
