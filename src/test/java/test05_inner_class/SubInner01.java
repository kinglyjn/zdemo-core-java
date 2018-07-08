package test05_inner_class;


/**
 * 继承普通内部类Inner01的子类
 * 内部类的继承，是指内部类被继承，普通类 extents 内部类。而这时候代码上要有点特别处理，具体看以下例子
 *
 */
public class SubInner01 extends Outer01.Inner01 {
	
	//无参构造函数A()是不能通过编译的，一定要加上形参
	SubInner01(Outer01 outer01) {
		outer01.super();
	}
	
	public static void main(String[] args) {
		Outer01 outer01 = new Outer01();
		Outer01.Inner01 inner01 = new SubInner01(outer01);
		inner01.say("hello world!");
	}
}
