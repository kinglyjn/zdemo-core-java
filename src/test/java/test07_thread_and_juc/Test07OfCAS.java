package test07_thread_and_juc;

import java.util.Random;

/**
 * CAS算法的模拟
 * JVM的CAS算法不是通过加锁的机制实现的，而是通过调用计算机底层CAS算法实现的。
 * 我们这里仅仅通过加锁的机制来模拟JVM的CAS算法
 *
 */
public class Test07OfCAS {
	/*
	 * main
	 * 
	 */
	public static void main(String[] args) {
		MyCompareAndSwap myCompareAndSwap = new MyCompareAndSwap();
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					int expectedValue = myCompareAndSwap.get();
					boolean b = myCompareAndSwap.compareAndSet(expectedValue, new Random().nextInt(100));
					System.out.println("b=" + b + ", value=" + myCompareAndSwap.get());
				}
			}).start();
		}
	}
	
	static class MyCompareAndSwap {
		private int value;
		
		// 获取值
		public synchronized int get() {
			return this.value;
		}
		
		// CAS设置
		public synchronized int compareAndSwap(int expectedValue, int newValue) {
			int oldValue = this.value;
			if (expectedValue==oldValue) {
				this.value = newValue;
			}
			return oldValue;
		}
		
		// 设置value的值
		public synchronized boolean compareAndSet(int expectedValue, int newValue) {
			return expectedValue==compareAndSwap(expectedValue, newValue);
		}
	}
}
