package test03_thread_and_juc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单的数据库连接池示例
 *
 */
public class Test18OfJdbcConnectionPool {
	
	/**
	 * 连接池生成器（模拟）
	 * 
	 */
	static class ConnectionDriver {
		public static Connection createConnection() {
			return (Connection) Proxy.newProxyInstance(ConnectionDriver.class.getClassLoader(), 
					new Class[]{Connection.class}, 
					new InvocationHandler() {
						@Override
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							if (method.getName().equals("commit")) {
								TimeUnit.MILLISECONDS.sleep(100);
							}
							return null;
						}
					});
		}
	}
	
	/**
	 * 连接池
	 * 
	 */
	static class ConnectionPool {
		private LinkedList<Connection> pool = new LinkedList<>();
		
		public ConnectionPool(int initailSize) {
			if (initailSize > 0) {
				for (int i = 0; i < initailSize; i++) {
					pool.addLast(ConnectionDriver.createConnection());
				}
			}
		}
		
		// 释放连接
		public void releaseConnection(Connection connection) {
			if (connection != null) {
				synchronized (pool) {
					pool.offerLast(connection); //连接释放后需要进行通知，这样其他消费者能够感知到连接池中已经归还了一个连接
					pool.notifyAll();
				}
			}
		}
		
		// 在millis时间内无法获取连接，将会返回null
		public Connection fetchConnection(long millis) throws InterruptedException {
			synchronized (pool) {
				if (millis < 0) { //超时
					while (pool.isEmpty()) {
						pool.wait();
					}
					return pool.pollFirst();
				} else {
					long start = System.currentTimeMillis();
					long remaining = millis;
					while (remaining>0 && pool.isEmpty()) {
						pool.wait(remaining);
						remaining = remaining-(System.currentTimeMillis()-start);
					}
					Connection result = null;
					if (!pool.isEmpty()) {
						result = pool.pollFirst();
					}
					return result;
				}
			}
		}
	}
	
	/**
	 * 
	 */
	static class ConnectionRunner implements Runnable {
		int count;
		AtomicInteger got;
		AtomicInteger notGot;
		public ConnectionRunner(int count, AtomicInteger got, AtomicInteger notGot) {
			this.count = count;
			this.got = got;
			this.notGot = notGot;
		}
		
		@Override
		public void run() {
			try { startCountDownLatch.await(); } catch(Exception e) {}
			while (count > 0) {
				try {
					//分别统计获取到的次数和未获取到的次数got、notGot
					Connection connection = pool.fetchConnection(1000);
					if (connection != null) {
						try {
							connection.createStatement();
							connection.commit();
						} finally {
							pool.releaseConnection(connection);
							got.incrementAndGet();
						}
					} else {
						notGot.incrementAndGet();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					count--;
				}
			}
			endCountDownLatch.countDown();
		}
	}
	
	
	//线程池
	private static final ConnectionPool pool = new ConnectionPool(3);
	//保证所有的ConnectionRunner线程能够同时开始
	private static CountDownLatch startCountDownLatch = new CountDownLatch(1); 
	//保证所有的ConnectionRunner线程都运行结束后才继续执行
	private static CountDownLatch endCountDownLatch;
	 /*
	 * main
	 * 
	 */
	public static void main(String[] args) throws InterruptedException {
		int threadCount = 10;	//定义线程的数量，可以修改进行观察
		int count = 20;			//定义每个线程获取连接的次数
		AtomicInteger got = new AtomicInteger();
		AtomicInteger notGot = new AtomicInteger();
		endCountDownLatch = new CountDownLatch(threadCount);
		
		for (int i = 0; i < threadCount; i++) {
			new Thread(new ConnectionRunner(count, got, notGot), "ConnectionRunner"+i).start();
		}
		startCountDownLatch.countDown();
		
		endCountDownLatch.await();
		System.out.println("total invoke: " + (threadCount*count));
		System.out.println("got connection: " + got);
		System.out.println("not got connection: " + notGot);
	}
	
	/*
	 实验结果：
	 上述代码使用了CountDownLatch来确保ConnectionRunner线程能够同时开始执行，并且在全部结束之后，
	 才能使main线程从等待状态中返回。实验结果表明，在资源一定的情况下（连接池中的3个连接），随着客户
	 端线程的逐步增加。客户端出现超时无法获取的比率不断升高。虽然客户端线程在这种超时获取的模式下无法
	 正常获取连接，但它能够保证客户端线程不会一致挂在连接获取的操作上，而是“按时”返回，并告知客户端连
	 接获取出现问题，是系统的一种自我保护机制。数据库连接池的设计也可以复用到其他资源的获取场景，针对
	 昂贵的资源的获取均应该加以超时限制。
	 
	 */
}
