package test07_thread_and_juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.junit.Test;

/**
 * 测试 通过Callable接口创建线程
 * 
 * 创建线程的四种方法总结：
 * 1. Thread
 * 2. Runnable
 * 3. Callable：
 * 		i.相较于Runnable接口的方式，可以有返回值，并且可以抛出异常
 * 		ii.执行Callable形式的线程需要FutureTask实现类（实现了Future接口）的支持，用于在线程运行完成后接收call方法的返回值。
 * 4. 线程池
 *
 */
public class Test10OfCallable {
	
	/**
	 * 测试01 Callable接口的基本使用
	 *
	 */
	static class CallableImpl01 implements Callable<Integer> {
		@Override
		public Integer call() throws Exception {
			int sum = 0;
			for (int i = 0; i <= 100; i++) {
				sum += i;
				System.out.println("current sum=" + sum);
			}
			return sum;
		}
	}
	@Test
	public void test01() throws InterruptedException, ExecutionException {
		// 创建和启动线程进行计算
		FutureTask<Integer> futureTask = new FutureTask<>(new CallableImpl01()); // implements Runnable 
		Thread t = new Thread(futureTask);
		t.start();
		
		// 获取计算结果
		Integer result = futureTask.get();
		System.out.println("result=" + result);
		Thread.sleep(10000);
	}
}