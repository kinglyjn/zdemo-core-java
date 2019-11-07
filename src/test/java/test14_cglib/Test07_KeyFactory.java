package test14_cglib;

import org.junit.Test;
import org.springframework.cglib.core.KeyFactory;

/**
 * 
 * 测试 KeyFactory
 * keyFactory类用来动态生成接口的实例，接口需要只包含一个newInstance方法，返回一个Object。
 * keyFactory为构造出来的实例动态生成了Object.equals和Object.hashCode方法，能够确保相同
 * 的参数构造出的实例为单例的。
 * 
 * @author kinglyjn
 * @date Dec 7, 2018
 *
 */
public class Test07_KeyFactory {
	
	interface SampleKeyFactory {
		Object newInstance(String first, int second);
	}
	
	@Test
	public void test01() {
		SampleKeyFactory keyFactory = (SampleKeyFactory) KeyFactory.create(SampleKeyFactory.class);
		Object key1 = keyFactory.newInstance("foo", 42); //测试参数相同，结果是否相等
		Object key2 = keyFactory.newInstance("foo", 42);
		System.out.println(key1==key2);         //false
		System.out.println(key1.equals(key2));  //true
		System.out.println(key1.getClass());    //xxx.Test07_KeyFactory$SampleKeyFactory$$KeyFactoryByCGLIB$$28e00fff
		System.out.println(key2.getClass());    //xxx.Test07_KeyFactory$SampleKeyFactory$$KeyFactoryByCGLIB$$28e00fff
	}
	
}
