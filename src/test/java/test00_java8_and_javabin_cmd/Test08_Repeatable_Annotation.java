package test00_java8_and_javabin_cmd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;

/**
 * JDK1.8的一些基本新特性
 * @author zhangqingli
 *
 */
public class Test08_Repeatable_Annotation {
	
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


