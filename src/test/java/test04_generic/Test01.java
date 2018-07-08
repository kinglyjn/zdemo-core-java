package test04_generic;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * 测试泛型
 * @author zhangqingli
 *
 */
public class Test01 {

	/**
	 * 泛型类01
	 * @author zhangqingli
	 * @param <T>
	 */
	static class Fanxinglei01<T> {
		private T t;

		public T getT() {
			return t;
		}
		public void setT(T t) {
			this.t = t;
		}
		
		@Override
		public String toString() {
			return "Fanxinglei01 [t=" + t + "]";
		}
	}
	
	/**
	 * 含有泛型方法的普通类
	 * @author zhangqingli
	 *
	 */
	static class CustomClass {
		public <T extends Number> double add(T t1, T t2) {
			double result = 0.0;
			result = t1.doubleValue() + t2.doubleValue();
			return result;
		}
	}
	
	/**
	 * 泛型综合测试类
	 * 泛型：编译的过程中进行检查，编译完成后生成的 class文件已将泛型擦除
	 * 获取泛型：getGenericSuperClass、getGenericType
	 * 泛型类型：
	 * 	ParameterizedType：
	 * 		含有泛型的列表的Type，如 BaseDao<Student>、Map<K,V>、List<? extends Person>
	 * 	TypeVariable：
	 * 		本身就是泛型的Type，如 E、T、K、V
	 *	GenericArrayType：
	 *		数组中含有泛型的Type，如 Map<String,Integer>[]、List<? extends Person>[]
	 *  WildcardType：
	 *  	含有通配符的Type，如 List<? extends Person>、List<? super Student>
	 *  
	 */
	static class BaseDao<T> {}
	static class Person {}
	static class Student {}
	@SuppressWarnings("unused")
	static class StudentDao<E extends Person> extends BaseDao<Student> {
		private Map<String, E> map;
		private List<? extends Person> list;
		private E[] vtypeArray;
		private Map<String,Integer>[] mapArray;
		public void wildcardParamsMethod(List<? extends Person> list1, List<? super Student> list2, Class<?> clazz1, Class<Student> claszz2) {}
	}

	
	/**
	 * 测试泛型类
	 * 
	 */
	@Test
	public void test01() {
		Fanxinglei01<String> f1 = new Fanxinglei01<String>();
		f1.setT("嘿嘿");
		System.out.println(f1); // Fanxinglei01 [t=嘿嘿]
	}
	
