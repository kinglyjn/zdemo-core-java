package test07_thread_and_juc;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * 线程 Synchronized 和 数据安全测试
 * @author zhangqingli
 *
 */
public class Test04OfSynchronized {
	
	public void sleep(int secounds) {
		try {
			TimeUnit.SECONDS.sleep(secounds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试01 安全共享数居（安全同步数据）
	 * 只有操作公共资源的原子操作，才是需要加锁的，如果不是就没有加锁的必要。
	 * 
	 */
	static class RunnableImpl01 implements Runnable {
		private int count = 10;
		
		@Override
		public void run() {
			while (count > 0) {
				synchronized (RunnableImpl01.class) {
					if (count > 0) { // 双层判断
						System.out.println(Thread.currentThread().getName() + ": " + count);
						count--;
					}
				}
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Test
	public void test01() {
		RunnableImpl01 r01 = new RunnableImpl01();
		for (int i = 0; i < 3; i++) {
			new Thread(r01).start();
		}
		sleep(100);
	}
	
	
	
	/**
	 * 测试02 多个对象多个锁
	 * 两个线程分别访问同一个类的两个不同实例的相同名称的同步方法，效果是以异步方式执行的。
	 * 
	 */
	static class LoginTask implements Runnable {
		private LoginServlet loginServlet;
		private String username;
		
		public LoginTask(LoginServlet loginServlet, String username) {
			this.loginServlet = loginServlet;
			this.username = username;
		}

		@Override
		public void run() {
			loginServlet.login(username);
		}
	}
	static class LoginServlet {
		public synchronized void login(String username) {
			try {
				if ("a".equals(username)) {
					System.out.println("login success!");
					Thread.sleep(2000);
				} else {
					System.out.println("login fail!");
				}
				System.out.println("username=" + username);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Test
	public void test021() { // 创建了两个锁对象，所以运行的结果是异步的
		LoginServlet loginServlet01 = new LoginServlet();
		LoginServlet loginServlet02 = new LoginServlet();
		new Thread(new LoginTask(loginServlet01, "a")).start();
		new Thread(new LoginTask(loginServlet02, "b")).start();
		sleep(100);
		// login success!
		// login fail!
		// username=b
		// username=a
	}
	@Test
	public void test022() { //如果把loginServlet的login方法改成synchronized static类型的，则运行结果是同步的
		LoginServlet loginServlet = new LoginServlet();
		new Thread(new LoginTask(loginServlet, "a")).start();
		new Thread(new LoginTask(loginServlet, "b")).start();
		sleep(100);
		// login success!
		// username=a
		// login fail!
		// username=b
	}
	
	
	
	
	/**
	 * 测试03 
	 * 可重入锁的概念是：自己可以再次获取自己的内部锁，比如线程A获得了某个锁对象，
	 * 此时它的这个对象锁还没有释放，当它再次想获取这个对象锁的时候是可以获取的。
	 * 
	 * 父子可重入锁
	 * 当存在父子类继承关系时，子类完全可以通过可重入锁调用父类的同步方法。
	 * 下面的就是一个父子可重入锁的演示。
	 * 
	 */
	static class A {
		public synchronized void aaa() {
			System.out.println(Thread.currentThread().getName() + ": a.aaa");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	static class B extends A {
		public synchronized void bbb() {
			System.out.println(Thread.currentThread().getName() + ": b.bbb");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			super.aaa();
		}
	}
	static class RunnableImpl03 implements Runnable {
		private B b;
		public RunnableImpl03(B b) {
			this.b = b;
		}
		@Override
		public void run() {
			b.bbb();
		}
	}
	@Test
	public void test03() {
		B b = new B();
		new Thread(new RunnableImpl03(b)).start();
		new Thread(new RunnableImpl03(b)).start();
		sleep(10);
		// Thread-0: b.bbb
		// Thread-0: a.aaa
		// Thread-1: b.bbb
		// Thread-1: a.aaa
	}
	
	
	
	
}













