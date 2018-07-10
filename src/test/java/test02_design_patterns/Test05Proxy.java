package test02_design_patterns;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * 测试 动态代理
 *
 */
public class Test05Proxy {
	
	/**
	 * 计算器功能接口
	 *
	 */
	static interface Caculator {
		int add(int i, int j);
		int minus(int i, int j);
		int multi(int i, int j);
		int div(int i, int j);
	}
	/**
	 * 计算器功能实现类
	 *
	 */
	static class CaculatorImpl implements Caculator {
		@Override
		public int add(int i, int j) {
			return i+j;
		}
		@Override
		public int minus(int i, int j) {
			return i-j;
		}
		@Override
		public int multi(int i, int j) {
			return i*j;
		}
		@Override
		public int div(int i, int j) {
			return i/j;
		}
	}
	
	/**
	 * 测试 Java接口动态代理
	 * 
	 */
	@Test
	public void test01() {
		Caculator caculator = (Caculator) Proxy.newProxyInstance(
				Test05Proxy.class.getClassLoader(), 
				CaculatorImpl.class.getInterfaces(), 
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						// proxy: 正在返回的那个代理对象， 一般情况下invoke方法中都不使用该对象
						// method: 正在被调用的方法
						// args: 正在被调用方法的参数列表
						Caculator target = new CaculatorImpl();
						Object result = null;
						try {
							System.out.println("before（前置通知）: " + method.getName() + " with args " + Arrays.asList(args));
							result = method.invoke(target, args);
							System.out.println("after returning（返回通知）: " + method.getName() + " with result " + result);
						} catch (Exception e) {
							System.out.println("after throwing（异常通知）: " + method.getName() + " occurs exception " + e.toString());
							throw e;
						} finally {
							System.out.println("after（后置通知）: " + method.getName() + " ends");
						}
						return result;
					}
				});
		int result = caculator.div(1, 2);
		System.out.println(result);
	}
	
	
	/**
	 * 测试 CGLIB动态代理
	 * 
	 */
	@Test
	public void test02() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(CaculatorImpl.class);
		enhancer.setCallback(new MethodInterceptor() {
			@Override
			public Object intercept(Object obj, Method method, Object[] args,
					MethodProxy methodProxy) throws Throwable {
				// obj: 动态生成的代理对象
				// method: 实际调用的方法
				// method: 调用方法入参列表
				// proxy: method类的代理类，可以实现委托类对象的方法的调用，常用invokeSuper方法，在拦截方法内可以调用多次
				Object result = null;
				try {
					System.out.println("before（前置通知）: " + method.getName() + " with args " + Arrays.asList(args));
					result = methodProxy.invokeSuper(obj, args);
					System.out.println("after returning（返回通知）: " + method.getName() + " with result " + result);
				} catch (Exception e) {
					System.out.println("after throwing（异常通知）: " + method.getName() + " occurs exception " + e.toString());
					throw e;
				} finally {
					System.out.println("after（后置通知）: " + method.getName() + " ends");
				}
				return result;
			}
		});
		Caculator caculator = (Caculator) enhancer.create();
		int result = caculator.div(1, 2);
		System.out.println(result);
	}
}
