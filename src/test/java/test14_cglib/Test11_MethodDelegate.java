package test14_cglib;

import org.junit.Test;

import net.sf.cglib.reflect.MethodDelegate;

/**
 * 测试 MethodDelegate
 * MethodDelegate主要用来对方法进行代理
 * 
 * @author kinglyjn
 * @date Dec 7, 2018
 *
 */
public class Test11_MethodDelegate {
	static class SampleBean {
		private String value;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	interface BeanDelegate {
	    String getValueFromDelegate();
	}
	
	
	/**
	 * 关于Method.create的参数说明： 
	 * 1. 第二个参数为即将被代理的方法 
	 * 2. 第一个参数必须是一个无参数构造的bean。因此MethodDelegate.create并不是你想象的那么有用 
	 * 3. 第三个参数为只含有一个方法的接口。当这个接口中的方法被调用的时候，将会调用第一个参数所指向bean的第二个参数方法
	 * 
	 * 关于Method.create的缺点： 
	 * 1. 为每一个代理类创建了一个新的类，这样可能会占用大量的永久代堆内存 
	 * 2. 你不能代理需要参数的方法 
	 * 3. 如果你定义的接口中的方法需要参数，那么代理将不会工作，并且也不会抛出异常；如果你的接口中方法需要其他的返回类型，那么将抛出IllegalArgumentException
	 * 
	 */
	@Test
	public void test01() throws Exception {
		SampleBean bean = new SampleBean();
		bean.setValue("hello world");
		BeanDelegate delegate = (BeanDelegate) MethodDelegate.create(bean, "getValue", BeanDelegate.class);
		String v = delegate.getValueFromDelegate();
		System.out.println(v);
	}
}
