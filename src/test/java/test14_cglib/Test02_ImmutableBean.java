package test14_cglib;

import org.junit.Test;
import net.sf.cglib.beans.ImmutableBean;

/**
 * 
 * ImmutableBean测试
 * ImmutableBean允许创建一个原来对象的包装类，这个包装类是不可变的，
 * 任何改变底层对象的包装类操作都会抛出IllegalStateException。
 * 但是我们可以通过直接操作底层对象来改变包装类对象。这有点类似于Guava中的不可变视图
 * @author kinglyjn
 * @date Dec 7, 2018
 *
 */
public class Test02_ImmutableBean {
	
	static class SampleBean {
		private String value;
		public SampleBean() {}
	    public SampleBean(String value) {
	        this.value = value;
	    }
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void test01() {
		SampleBean bean = new SampleBean();
		bean.setValue("hello world");
		SampleBean immutableBean = (SampleBean) ImmutableBean.create(bean);
		System.out.println(immutableBean.getValue());
		bean.setValue("hello world, again");
		System.out.println(immutableBean.getValue());
		immutableBean.setValue("hello wordl, again");
	}
	
}
