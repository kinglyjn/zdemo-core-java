package test06_inner_class;

import org.junit.Test;

import test06_inner_class.Outer01.Inner01;
import test06_inner_class.Outer02.Spider;

/**
 * 内部类测试
 * 
 * 1、内部类是指在一个外部类的内部再定义一个类。类名不需要和文件夹相同。
 * 2、内部类可以是静态static的，也可用public，default，protected和private修饰，
 *    而外部顶级类即类名和文件名相同的只能使用public和default
 * 3、内部类是一个编译时的概念，一旦编译成功，就会成为完全不同的两类。对于一个名为
 *    outer的外部类和其内部定义的名为inner的内部类。编译完成后出现outer.class和
 *    outer$inner.class两类。所以内部类的成员变量/方法名可以和外部类的相同。
 * 4、内部类主要可分为成员内部类、局部内部类、嵌套内部类(静态内部类)、匿名内部类等
 * 
 * 
 */
public class Test01 {
	
	/**
	 * 测试 成员内部类
	 * 
	 */
	@Test
	public void test01() {
		Outer01 outer = new Outer01();
		
		Inner01 inner1 = outer.new Inner01();
		inner1.say("hello world!");
		
		Inner01 inner2 = outer.getInner();
		inner2.say("你好，世界！");
	}
	
	/**
	 * 测试 局部内部类
	 * 
	 */
	@Test
	public void test02() {
		Spider spider = new Outer02().getSpiderman();
		spider.fly();
	}
	
	/**
	 * 测试 嵌套内部类（静态内部类） 
	 * 
	 */
	@Test
	public void test03() {
		System.out.println(Outer03.Inner03.STR); // I am in Inner03.
		Outer03.Inner03.say(); // in innner: I am in Inner03.
	}
	
	/**
	 * 测试 匿名内部类
	 * 
	 */
	@Test
	public void test04() {
		Outer04 outer = new Outer04();
		test06_inner_class.Outer04.Spider spiderman = outer.getSpiderman("Parker", "New York");
		System.out.println(spiderman.getName()); // Parker
	}
	
	
}
