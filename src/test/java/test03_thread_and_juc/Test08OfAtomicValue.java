package test03_thread_and_juc;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

/**
 * 原子变量测试
 *  
 *  主存
 * 	i=0
 * 	----------------
 * 	线程1	main线程
 *  i=1		i=2 X
 *  
 *  JDK1.5之后，java.util.concurrent.atomic包下提供了常用的原子变量，通过CAS算法以保证对原子变量操作的原子性。
 *  1. 原子变量通过volatile关键字保证数据在内存（或者不同线程）中的可见性
 *  2. 原子变量通过CAS（compare and swap）算法保证对其操作的原子性
 *  
 */
public class Test08OfAtomicValue {
	
	static class RunnableImpl01 implements Runnable {
		private AtomicInteger i = new AtomicInteger(0);

		private Integer increment() {
			return i.getAndIncrement();
		}
		
		@Override
		public void run() {
			try {Thread.sleep(1000);} catch (InterruptedException e) {}
			System.out.println(Thread.currentThread().getName() + ": " + increment());
		}
	}
	
	@Test
	public void test01() throws InterruptedException {
		RunnableImpl01 r01 = new RunnableImpl01();
		for (int i = 0; i < 10; i++) {
			Thread t = new Thread(r01);
			t.start();
		}
		Thread.sleep(10000);
	}
	
	
}
