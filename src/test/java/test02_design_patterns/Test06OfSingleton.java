package test02_design_patterns;

/**
 * 单例模式测试
 * @author zhangqingli
 *
 */
public class Test06OfSingleton {
	private volatile static Test06OfSingleton instance; 
	private Test06OfSingleton(){}
	public static Test06OfSingleton getInstance() {
		if (instance==null) {
			synchronized (Test06OfSingleton.class) {
				if (instance==null) {
					instance = new Test06OfSingleton();
				}
			}
		}
		return instance;
	}
	
	public static void main(String[] args) {
		Test06OfSingleton instance1 = Test06OfSingleton.getInstance();
		Test06OfSingleton instance2 = Test06OfSingleton.getInstance();
		System.out.println(instance1==instance2); // true
	}
}
