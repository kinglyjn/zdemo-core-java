package test03_thread_and_juc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 测试 ScheduledExecutorService接口 及其实现类 ScheduledThreadPoolExecutor
 * @author zhangqingli
 *
 */
public class Test21ScheduledThreadPoolExecutor {
	
	/*
	 * main
	 * 
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		final int poolSize = 5;
		final int taskNum = 10;
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(poolSize);
		System.out.println(executor.getClass()); // ScheduledThreadPoolExecutor
		List<Future<Integer>> list = new ArrayList<>(taskNum);
		
		for (int i = 0; i < taskNum; i++) {
			ScheduledFuture<Integer> future = executor.schedule(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					int sum = 0;
					for (int i = 0; i <= 100; i++) {
						sum += i;
					}
					System.out.println(Thread.currentThread().getName() + ": done!");
					return sum;
				}
			}, new Random().nextInt(10), TimeUnit.SECONDS);
			
			list.add(future);
		}
		
		for (Future<Integer> future : list) {
			System.out.println(future.get());
		}
		
		System.out.println("---------");
		
	}
	
	/*
	class java.util.concurrent.ScheduledThreadPoolExecutor
	pool-1-thread-2: done!
	pool-1-thread-3: done!
	pool-1-thread-4: done!
	pool-1-thread-1: done!
	pool-1-thread-5: done!
	pool-1-thread-2: done!
	5050
	pool-1-thread-3: done!
	5050
	pool-1-thread-4: done!
	5050
	pool-1-thread-1: done!
	5050
	5050
	5050
	5050
	5050
	pool-1-thread-5: done!
	5050
	5050
	---------
	*/
}