	/**
	 * 测试泛型方法
	 * 
	 */
	@Test
	public void test02() {
		CustomClass cc = new CustomClass();
		double result = cc.add(1.2, 1);
		System.out.println(result);
	}
	
	
	/**
	 * 测试使用反射获取含有泛型的父类
	 * 
	 */
	@Test
	public void test03() {
		StudentDao<Person> sdao = new StudentDao<Person>();
		Type t1 = sdao.getClass().getGenericSuperclass();
		// 判断StudentDao的父类有没有泛型化（即BaseDao后面有没有<..>）
		if (t1 instanceof ParameterizedType) {
			// 返回这个Type泛型列表对应的实际类型数组
			// 如 Map<String,Person> 这个ParameterizedType返回的是 String和Person类的全限定类名的 Type Array
			Type[] ts2 = ((ParameterizedType) t1).getActualTypeArguments();
			System.out.println(Arrays.asList(ts2)); // [class xxx.Student]
			
			// 返回的是当前这个ParameterizedType的Type
			// 如 Map<String,Person>这个ParameterizedType返回的是 Map类的全限定类名的 Type Array
			Type t3 = ((ParameterizedType) t1).getRawType();
			System.out.println(t3); // class xxx.BaseDao
		}
	}
	
	
	/**
	 * 测试使用反射获取含有泛型的属性
	 * 
	 */
	@Test
	public void test04() {
		StudentDao<Person> sdao = new StudentDao<Person>();
		Field[] fs = sdao.getClass().getDeclaredFields(); //获取自身私有属性或公开的属性
		for (Field f : fs) {
			Type t = f.getGenericType();
			/**
			 * 判断属性本身属于哪一类
			 * 
			 */
			// ParameterizedType：如Map<K,V>、List<? extends T> ...
			if (t instanceof ParameterizedType) {
				Type[] ts2 = ((ParameterizedType) t).getActualTypeArguments();
				for (Type tt : ts2) {
					// 判断属性的泛型列表元素属于哪一类
					if (tt instanceof TypeVariable) { // Map<K,V>、List<T>
						Type[] ts3 = ((TypeVariable<?>) tt).getBounds();
						StringBuffer sb = new StringBuffer();
						for (Type t4 : ts3) {
							sb.append(t4.getTypeName() + ",");
						}
						System.out.println(
								f.getName() 
                                + "：ParameterizedType-泛型列表-泛型类 " 
                                + tt.getTypeName() + "，泛型类的上边界为 " 
                                + sb.substring(0, sb.lastIndexOf(",")) );
					} else { // Map<K,V>、List<T>
						System.out.println(f.getName() 
                                + "：ParameterizedType-泛型列表-普通类 " 
                                + t.getTypeName());
					}
				}
			}
			// map：ParameterizedType-泛型列表-普通类 java.util.Map<java.lang.String, E>
			// map：ParameterizedType-泛型列表-泛型类 E，泛型类的上边界为 test04_generic.Test01$Person
			// list：ParameterizedType-泛型列表-普通类 java.util.List<? extends test04_generic.Test01$Person>
			
			// GenericArrayType：如T[]、List<String>[] ...
			else if (t instanceof GenericArrayType) { 
				Type t2 = ((GenericArrayType) t).getGenericComponentType();
				// 判断数组元素属于哪一类
				if (t2 instanceof ParameterizedType) { // Map<String,Integer>[]、List<String>[]
					Type rawType = ((ParameterizedType) t2).getRawType();
					Type[] actualTypeArguments = ((ParameterizedType) t2).getActualTypeArguments();
					System.out.println(f.getName() + "：GenericArrayType-数组元素及其泛型列表为 " + rawType.getTypeName() + Arrays.toString(actualTypeArguments));
				} else if (t2 instanceof TypeVariable) { // E[]
					String typeName = ((TypeVariable<?>) t2).getName();
					System.out.println(f.getName() + "：TypeVariable-数组元素及其泛型名称为 " + typeName);
				} else {
					System.out.println(f.getName() + "：待论");
				}
			}
			// vtypeArray：TypeVariable-数组元素及其泛型名称为 E
			// mapArray：GenericArrayType-数组元素及其泛型列表为 java.util.Map[class java.lang.String, class java.lang.Integer]
		}
	}
	
	
	/**
	 * 测试使用反射获取含有泛型的方法
	 * 
	 */
	@Test
	public void test05() throws NoSuchMethodException, SecurityException {
		StudentDao<Person> sdao = new StudentDao<Person>();
		Method m = sdao.getClass().getDeclaredMethod("wildcardParamsMethod", new Class[]{List.class, List.class, Class.class, Class.class});
		Type[] ts = m.getGenericParameterTypes(); //参数列表个元素的type
		for (Type t : ts) {
			 /*
			 * 判断Type是否含有泛型列表，如果没有则继续遍历
			 */
			if (!(t instanceof ParameterizedType)) {
				continue;
			}
			 /*
			 * 判断Type泛型列表中是否含有通配符
			 */
			Type[] ts2 = ((ParameterizedType)t).getActualTypeArguments();
			for (Type t2 : ts2) {
				if (!(t2 instanceof WildcardType)) {
					continue;
				}
				WildcardType t3 = (WildcardType) t2;
				Type[] lowerBounds = t3.getLowerBounds();
				Type[] upperBounds = t3.getUpperBounds();
				System.out.println(t.getTypeName() + "：下边界为 " + Arrays.toString(lowerBounds) + "，上边界为 " + Arrays.toString(upperBounds));
			}
		}
		// java.util.List<? extends test04_generic.Test01$Person>：下边界为 []，上边界为 [class test04_generic.Test01$Person]
		// java.util.List<? super test04_generic.Test01$Student>：下边界为 [class test04_generic.Test01$Student]，上边界为 [class java.lang.Object]
		// java.lang.Class<?>：下边界为 []，上边界为 [class java.lang.Object]
	}
	
	
}
