package test03_thread_and_juc;

import org.junit.Test;

/**
 * 测试 线程的中断感知和中断响应
 * 中断线程的更好的方式还是设置中断标识
 *
 */
public class Test19ThreadPoolExecutor {
	
	static class TaskWithoutResult implements Runnable {
		@Override
		public void run() {
			try {
				System.out.println("线程" + Thread.currentThread().getName() + "开始运行");
				Thread.sleep(5000);
			} catch (InterruptedException e) { //感知中断，捕捉中断异常
				System.out.println("线程" + Thread.currentThread() + "被中断");
				return; //中断响应
			}
			System.out.println("线程"+Thread.currentThread()+"结束运行");
		}
	}
	
	/**
	 * 测试 线程中断
	 * 
	 */
	@Test
	public void test01() throws InterruptedException {
		Thread thread = new Thread(new TaskWithoutResult());
		thread.start();
		thread.interrupt();
		//////
		Thread.sleep(100000);
	}
}
