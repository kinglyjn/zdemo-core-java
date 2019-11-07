package test14_cglib;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import net.sf.cglib.beans.BeanCopier;

/**
 * 测试 BeanCopier
 * cglib提供的能够从一个bean复制到另一个bean中，而且其还提供了一个转换器，用来在转换的时候对bean的属性进行操作。
 * @author kinglyjn
 * @date Dec 7, 2018
 *
 */
public class Test04_BeanCopier {
	
	static class SampleBean {
		private String value;
		private Map<String, Object> map;
	    public String getValue() {
	        return value;
	    }
	    public void setValue(String value) {
	        this.value = value;
	    }
		public Map<String, Object> getMap() {
			return map;
		}
		public void setMap(Map<String, Object> map) {
			this.map = map;
		}
	}
	static class OtherSampleBean {
		private String value;
		private Map<String, Object> map;
	    public String getValue() {
	        return value;
	    }
	    public void setValue(String value) {
	        this.value = value;
	    }
		public Map<String, Object> getMap() {
			return map;
		}
		public void setMap(Map<String, Object> map) {
			this.map = map;
		}
	}
	
	
	@Test
	public void test01() {
		BeanCopier copier = BeanCopier.create(SampleBean.class, OtherSampleBean.class, false); //设置为true，则使用converter
		SampleBean sampleBean = new SampleBean();
		sampleBean.setValue("hello world");
		sampleBean.setMap(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				this.put("a", "1");
				this.put("b", 2);
				this.put("list", Arrays.asList("1","2","3"));
			}
		});
		
		OtherSampleBean otherSampleBean = new OtherSampleBean();
		copier.copy(sampleBean, otherSampleBean, null); //设置为true，则传入converter指明怎么进行转换
		System.out.println(otherSampleBean.getValue());
		System.out.println(otherSampleBean.getMap());
		
		sampleBean.setMap(new HashMap<String,Object>());
		System.out.println(otherSampleBean.getMap());
		
		/*
		 	hello world
			{a=1, b=2, list=[1, 2, 3]}
			{a=1, b=2, list=[1, 2, 3]}
		 */
	}
}
