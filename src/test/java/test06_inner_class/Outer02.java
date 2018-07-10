package test06_inner_class;

/**
 * 
 * 局部内部类
 * 定义在方法和作用域内，它也像别的类一样进行编译，但只是作用域不同而已，
 * 只在该方法或条件的作用域内才能使用，退出这些作用域后无法引用的。
 *
 */
public class Outer02 {
	
	interface Spider {
		void fly();
	}
	
	public Spider getSpiderman() {
		//定义局部内部类
		class Spiderman implements Spider {
			@Override
			public void fly() {
				System.out.println("我是蜘蛛侠，我会飞！");
			}
		}
		return new Spiderman();
	}
}
