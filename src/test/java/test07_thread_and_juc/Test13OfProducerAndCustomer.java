package test07_thread_and_juc;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/**
 * 生产者消费者测试
 *
 */
public class Test13OfProducerAndCustomer {
	
	public static void main(String[] args) {
		Shop shop = new Shop();
		new Thread(new Producer(shop )).start();
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
		private static final Integer MAX_COUNT = 3;
		private Deque<Egg> deque = new ArrayDeque<>(4);
		
		// 进货+1
		public synchronized void stock(Egg egg) {
			while (deque.size() >= MAX_COUNT) {
				System.out.println("进货已满，queue=" + deque);
				try { this.wait(); } catch (Exception e) {}
			}
			deque.offer(egg);
			System.out.println(Thread.currentThread().getName() + "#stock: " + egg + ", queue=" + deque);
			this.notify();
		}
		
		// 出货-1
		public synchronized void sell() {
			while (deque.size() <= 0) {
				System.out.println("出货已空，queue=" + deque);
				try { this.wait(); } catch (Exception e) {}
			}
			Egg egg = deque.poll();
			System.out.println(Thread.currentThread().getName() + "#sell: " + egg + ", queue=" + deque);
			this.notify();
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
