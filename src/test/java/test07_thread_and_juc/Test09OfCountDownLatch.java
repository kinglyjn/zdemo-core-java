package test07_thread_and_juc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试 闭锁(CountDownLatch)
 * 在进行某些运算时，只有其他所有线程的运算全部完成，当前运算的线程才能继续往下执行
 *
 */
public class Test09OfCountDownLatch {
	
	public static void main(String[] args) throws InterruptedException {
		long start = System.currentTimeMillis();
		
		// core
		int taskCount = 3;
		CountDownLatch countDownLatch = new CountDownLatch(taskCount);
		RunnableImpl01 r01 = new RunnableImpl01(countDownLatch);
		for (int i = 0; i < taskCount; i++) {
			new Thread(r01).start();
		}
		countDownLatch.await(); 
		//
		
		System.out.println("耗时：" + (System.currentTimeMillis()-start) + " ms");
	}
	
	static class RunnableImpl01 implements Runnable {
		private CountDownLatch countDownLatch;
		private AtomicInteger i = new AtomicInteger(100);
		
		public RunnableImpl01(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}
		
		@Override
		public void run() {
			try {
				while (i.get() > 0) {
					int currentI = i.getAndDecrement();
					System.out.println(Thread.currentThread().getName() + ": " + currentI);
					try {Thread.sleep(100);} catch (Exception e) {}
				}
			} finally {
				countDownLatch.countDown();
			}
		}
	}
}
