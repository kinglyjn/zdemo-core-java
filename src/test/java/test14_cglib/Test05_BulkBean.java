package test14_cglib;

import org.junit.Test;

import net.sf.cglib.beans.BulkBean;

/**
 * 测试 BulkBean
 * 相比于BeanCopier，BulkBean将copy的动作拆分为
 * getPropertyValues 和 setPropertyValues两个
 * 方法，允许自定义处理属性
 * 
 * 使用注意： 
 * 1. 避免每次进行BulkBean.create创建对象，一般将其声明为static的 
 * 2. 应用场景：针对特定属性的get,set操作，一般适用通过xml配置注入和注出
 *    的属性，运行时才确定处理的Source,Target类，只需要关注属性名即可。
 *
 */
public class Test05_BulkBean {
	
	static class SampleBean {
		private String value;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	@Test
	public void test01() {
		BulkBean bulkBean = BulkBean.create(SampleBean.class, 
				new String[]{"getValue"}, new String[]{"setValue"}, 
				new Class[]{String.class});
		
		SampleBean bean = new SampleBean();
		bean.setValue("Hello world");
		
		Object[] propertyValues = bulkBean.getPropertyValues(bean);
		System.out.println(propertyValues.length);
		
		bulkBean.setPropertyValues(bean, new Object[]{"Hello Keyllo"});
		System.out.println(bean.getValue());
	}
	
}
