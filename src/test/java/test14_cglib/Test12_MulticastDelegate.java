package test14_cglib;

import org.junit.Test;
import net.sf.cglib.reflect.MulticastDelegate;

/**
 * 测试 MulticastDelegate
 * 1. 多重代理和方法代理差不多，都是将代理类方法的调用委托给被代理类。使用前提是需要一个接口，以及一个类实现了该接口
 * 2. 通过这种interface的继承关系，我们能够将接口上方法的调用分散给各个实现类上面去。
 * 3. 多重代理的缺点是接口只能含有一个方法，如果被代理的方法拥有返回值，那么调用代理类的返回值为最后一个添加的被代理类的方法返回值
 *
 */
public class Test12_MulticastDelegate {
	
	public interface DelegatationProvider { // 接口声明必须为public的
		void setValue(String value);
	}
	
	static class SimpleMulticastBean implements DelegatationProvider {
		private String value;
		@Override
	    public void setValue(String value) {
	        this.value = value;
	    }
	    public String getValue() {
	        return value;
	    }
	}
	
	@Test
	public void test01() {
		MulticastDelegate multicastDelegate = MulticastDelegate.create(DelegatationProvider.class);
		SimpleMulticastBean first = new SimpleMulticastBean();
		SimpleMulticastBean second = new SimpleMulticastBean();
		multicastDelegate = multicastDelegate.add(first);
		multicastDelegate = multicastDelegate.add(second);
		
		DelegatationProvider provider = (DelegatationProvider) multicastDelegate;
		provider.setValue("Hello World!");
		System.out.println(first.getValue());
		System.out.println(second.getValue());
	}
}
