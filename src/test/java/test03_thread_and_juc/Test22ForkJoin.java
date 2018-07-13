package test03_thread_and_juc;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

import org.junit.Test;

/**
 * 测试 Fork/Join框架
 *
 * 计算0L-50000000000L 的和
 * 
 */
public class Test22ForkJoin {
	private static final long START = 0;
	private static final long END = 10000000000L;
	
	/**
	 * 使用 Fork/Join 计算大型任务
	 * 
	 */
	public static void main(String[] args) throws Exception {
		Instant starttime = Instant.now();
		
		ForkJoinPool poolExecutor = new ForkJoinPool();
		long result = poolExecutor.invoke(new Task(START, END));
		System.out.println(result);
		
		Instant endtime = Instant.now();
		System.out.println("Fork/Join#耗费的时间为(ms)：" + Duration.between(starttime, endtime).toMillis()); // 1649
	}
	
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
	 * 使用普通方法计算大型任务
	 * 
	 */
	@Test
	public void test01() {
		Instant starttime = Instant.now();
		
		long sum = 0;
		for (long i = START; i <= END; i++) {
			sum += i;
		}
		System.out.println(sum);
		
		Instant endtime = Instant.now();
		System.out.println("普通方法#耗费的时间为(ms)：" + Duration.between(starttime, endtime).toMillis()); // 3205
	}
	
	
	/**
	 * 使用JDK1.8 API
	 * 
	 */
	@Test
	public void test02() {
		Instant starttime = Instant.now();
		
		long result = LongStream.range(START, END).parallel().reduce(0L, Long::sum);
		System.out.println(result);
		
		Instant endtime = Instant.now();
		System.out.println("JDK1.8 API#耗费的时间为(ms)：" + Duration.between(starttime, endtime).toMillis()); // 1553
	}
}
