package test07_thread_and_juc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * 线程工厂测试
 * @author zhangqingli
 *
 */
public class Test03OfThreadFactory {
	 /*
	 * main
	 * 
	 */
	public static void main(String[] args) {
		MyThreadFactory factory = new MyThreadFactory("MyThreadFactory");
		for (int i = 0; i < 3; i++) {
			Thread t = factory.newThread(new Runnable() {
				@Override
				public void run() {
					// TODO
				}
			});
			t.start();
		}
		System.out.println(factory.getStatus());
	}
}

/**
 * 线程工厂类
 *
 */
class MyThreadFactory implements ThreadFactory {
	private String name;
	private int threadNum;
	private List<String> status;
	
	public MyThreadFactory(String name) {
		this.name = name;
		this.threadNum = 0;
		status = new ArrayList<String>();
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, name+"-Thread-"+threadNum);
		threadNum++;
		status.add(String.format("created thread %d with name %s on %s\n", t.getId(),t.getName(),new Date()));
		return t;
	}
	
	public String getStatus() {
		return status.toString();
	}
}
