package test03_thread_and_juc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 测试 自定义线程池
 *
 */
public class Test16CustomThreadPool {
	
	/**
	 * 连接池接口
	 * 客户端可以通过execute方法将job提交到线程池执行，而客户端自身不用等待job的执行完成
	 * 这里的工作者线程代表着一个重复执行job的线程，而每个由客户端提交的job都将进入到一个工
	 * 作者队列中，等待工作者线程的处理
	 * 
	 */
	static interface ThreadPool<Job extends Runnable> {
		// 执行一个job，这个job需要实现Runnbale
		void execute(Job job); 
		// 关闭线程池
		void shutdown();
		// 增加工作者线程
		void addWorkers(int num);
		// 减少工作线程
		void removeWorkers(int num);
		// 得到正在等待执行的任务总量
		int getJobSize();
	}
	
	/**
	 * 自定义线程池
	 * 从线程池的默认实现我们可以看到，当客户端调用execute(Job)方法时，会不断向任务列表jobs中添加job，
	 * 而每个工作线程会不断从jobs上取出一个job进行执行，当jobs为空时，工作者线程进入等待状态。添加一个
	 * job后，对工作队列jobs调用了其notify方法，而不是notifyAll，因为能够确保有工作者线程被唤醒，这时
	 * 使用notify竟会比使用notifyAll获得更小的开销（避免将等待队列中的线程全部移动到阻塞队列中）。
	 * 
	 * 线程池的本质 就是使用了一个线程安全的工作队列连接工作者线程和客户端线程。客户端线程将任务放入工作
	 * 队列后边返回，而工作者线程则不断从工作队列上取出任务并执行。当工作队列为空的时候，所有的工作线程均
	 * 【等待在工作队列上】，当客户端提交了一个任务之后，会通知任意一个工作线程，随着大量的任务被提交，更
	 * 多的工作线程将被唤醒。
	 *
	 */
	static class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {
		//线程池最大限制数
		private static final int MAX_WORKER_NUMBERS = 10;
		//线程池最小的数量
		private static final int MIN_WORKER_NUMBERS = 5;	
		//线程池默认的数量
		private static final int DEFAULT_WORKER_NUMBERS = 5;
		//实际工作线程的数量
		private int workerNum = DEFAULT_WORKER_NUMBERS;
		//这是一个工作列表，将会向里面插入工作
		private final LinkedList<Job> jobs = new LinkedList<>();
		//工作者列表
		private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());
		//线程编号生成器
		private AtomicLong threadNum = new AtomicLong();
		
		// 工作线程
		class Worker implements Runnable {
			private volatile boolean running = true;
			//
			@Override
			public void run() {
				while (running) {
					Job job = null;
					synchronized (jobs) {
						while (jobs.isEmpty()) {
							try {
								jobs.wait();
							} catch (InterruptedException e) {
								//感知到外部对workerThread的中断操作，返回
								Thread.currentThread().interrupt();
								return;
							}
						}
						// 取出一个job
						job = jobs.pollFirst();
						if (job != null) {
							try {
								job.run(); //注意是run而不是start
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			//
			public void shutdown() {
				this.running = false;
			}
		}
		
		private void initializeWorkers(int num) {
			for (int i = 0; i < num; i++) {
				Worker worker = new Worker();
				workers.add(worker);
				new Thread(worker, "ThreadWorker-" + threadNum.incrementAndGet()).start();
			}
		}
		public DefaultThreadPool() {
			initializeWorkers(DEFAULT_WORKER_NUMBERS);
		}
		public DefaultThreadPool(int num) {
			workerNum = num>MAX_WORKER_NUMBERS ? MAX_WORKER_NUMBERS : 
							num<MIN_WORKER_NUMBERS ? MIN_WORKER_NUMBERS : num;
			initializeWorkers(workerNum);
		}
		
		
		@Override
		public void execute(Job job) {
			if (job != null) {
				//添加一个工作，然后进行通知
				synchronized (jobs) {
					jobs.offerLast(job);
					jobs.notify();
				}
			}
		}

		@Override
		public void shutdown() {
			for (Worker worker : workers) {
				worker.shutdown();
			}
		}

		@Override
		public void addWorkers(int num) {
			synchronized (jobs) {
				//限制新增的worker数量不能超过最大值
				if (this.workerNum+num > MAX_WORKER_NUMBERS) {
					num = MAX_WORKER_NUMBERS-this.workerNum;
				}
				initializeWorkers(num);
				this.workerNum += num;
			}
		}

		@Override
		public void removeWorkers(int num) {
			synchronized (jobs) {
				if (num > this.workerNum) {
					throw new IllegalArgumentException("beyond workerNum.");
				}
				//按照给定的数量停止worker
				int count = 0;
				while (count < num) {
					Worker worker = workers.get(count);
					if (workers.remove(worker)) {
						worker.shutdown();
						count++;
					}
				}
				this.workerNum -= count;
			}
		}

		@Override
		public int getJobSize() {
			return jobs.size();
		}
	}
	
	
	 /*
	 * main
	 * 
	 */
	static class TicketWindow implements Runnable {
		private int num = 20;
		
		@Override
		public void run() {
			while (num > 0) {
				System.out.println(Thread.currentThread().getName() + ": " + num);
				num--;
				try {Thread.sleep(100);} catch (Exception e){}
			}
		}
	}
	public static void main(String[] args) {
		DefaultThreadPool<TicketWindow> pool = new DefaultThreadPool<>();
		TicketWindow job1 = new TicketWindow();
		TicketWindow job2 = new TicketWindow();
		TicketWindow job3 = new TicketWindow();
		pool.execute(job1);
		pool.execute(job2);
		pool.execute(job3);
	}
}
