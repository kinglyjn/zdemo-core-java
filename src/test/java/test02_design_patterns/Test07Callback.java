package test02_design_patterns;

import org.junit.Test;

/**
 * 测试 回调函数
 * 
 * 
 */
public class Test07Callback {

	/**
	 * 测试 同步回调
	 * 
	 */
	static class RoomMate1 {
		public String getAnswer(String question, Handler1 handler) {
			return handler.handle(question);
		}
	}
	static interface Handler1 {
		String handle(String question);
	}
	@Test
	public void test01() {
		RoomMate1 rm = new RoomMate1();
		String answer = rm.getAnswer("1+1=?", new Handler1() {
			@Override
			public String handle(String question) {
				return "1+1=2 哈哈";
			}
		});
		System.out.println(answer);
	}
	
	
	
	/**
	 * 测试 异步回调
	 * 我们给好室友设置了个较难的问题，希望好室友能多有点时间思考
	 * 新开的线程纯粹用来等待好室友来写完作业。由于在好室友类中设置了3秒的等待时间，所以可以看到goHome方法将先执行。
	 * 这意味着该学生在告知好室友做作业后，就可以做自己的事情去了，不需要同步阻塞去等待结果。一旦好室友完成作业，写
	 * 入作业本，该线程也就结束运行了。
	 * 
	 */
	static class RoomMate2 {
		public void getAnswer(String question, Handler2 handler) {
			handler.handle(question);
		}
	}
	static interface Handler2 {
		void handle(String question);
	}
	@Test
	public void test02() throws InterruptedException {
		System.out.println("好同学，问你个问题，慢慢做，我先去踢球啦~");
		
		RoomMate2 rm = new RoomMate2();
		new Thread(new Runnable() {
			@Override
			public void run() {
				rm.getAnswer("1+1=?", new Handler2() {
					@Override
					public void handle(String question) {
						System.out.println(Thread.currentThread().getName() + ": 让我想想。。。");
						try { Thread.sleep(3000); } catch (Exception e) {}
						System.out.println("哦，1+1 答案是 2 !");
					}
				});
			}
		}).start();
		Thread.sleep(10000);
	}
}
