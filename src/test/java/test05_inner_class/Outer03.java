package test05_inner_class;

/**
 * 嵌套内部类（静态内部类）
 * 1、嵌套内部类，就是修饰为static的内部类。声明为static的内部类，不需要内部类对象和外部类对象
 *    之间的联系，就是说我们可以直接引用outer.inner，即不需要创建外部类，也不需要创建内部类。
 * 2、嵌套类和普通的内部类还有一个区别：普通内部类不能有static数据和static属性，也不能包含嵌套类，
 *    但嵌套类可以。而嵌套类不能声明为private，一般声明为public，方便调用。
 */
public class Outer03 {
	public static String STR = "I am in Outer03.";
	
	static class Inner03 {
		public static String STR = "I am in Inner03.";
		public static void say() {
			System.out.println("in innner: " + STR);
			//Outer03.say();
		}
	}

	public static void say() {
		System.out.println("in outer: " + STR);
	}
}
