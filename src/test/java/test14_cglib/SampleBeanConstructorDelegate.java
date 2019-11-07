package test14_cglib;

/**
 * 测试 SampleBeanConstructorDelegate
 * @author kinglyjn
 * @date Dec 10, 2018
 *
 */
public class SampleBeanConstructorDelegate {
	public interface SampleBenanConstructorDelegtor {
		Object newInstance(String value);
	}
	
	
}
