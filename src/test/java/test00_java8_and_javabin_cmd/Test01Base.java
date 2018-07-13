package test00_java8_and_javabin_cmd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import org.junit.Test;

/**
 * JDK1.8的一些基本新特性
 * @author zhangqingli
 *
 */
public class Test01Base {
	
	
	/**
	 * Lambda表达式（闭包）
	 * 
	 */
	@Test
	public void test01() {
		// Lambda表达式的基本使用，类型是由编译器推理得出
		Arrays.asList("a","b","c").forEach((String e) -> System.out.print(e));
		System.out.println();
		
		
		// Lambda表达式可以引用类成员和局部变量（会将这些变量隐式得转换成final的
		String separator = "#";
		Arrays.asList("a","b","c").forEach(e -> {
			System.out.print(separator+e);
		});
		System.out.println();
		
		
		// Lambda表达式有返回值，返回值的类型也由编译器推理得出。
		// 如果Lambda表达式中的语句块只有一行，则可以不用使用return语句
		List<String> list = Arrays.asList("b","a","c");
		list.sort((e1, e2) -> e1.compareTo(e2));
		System.out.println(list);
		
		List<String> list2 = Arrays.asList("2","3","1");
		list2.sort((e1, e2) -> {
			return e1.compareTo(e2);
		});
		System.out.println(list2);
	}
	
	
	
	/**
	 * 接口的默认方法和静态方法
	 * 
	 */
	static interface Flyer {
		// 在接口中定义其默认实现
		default void say(String somewords) {
			System.out.println("Hello, I can fly! " + somewords);
		}
		
		// 在接口中定义静态方法
		static <T extends Flyer> T createInstance(Class<T> type) throws Exception {
			return type.newInstance();
		}
		static SuperMan cerateSuperMan(Supplier<SuperMan> supplier) {
			return supplier.get();
		}
	}
	static class SuperMan implements Flyer {}
	
	@Test
	public void test02() throws Exception { 
		SuperMan superMan1 = Flyer.createInstance(SuperMan.class);
		superMan1.say("哈哈");
		
		// Supplier< T>接口没有入参，返回一个T类型的对象，类似工厂方法
		// Supplier<SuperMan> supplier = ()->new SuperMan();
		// man->man.say() 等价于 Superman::say   ()->new SuperMan() 等价于 SuperMan::new
		SuperMan superMan2 = Flyer.cerateSuperMan(SuperMan::new);
		superMan2.say("嘿嘿");
		
		Supplier<SuperMan> supplier = ()->new SuperMan(); // 这里的 ()->new SuperMan() 等价于 SuperMan::new
		supplier.get().say("呵呵");
	}
	
	
	
	/**
	 * 方法的引用
	 * 方法引用使得开发者可以直接引用现存的方法、Java类的构造方法或者实例对象。
	 * 方法引用和Lambda表达式配合使用，使得java类的构造方法看起来紧凑而简洁，没有很多复杂的模板代码。
	 * 
	 */
	static class Car {
		public static Car createCar(final Supplier<Car> supplier) {
			return supplier.get();
		}
		
		public static void printCurrentCar(final Car car) {
			System.out.println("当前车为 " + car.toString());
		}
		
		public void repairCurrentCar() {
			System.out.println("当前被修的车为" + this);
		}
		
		public void trackCurrentCar(final Car car) {
			System.out.println("当前被追踪的车为" + car);
		}
	}
	
	@Test
	public void test03() {
		// 第一种方法引用的类型是构造器引用，语法是Class::new，或者更一般的形式：Class<T>::new。注意：这个构造器没有参数
		final Car car1 = Car.createCar(Car::new);
		final Car car2 = Car.createCar(Car::new);
		final List<Car> cars = Arrays.asList(car1,car2);
		System.out.println(cars);
		System.out.println("----------------");
		
		// 第二种方法引用的类型是静态方法引用，语法是Class::static_method。注意：这个方法接受一个Car类型的参数
		cars.forEach(Car::printCurrentCar);
		cars.forEach(c->Car.printCurrentCar(c));
		System.out.println("----------------");
		
		// 第三种方法引用的类型是某个类的成员方法的引用，语法是Class::method，注意，这个方法没有定义入参
		cars.forEach(Car::repairCurrentCar);
		cars.forEach(c->c.repairCurrentCar());
		System.out.println("----------------");
		
		// 第四种方法引用的类型是某个实例对象的成员方法的引用，语法是instance::method。注意：这个方法接受一个Car类型的参数
		final Car police = Car.createCar(Car::new);
		cars.forEach(police::trackCurrentCar);
		cars.forEach(c->c.trackCurrentCar(c));
	}
	
	
	
	/**
	 * 可重复注解
	 * 自从Java 5中引入注解以来，这个特性开始变得非常流行，并在各个框架和项目中被广泛使用。
	 * 不过，注解有一个很大的限制是：在同一个地方不能多次使用同一个注解。Java 8打破了这个
	 * 限制，引入了重复注解的概念，允许在同一个地方多次使用同一个注解。
	 * 在Java 8中使用@Repeatable注解定义重复注解，实际上，这并不是语言层面的改进，而是编
	 * 译器做的一个trick，底层的技术仍然相同。
	 * 
	 * 这里的Filter类使用@Repeatable(Filters.class)注解修饰，而Filters是存放Filter
	 * 注解的容器，编译器尽量对开发者屏蔽这些细节。这样，Filterable接口可以用两个Filter
	 * 注解注释（这里并没有提到任何关于Filters的信息）。
	 * 另外，反射API提供了一个新的方法：getAnnotationsByType()，可以返回某个类型的重复注解
	 * 
	 */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	static @interface Filters {
		Filter[] value();
	}
	@Target( ElementType.TYPE )
    @Retention( RetentionPolicy.RUNTIME )
	@Repeatable(Filters.class)
	static @interface Filter {
		String value();
	}
	@Filter("filter1")
	@Filter("filter2")
	static interface Filterable {
		// ...
	}
	@Test
	public void test04() {
		// 获取某个类型的重复注解
		Filter[] filters = Filterable.class.getAnnotationsByType(Filter.class);
		for (Filter filter : filters) {
			System.out.println(filter.value()); //filter1 filter2
		}
		
		// 由于Filterable接口使用了两个相同的@Filter注解，相当于使用Filters注解，故此处得到的值为null
		System.out.println("--------------");
		Filter filter = Filterable.class.getAnnotation(Filter.class);
		System.out.println(filter); //null
		
		// 返回值为 @xxx$Filters(value=...
		System.out.println("--------------");
		Filters fs = Filterable.class.getAnnotation(Filters.class);
		System.out.println(fs);
	}
	
}
















