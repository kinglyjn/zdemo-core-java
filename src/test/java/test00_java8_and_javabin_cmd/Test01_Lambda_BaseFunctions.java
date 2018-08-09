package test00_java8_and_javabin_cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.junit.Test;

/**
 * Lambda表达式使用的条件：
 * 
 * 注意只有函数接口才能使用lambda来表达，函数式接口就是一个有且仅有一个抽象方法，但是可以有多个非抽象方法
 * 的接口。通常函数接口就是只有一个方法(不包括接口中的默认方法、静态方法、Object类中的方法)的接口，该接口
 * 可以用 @FunctionalInterface 来修饰。lambda表达式会极大简化 接口对于匿名实现类 的开发，使代码更优雅。
 * 
 * 四大核心函数式接口：
 * 消费型接口：Consumer<T> { void accept(T t); }
 * 供给型接口：Supplier<T> { T get(); }
 * 断言型接口：Predicate<T> { boolean test(T t); }
 * 函数型接口：Function<T,R> { R apply(T t); }
 * 
 * lambda语法糖表达式的语法：
 * 上联：左侧为一括号省
 * 下联：右侧为一括号省
 * 横批：能省就省
 *
 */
public class Test01_Lambda_BaseFunctions {
	
	/**
	 * 消费型接口的lambda表达
	 */
	@Test
	public void test01() {
		f01("哈哈哈", (v) -> System.out.println(v) );
	}
	private void f01(String t, Consumer<String> consumer) {
		consumer.accept(t);
	}
	
	/**
	 * 供给型接口的lambda表达
	 * 
	 */
	@Test
	public void test02() {
		List<Integer> list = f02(10, () -> (int)(Math.random()*100) );
		list.forEach(v -> System.out.println(v) );
	}
	private List<Integer> f02(Integer size, Supplier<Integer> supplier) {
		List<Integer> list = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			list.add(supplier.get());
		}
		return list;
	}
	
	/**
	 * 断言型接口的lambda表达
	 * 
	 */
	@Test
	public void test03() {
		List<String> rs = filterStr(Arrays.asList("hello", "world", "aa", "bb"), (v) -> v.length()>2);
		rs.forEach(v -> System.out.println(v) );
		
	}
	private List<String> filterStr(List<String> ss, Predicate<String> predicate) {
		List<String> list = new LinkedList<String>();
		for (String s : ss) {
			if (predicate.test(s)) {
				list.add(s);
			}
		}
		return list;
	}
	
	/**
	 * 函数型接口的lambda表达
	 * 
	 */
	@Test
	public void test04() {
		String result = handleStr("我只要前三个字符", v -> v.substring(0, 3) );
		System.out.println(result);
	}
	private String handleStr(String in, Function<String, String> function) {
		return function.apply(in);
	}
	
	/**
	 * 自定义函数接口的lambda表达
	 * 
	 */
	@Test
	public void test05() {
		flyHaha( ()->System.out.println("我会飞，我来啦！") );
	}
	@FunctionalInterface
	interface Superman {
		void fly();
	}
	private void flyHaha(Superman superman) {
		superman.fly();
	}
}
