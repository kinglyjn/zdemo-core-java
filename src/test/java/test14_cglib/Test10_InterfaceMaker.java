package test14_cglib;

import org.junit.Test;
import org.objectweb.asm.Type;

import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.InterfaceMaker;

/**
 * 测试 InterfaceMaker
 * 正如名字所言，Interface Maker用来创建一个新的Interface
 * @author kinglyjn
 * @date Dec 7, 2018
 *
 */
public class Test10_InterfaceMaker {
	
	/**
	 * 下面的Interface Maker创建的接口中只含有一个方法，签名为double foo(int)。
	 * Interface Maker与上面介绍的其他类不同，它依赖ASM中的Type类型。由于接口仅
	 * 仅只用做在编译时期进行类型检查，因此在一个运行的应用中动态的创建接口没有什么
	 * 作用。但是InterfaceMaker可以用来自动生成代码，为以后的开发做准备。
	 */
	@Test
	public void test01() {
		Signature signature = new Signature("foo", Type.DOUBLE_TYPE, new Type[]{Type.INT_TYPE});
		InterfaceMaker interfaceMaker = new InterfaceMaker();
		interfaceMaker.add(signature, new Type[0]);
		Class<?> iface = interfaceMaker.create();
		
		System.out.println(iface.getMethods().length); // 1
		System.out.println(iface.getMethods()[0].getName()); // foo
		System.out.println(iface.getMethods()[0].getReturnType()); // double
	}
}
