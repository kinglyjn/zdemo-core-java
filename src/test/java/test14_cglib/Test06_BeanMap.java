package test14_cglib;

import java.lang.reflect.Method;
import org.junit.Test;
import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

/**
 * 测试 BeanMap
 * BeanMap类实现了Java Map，将一个bean对象中的所有属性转换为一个String-to-Obejct的Java Map
 * 
 * @author kinglyjn
 * @date Dec 7, 2018
 *
 */
public class Test06_BeanMap {
	
	@Test
	public void test01() throws Exception {
		BeanGenerator generator = new BeanGenerator();
		generator.addProperty("username", String.class);
		generator.addProperty("password", String.class);
		
		Object bean = generator.create();
		Method setUsername = bean.getClass().getMethod("setUsername", String.class);
		Method setPassword = bean.getClass().getMethod("setPassword", String.class);
		setUsername.invoke(bean, "zhangsan");
		setPassword.invoke(bean, "123456");
		
		BeanMap beanMap = BeanMap.create(bean);
		System.out.println(beanMap);
		System.out.println(beanMap.get("username"));
	}
}
