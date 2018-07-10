package test03_thread_and_juc;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者测试
 * 使用 lock + condition_await_signal 机制实现
 *
 */
public class Test14OfProducerAndCustomerByLock {
	
	public static void main(String[] args) {
		Shop shop = new Shop();
		new Thread(new Producer(shop )).start();
		new Thread(new Producer(shop )).start();
		new Thread(new Consumer(shop)).start();
		new Thread(new Consumer(shop)).start();
	}

	/**
	 * 鸡蛋商品
	 * 
	 */
	static class Egg {
		private Integer id;
		public Egg(Integer id) {
			this.id = id;
		}
		@Override
		public String toString() {
			return Integer.toString(id);
		}
	}
	
	/**
	 * 商店
	 * 
	 */
	static class Shop {
		private static final Integer MAX_COUNT = 1;
		private Deque<Egg> deque = new ArrayDeque<>(1);
		private Lock lock = new ReentrantLock();
		private Condition condition = lock.newCondition();
		
		// 进货+1
		public void stock(Egg egg) {
			lock.lock();
			try {
				while (deque.size() >= MAX_COUNT) { //使用 while 而不是 if 进行判断，可以避免虚假唤醒的问题
					System.out.println("进货已满，queue=" + deque);
					try { condition.await(); } catch(Exception e) {}
				}
				deque.offer(egg);
				System.out.println(Thread.currentThread().getName() + "#stock: " + egg + ", queue=" + deque);
				condition.signal();
			} finally {
				lock.unlock();
			}
		}
		
		// 出货-1
		public void sell() {
			lock.lock();
			try {
				while (deque.size() <= 0) { //使用 while 而不是 if 进行判断，可以避免虚假唤醒的问题
					System.out.println("出货已空，queue=" + deque);
					try { condition.await(); } catch(Exception e) {}
				}
				Egg egg = deque.poll();
				System.out.println(Thread.currentThread().getName() + "#sell: " + egg + ", queue=" + deque);
				condition.signal();
			} finally {
				lock.unlock();
			}
		}
	}
	
	
	/**
	 * 生产者
	 * 
	 */
	static class Producer implements Runnable {
		private Shop shop;
		public Producer(Shop shop) {
			this.shop = shop;
		}
		@Override
		public void run() {
			int i = 1;
			while (true) {
				shop.stock(new Egg(i));
				i++;
				try { Thread.sleep(new Random().nextInt(1000)); } catch(Exception e) {}
			}
		}
	}
	
	
	/**
	 * 消费者
	 * 
	 */
	static class Consumer implements Runnable {
		private Shop shop;
		public Consumer(Shop shop) {
			this.shop = shop;
		}
		@Override
		public void run() {
			while (true) {
				shop.sell();
				try { Thread.sleep(new Random().nextInt(1000)); } catch(Exception e) {}
			}
		}
	}
	
}
