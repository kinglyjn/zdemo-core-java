package test03_thread_and_juc;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 本地线程变量测试
 * @author zhangqingli
 *
 */
public class Test02OfThreadLocal {
	/*
	 * main
	 * 
	 */
	public static void main(String[] args) throws InterruptedException {
		Task task = new UnSafeTask();
		//Task task = new SafeTask();
		for (int i = 0; i < 10; i++) {
			new Thread(task).start();
			TimeUnit.SECONDS.sleep(2);
		}
	}
}

/**
 * 任务抽象类
 * 
 */
abstract class Task implements Runnable {}

/**
 * 公共变量不安全的任务类
 * 此类startDate为非本地线程变量
 * 如果你创建一个类对象，实现Runnable接口，然后多个Thread对象使用同样的Runnable对象，全部的线程都共享同样的属性。
 * 这意味着，如果你在一个线程里改变一个属性，全部的线程都会受到这个改变的影响。
 * 
 */
class UnSafeTask extends Task {
	private Date startDate;
	
	@Override
	public void run() {
		startDate = new Date();
		System.out.printf("Thread Started: %s : %s\n", Thread. currentThread().getId(), startDate);
		try {
			TimeUnit.SECONDS.sleep( (int)Math.rint(Math.random()*10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Thread Finished: %s : %s\n", Thread. currentThread().getId(), startDate);
	}
}

/**
 * 公共变量安全的任务类
 * 此类startDate为本地线程变量
 * 有时，你希望程序里的各个线程的属性不会被共享。 Java 并发 API提供了一个很清楚的机制叫本地线程变量。
 * 本地线程类还提供 remove() 方法，删除存储在线程本地变量里的值。
 * 另外，Java 并发 API 包括 InheritableThreadLocal 类提供线程创建线程的值的遗传性 。
 * 如果线程A有一个本地线程变量，然后它创建了另一个线程B，那么线程B将有与A相同的本地线程变量值。 
 * 你可以覆盖 childValue() 方法来初始子线程的本地线程变量的值。 它接收父线程的本地线程变量作为参数。
 * 
 */
class SafeTask extends Task {
	private ThreadLocal<Date> startDate = new ThreadLocal<Date>() {
		@Override
		protected Date initialValue() {
			return new Date();
		}
	};
	
	@Override
	public void run() {
		System.out.printf("Thread Started: %s : %s\n",Thread. currentThread().getId(), startDate.get());
		try {
			TimeUnit.SECONDS.sleep( (int)Math.rint(Math.random()*10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Thread Finished: %s : %s\n",Thread. currentThread().getId(), startDate.get());
	}
}





