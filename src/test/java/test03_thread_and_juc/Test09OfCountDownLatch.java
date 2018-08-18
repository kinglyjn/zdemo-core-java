package test03_thread_and_juc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

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
	
	
	/**
	 * CountDownLatch 测试
	 * 
	 */
	@Test
	public void test01() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(3);
		for (int i = 0; i < 3; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {Thread.sleep(1000);} catch(Exception e) {}
					System.out.println(Thread.currentThread().getName() + "执行了。。。");
					latch.countDown();
				}
			}).start();
		}
		
		latch.await();
		System.out.println("都执行了，哈哈。。。");
		Thread.sleep(10000);
	}
	
	
	/**
	 * CyclicBarrier 测试
	 * 
	 */
	@Test
	public void test02() throws InterruptedException {
		CyclicBarrier barrier = new CyclicBarrier(3); ////
		for (int i = 0; i < 3; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + "开始执行。。。");
					try {
						System.out.println(Thread.currentThread().getName() + "执行中。。。");
						
						barrier.await(); ////
						System.out.println(Thread.currentThread().getName() + "已经结束。。。");
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		Thread.sleep(10000);
	}
	
	
	/**
	 * Semaphore 测试
	 * 3个茅坑，10个人排队蹲坑
	 * 
	 */
	@Test
	public void test03() throws InterruptedException {
		Semaphore semaphore = new Semaphore(3); // 允许同时运行的线程数为3（资源信号量为3）
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (semaphore.availablePermits() > 0) { //判断资源信号量是否大于0
						System.out.println(Thread.currentThread().getName() + ": 有茅坑，蹲起来！");
					} else {
						System.out.println(Thread.currentThread().getName() + ": TMD，茅坑满了，等会吧！");
					}
					
					try {
						semaphore.acquire(); // 等着，直到抢占到可用资源
						Thread.sleep(1000);
						System.out.println(Thread.currentThread().getName() + ": 终于抢到茅坑啦，蹲起来！");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally {
						System.out.println(Thread.currentThread().getName() + ": 蹲完爽！");
						semaphore.release(); //释放用完的资源（归还信号量）
					}
				}
			}).start();
		}
		Thread.sleep(30000);
	}
	
	
	/**
	 * ConcurrentLinkedDeque [extends Queue implements Deque] & ConcurrentLinkedQueue [extends AbstractQueue implements Queue]
	 * 1. 它是基于链表的无界线程安全队列
	 * 2. 线程安全性通过CAS无锁机制实现，性能通常由于 BlockingQueue。
	 * 3. 该队列不允许存放和消费空null元素。
	 * 
	 * 主要方法：
	 * offer/add  新增队列元素，如果队列已满offer返回false而add抛异常（对于ConcurrentLinkedDeque这两个方法作用相同）
	 * poll/remove  弹出队列元素，如果队列为空poll返回null而remove抛出异常
	 * peek/element  查询队列的头部元素而不会将头元素消费掉，如果队列为空peek返回null而element抛异常
	 * 
	 */
	@Test
	public void test04() {
		ConcurrentLinkedDeque<String> deque = new ConcurrentLinkedDeque<>();
		deque.offer("a");
		deque.offer("b");
		deque.offer("c");
		System.out.println(deque.size()); //3
		
		System.out.println(deque.poll()); //a
		System.out.println(deque.poll()); //b
		System.out.println(deque.poll()); //c
		System.out.println(deque.poll()); //null
	}
	
	
	/**
	 * BlockingQueue [extends Queue]
	 * 阻塞队列BlockingQueue是一个支持两个附加操作的队列接口，这两个附加操作是：
	 * 1. 在队列为空时，消费队列元素的线程会等待队列变为非空
	 * 2. 在队列为满时，生产队列元素的线程会等待队列变为非满
	 * BlockingQueue通常用于生产者和消费者模型中
	 * 
	 */
	@Test
	public void test05() throws InterruptedException {
		BlockingQueue<String> deque = new LinkedBlockingQueue<>(2); //使用链表实现的先后层安全的有界阻塞队列
		deque.offer("a");
		deque.offer("b", 2, TimeUnit.SECONDS);
		deque.offer("c", 2, TimeUnit.SECONDS);
		System.out.println(deque.size()); //2
		
		System.out.println(deque.poll()); //a
		System.out.println(deque.poll()); //b
		System.out.println(deque.poll()); //null
		System.out.println(deque.poll()); //null
		System.out.println(deque.poll(10, TimeUnit.SECONDS)); //null
		deque.offer("d");
		System.out.println(deque.poll()); //d
	}
}
