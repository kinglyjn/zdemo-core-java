package test14_cglib;

import java.lang.reflect.Method;
import org.junit.Test;

import net.sf.cglib.proxy.CallbackHelper;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

/**
 * Enhancer测试
 * Enhancer可能是CGLIB中最常用的一个类，和Java1.3动态代理中引入的Proxy类
 * 差不多(如果对Proxy不懂，可以参考这里)。和Proxy不同的是，Enhancer既能够
 * 代理普通的class，也能够代理接口。Enhancer创建一个被代理对象的子类并且拦
 * 截所有的方法调用（包括从Object中继承的toString和hashCode方法）。
 * Enhancer不能够拦截final方法，例如Object.getClass()方法，这是由于
 * Java final方法语义决定的。基于同样的道理，Enhancer也不能对fianl类进行
 * 代理操作。这也是Hibernate为什么不能持久化final class的原因。
 * @author kinglyjn
 * @date Dec 4, 2018
 *
 */
public class Test01_Enhancer {
	
	static class Caculator {
		public int div(int i, int j) {
			return i/j;
		}
		public String haha() {
			return "haha";
		}
	}
	
	
	/**
	 * MethodInterceptor
	 */
	@Test
	public void test01() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Caculator.class);
		enhancer.setCallback(new MethodInterceptor() {
			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				Object result = null;
				try {
					System.out.println("前置通知");
					result = proxy.invokeSuper(obj, args);
					System.out.println("返回通知");
				} catch (Exception ex) {
					System.out.println("异常通知");
				} finally {
					System.out.println("后置通知");
				}
				return result;
			}
		});
		//
		Caculator caculator = (Caculator) enhancer.create();
		int result = caculator.div(1, 0);
		System.out.println("result=" + result);
	}
	
	/**
	 * InvocationHandler
	 */
	@Test
	public void test02() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Caculator.class);
		enhancer.setCallback(new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if (method.getDeclaringClass()!=Object.class && method.getReturnType()==String.class) { //防止循环调用
					return "hello cglib";
				} else {
					throw new RuntimeException("Do not know what to do");
				}
			}
		});
		Caculator caculator = (Caculator) enhancer.create();
		System.out.println(caculator.toString());
	}
	
	/**
	 * CallbackHelper
	 */
	@Test
	public void test03() {
		CallbackHelper callbackHelper = new CallbackHelper(Caculator.class, new Class[0]) {
			@Override
			protected Object getCallback(Method method) {
				if (method.getDeclaringClass()!=Object.class && method.getReturnType()==String.class) {
					return new FixedValue() {
						@Override
						public Object loadObject() throws Exception {
							return "hello world!";
						}
					};
				} else {
					return NoOp.INSTANCE;
				}
			}
		};
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Caculator.class);
		enhancer.setCallbackFilter(callbackHelper);
		enhancer.setCallbacks(callbackHelper.getCallbacks());
		Caculator caculator = (Caculator) enhancer.create();
		System.out.println(caculator.haha());
		System.out.println(caculator.toString());
		System.out.println(caculator.div(4,2));
	}
}
