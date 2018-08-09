package test00_java8_and_javabin_cmd;

import org.junit.Test;

/**
 * java.util.Optional<T>是一个容器类，代表一个值存在或者不存在。
 * 原来使用null表示一个值不存在，现在Optional可以更好地表达这个概
 * 念，并且可以很好地避免空指针异常。
 * 
 * 常用的方法：
 * Optional.of(T t)：创建一个Optional实例
 * Optional.empty()：创建一个空的Optional实例
 * Optional.ofNullable(T t)：若t不为null则创建Optional实例，否则创建空实例
 * isPresent()：判断是否包含值
 * orElse(T t)：如果调用对象包含值则返回该值，否则返回备用的t
 * orElseGet(Supplier s)：如果调用对象包含值则返回该值，否则返回s获取的值
 * map(Function f)：如果有值则对其处理并返回处理后的Optional，否则返回Optional.empty()
 * flatMap(Function mapper)：与map类似，要求返回的值必须是Optional
 * 
 */
public class Test05_Optional {

	@Test
	public void test01() {
		
	}
}
