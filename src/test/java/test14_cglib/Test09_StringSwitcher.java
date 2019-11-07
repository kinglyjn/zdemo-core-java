package test14_cglib;

import org.junit.Test;

import net.sf.cglib.util.StringSwitcher;

/**
 * 测试 StringSwitcher
 * 用来模拟一个String到int类型的Map类型。如果在Java7以后的版本中，类似一个switch语句。
 * @author kinglyjn
 * @date Dec 7, 2018
 *
 */
public class Test09_StringSwitcher {
	
	@Test
	public void test01() {
		String[] ss = new String[] {"one", "two", "three"};
		int[] ii = new int[] {1,2,3};
		StringSwitcher switcher = StringSwitcher.create(ss, ii, false);
		System.out.println(switcher.intValue("one"));
	}
}
