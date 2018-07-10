package test03_thread_and_juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * 测试 ThreadPoolExecutor线程池
 *
 */
public class Test18ThreadPoolExecutor {

	/**
	 * 验证shutdown和shutdownNow的区别
	 * shutdown方法只是发出了停止信号，等所有线程执行完毕会关闭线程池
	 * 而shutdownNow则是马上触发中断，立即停止所有任务
	 * 
	 */
	static class TaskWithoutResult implements Runnable {
		@Override
		public void run() {
			System.out.println("线程"+Thread.currentThread()+"开始运行");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) { //捕捉中断异常
				System.out.println("线程"+Thread.currentThread()+"被中断");
				return;
			}
			System.out.println("线程"+Thread.currentThread()+"结束运行");
		}
	}
	@Test
	public void test01() throws InterruptedException {
		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 20, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10));
		
		for (int i = 0; i < 10; i++) {
			TaskWithoutResult task = new TaskWithoutResult();
			poolExecutor.submit(task);
		}
		
		poolExecutor.shutdown(); 		//不会触发中断
		//poolExecutor.shutdownNow(); 	//马上触发中断
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	
	/**
	 * 验证线程池的扩容
	 * 在本例中想要验证线程池扩容到核心数量，然后再扩容到最大数量，最后再缩小到核心数量的过程
	 * 
	 * 在VisualVM中观察线程的变化，在任务提交的瞬间，线程池完成了预热到扩容到最大线程
	 * 之所以这么迅速是因为本例中的等待队列长度只有1，可以适当地增加队列长度，但不并不一定能看到扩大最大容量。
	 * 在线程池中任务都运行完毕后，可以看到线程池回收了多余的线程，但并没有完全回收，而是保持在核心线程数量。
	 * 从这里也可以看出，合理地设置核心线程的数量可以减少线程的频繁创建和回收，而这才是线程池的真正作用。
	 * 
	 */
	static class TaskBusyWithoutResult implements Runnable {
		@Override
		public void run() {
			System.out.println("线程"+Thread.currentThread()+"开始运行");
			int i=10000*10000;
			while(i>0) {
				i--;
			}
			System.out.println("线程"+Thread.currentThread()+"运行结束");
		}
	}
	@Test
	public void test02() throws InterruptedException {
		Thread.sleep(10000);
		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 20, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10));
		for (int i = 0; i < 20; i++) {
			Runnable r = new TaskBusyWithoutResult();
			poolExecutor.submit(r);
		}
		Thread.sleep(Integer.MAX_VALUE);
	}
	
}
