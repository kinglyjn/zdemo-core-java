package test14_cglib;

import java.lang.reflect.Method;
import org.junit.Test;
import net.sf.cglib.beans.BeanGenerator;

/**
 * 测试 BeanGenerator
 * cglib提供的一个操作bean的工具，使用它能够在运行时动态的创建一个bean
 * @author kinglyjn
 * @date Dec 7, 2018
 *
 */
public class Test03_BeanGenerator {
	
	@Test
	public void test01() throws Exception {
		BeanGenerator beanGenerator = new BeanGenerator();
		beanGenerator.addProperty("value", String.class);
		Object myBean = beanGenerator.create();
		
		Method setValue = myBean.getClass().getMethod("setValue", String.class);
		setValue.invoke(myBean, "hello world");
		
		Method getValue = myBean.getClass().getMethod("getValue");
		System.out.println(getValue.invoke(myBean));
	}
}
