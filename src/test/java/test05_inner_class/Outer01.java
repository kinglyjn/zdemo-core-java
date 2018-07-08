package test05_inner_class;

/**
 * 成员内部类
 * 
 * 1、成员内部类，就是作为外部类的成员，可以直接使用外部类的所有成员和方法，即使是private的。
 *    同时外部类要访问内部类的所有成员变量/方法，则需要通过内部类的对象来获取；
 * 2、要注意的是，成员内部类不能含有static的变量和方法。因为成员内部类需要先创建了外部类，
 *    才能创建它自己的，了解这一点，就可以明白更多事情，在此省略更多的细节了；
 * 3、在成员内部类要引用外部类对象时，使用outer.this来表示外部类对象；
 * 4、需要创建内部类对象，可以使用Outer.Inner obj = outerobj.new Inner()   
 *  
 */
public class Outer01 {
	public class Inner01 {
		public void say(String str) {
			System.out.println(str);
		}
	}
	
	//个人推荐使用getxxx()来获取成员内部类，尤其是该内部类的构造函数无参数时 
	public Inner01 getInner() {
		return new Inner01();
	}
}
