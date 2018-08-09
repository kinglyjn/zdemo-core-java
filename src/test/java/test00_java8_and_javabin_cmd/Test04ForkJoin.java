package test00_java8_and_javabin_cmd;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;
import org.junit.Test;

/**
 * 测试 Fork/Join框架（在CPU内核中工作的线程底层使用工作窃取模式执行线程任务）
 * 
 */
public class Test04ForkJoin {
	
	/**
	 * 任务类
	 * extends RecursiveTask（有返回值的任务）
	 * extends RecursiveAction（无返回值的任务）
	 *
	 */
	static class Task extends RecursiveTask<Long> { 
		private static final long serialVersionUID = 1L;
		private static final long THRESHOLD = 100000000L;
		private long start;
		private long end;
		
		public Task(long start, long end) {
			this.start = start;
			this.end = end;
		}
		
		@Override
		protected Long compute() {
			long sum = 0L;
			long length = end-start;
			
			if (length <= THRESHOLD) {
				for (long i = start; i <= end; i++) {
					sum += i;
				}
			} else {
				long middle =  (start+end)/2;
				
				Task leftTask = new Task(start, middle);
				leftTask.fork(); // 进行任务的拆分，同时压入线程队列
				
				Task rightTask = new Task(middle+1, end);
				rightTask.fork(); // 进行任务的拆分，同时压入线程队列
				
				sum = leftTask.join() + rightTask.join();
			}
			
			return sum;
		}
	}
	
	
	/**
	 * 使用普通单线程计算大型任务
	 * 
	 */
	@Test
	public void test02() {
		Instant starttime = Instant.now();
		
		long sum = 0;
		for (long i = 0; i <= 10000000000L; i++) {
			sum += i;
		}
		System.out.println(sum);
		
		Instant endtime = Instant.now();
		System.out.println("普通方法#耗费的时间为(ms)：" + Duration.between(starttime, endtime).toMillis()); // 3205
	}
	
	/**
	 * 使用传统Fork/Join计算大型任务
	 * 
	 */
	@Test
	public void test01() throws Exception {
		Instant starttime = Instant.now();
		
		ForkJoinPool poolExecutor = new ForkJoinPool();
		long result = poolExecutor.invoke(new Task(0L, 10000000000L));
		System.out.println(result);
		
		Instant endtime = Instant.now();
		System.out.println("Fork/Join#耗费的时间为(ms)：" + Duration.between(starttime, endtime).toMillis()); // 1649
	}
	
	/**
	 * 使用JDK1.8 API
	 * 
	 */
	@Test
	public void test03() {
		Instant starttime = Instant.now();
		
		//long result = LongStream.range(0L, 10000000001L).parallel().reduce(0L, Long::sum);
		long result = LongStream.rangeClosed(0, 10000000000L).parallel().reduce(0L, Long::sum);
		System.out.println(result);
		
		Instant endtime = Instant.now();
		System.out.println("JDK1.8 API#耗费的时间为(ms)：" + Duration.between(starttime, endtime).toMillis()); // 1553
	}
}
