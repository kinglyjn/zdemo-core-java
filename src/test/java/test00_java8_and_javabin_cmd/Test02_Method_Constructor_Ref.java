package test00_java8_and_javabin_cmd;

import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.junit.Test;

/**
 * 方法引用：
 * 若lambda体中的逻辑已经有方法实现了，我们可以使用“方法引用”，
 * 可以理解为 方法引用其实是lambda表达式的另外一种表现形式。
 * 方法引用主要有以下三种语法格式：
 * a) 对象::实例方法名  普通对象的实例方法
 * b) 类::静态方法名    普通类的静态方法
 * c) 类::实例方法名    若lambda参数列表中的第一个参数是实例方法的调用者，第二个参数是实例方法的参数时，可以使用class::method
 * 
 * 构造器引用：
 * Classname::new
 * 
 * 数组引用：
 * ElementType[]::new
 * 
 * 注意上述无论是哪种引用方式，lambda参数列表和返回值类型，
 * 都必须要与函数式接口中抽象方法的函数列表和返回值保持一致！
 * 
 * 
 */
public class Test02_Method_Constructor_Ref {

	/**
	 * 对象::实例方法名
	 * 
	 */
	static class Student1 {
		private Integer id;
		private String name;
		public Student1(Integer id, String name) {
			this.id = id;
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public Integer getId() {
			return id;
		}
	}
	@Test
	public void test01() {
		////
		Consumer<String> consumer1 = (v) -> System.out.println(v);
		consumer1.accept("哈哈");
		
		Consumer<String> consumer2 = System.out::println;
		consumer2.accept("嘿嘿");
		
		
		////
		Student1 student = new Student1(1, "张三");
		Supplier<String> supplier1 = () -> student.getName();
		System.out.println("student name: " + supplier1.get());
		
		Supplier<Integer> supplier2 = student::getId;
		System.out.println("student id: " + supplier2.get());
	}
	
	
	
	/**
	 * 类::静态方法名
	 * 
	 */
	@Test
	public void test02() {
		////
		Comparator<Integer> comparator1 = (x,y) -> Integer.compare(x, y);
		System.out.println(comparator1.compare(1, 2));
		
		Comparator<Integer> comparator2 = Integer::compare;
		System.out.println(comparator2.compare(1, 2));
	}
	
	
	
	/**
	 * 类::实例方法名
	 * 
	 */
	@Test
	public void test03() {
		BiPredicate<String,String> predicate1 = (x,y) -> x.equals(y);
		System.out.println(predicate1.test("xxx", "xxx"));
		
		BiPredicate<String,String> predicate2 = String::equals;
		System.out.println(predicate2.test("yyy", "yyy"));
	}
	
	
	
	/**
	 * 构造器引用：
	 * Classname::new
	 * 
	 */
	static class Student2 {
		Integer id;
		public Student2() {}
		public Student2(Integer id) {
			this.id = id;
		}
	}
	@Test
	public void test04() {
		////
		Supplier<Student2> supplier1 = () -> new Student2();
		System.out.println(supplier1.get());
		Supplier<Student2> supplier2 = Student2::new;
		System.out.println(supplier2.get());
		
		////
		Function<Integer, Student2> function1 = (id) -> new Student2(id);
		System.out.println(function1.apply(1).id);
		Function<Integer, Student2> function2 = Student2::new;
		System.out.println(function2.apply(2).getClass());
	}
	
	
	
	/**
	 * 数组引用：
	 * ElementType[]:new
	 * 
	 */
	@Test
	public void test05() {
		////
		Function<Integer, String[]> function1 = (length) -> new String[length];
		String[] ss1 = function1.apply(10);
		System.out.println(ss1.length);
		
		Function<Integer, String[]> function2 = String[]::new;
		String[] ss2 = function2.apply(20);
		System.out.println(ss2.length);
	}
}
