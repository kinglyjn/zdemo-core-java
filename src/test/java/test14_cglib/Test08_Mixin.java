package test14_cglib;

import org.junit.Test;
import net.sf.cglib.proxy.Mixin;

/**
 * 测试 Mixin
 * Mixin能够让我们将多个对象整合到一个对象中去，前提是这些对象必须是接口的实现。
 * Mixin类比较尴尬，因为他要求Minix的类（例如MixinInterface）实现一些接口。
 * 既然被Minix的类已经实现了相应的接口，那么我就直接可以通过纯Java的方式实现，没有必要使用Minix类。
 * @author kinglyjn
 * @date Dec 7, 2018
 *
 */
public class Test08_Mixin {
	
	interface Interface1 {
		String first();
	}
	interface Interface2 {
		String second();
	}
	static class Class1 implements Interface1 {
		@Override
		public String first() {
			return "first";
		}
	}
	static class Class2 implements Interface2 {
		@Override
		public String second() {
			return "second";
		}
	}
	interface MixinInterface extends Interface1,Interface2 {}
	
	
	@Test
	public void test01() {
		MixinInterface mixinDelegate = (MixinInterface) Mixin.create(new Class[]{Interface1.class, Interface2.class, MixinInterface.class}, 
				new Object[]{new Class1(), new Class2()});
		String first = mixinDelegate.first();
		String second = mixinDelegate.second();
		System.out.println(first + " " + second);
	}
	
}
