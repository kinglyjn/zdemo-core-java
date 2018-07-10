package test05_reflection;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.junit.Test;

/**
 * 测试反射的基本使用
 * @author zhangqingli
 *
 */
public class Test01 {

	/**
	 * 类对象测试
	 * 
	 */
	static class Stu {
		private String name;
		private int age;
		
		public Stu(String name, int age) {
			this.name = name;
			this.age = age;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		
		@SuppressWarnings("unused")
		private String toString1() { //注意这是私有方法
			return "Stu [name=" + name + ", age=" + age + "]";
		}
	}
	@Test
	public void test01() throws Exception {
		// 创建类对象
		Class<?> c = Class.forName("test05_reflection.Test01$Stu");
		Constructor<?> constructor = c.getConstructor(String.class, int.class);
		Stu student = (Stu) constructor.newInstance("zhangsan", 23);
		
		//利用反射调用对象的私有方法
		Method toString = c.getDeclaredMethod("toString1", new Class[]{});
		toString.setAccessible(true);
		String str = (String) toString.invoke(student, new Object[]{});
		System.out.println(str);
		
		//利用反射修改私有属性
		Field age = c.getDeclaredField("age");
		age.setAccessible(true); //
		age.set(student, 25);
		System.out.println(student.getAge()); //25
	}
	
	
	
	
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	static @interface MyAnnotation {
		String value();
	}
	
	static interface MyInterface extends Serializable {}
	
	static class Animal {
		public boolean sex = true;
	}
	static class Person<T> extends Animal {
		protected T name;
		public int age;

		public T getName() {
			return name;
		}
		public void setName(T name) {
			this.name = name;
		}
	}
	@MyAnnotation("t_student")
	static class Student<T> extends Person<String> implements Comparable<Student<T>>, MyInterface {
		private static final long serialVersionUID = 1L;
		
		public Student() {
			System.out.println("无参构造器");
		}

		@Override
		public int compareTo(Student<T> o) {
			return 0;
		}
		
		@MyAnnotation("method_show")
		public void show() {
			System.out.println(name);
		}
		
		@MyAnnotation("method_dispaly")
		protected void dispaly(String name) throws Exception {
			System.out.println(name);
		}
		
		public static final void haha() {
			System.out.println("haha");
		}
		
		class Address {
			private String province;
			private String city;
			public String getProvince() {
				return province;
			}
			public void setProvince(String province) {
				this.province = province;
			}
			public String getCity() {
				return city;
			}
			public void setCity(String city) {
				this.city = city;
			}
		}
	}
	
	
	
	/**
	 * 测试 获取运行时类的属性
	 * 
	 */
	@Test
	public void test02() {
		Student<Integer> student = new Student<Integer>(); 
		Class<?> c = student.getClass();
		//无参构造器
		
		Field[] fields = c.getFields();
		for (Field field : fields) {
			System.out.println(field); 
			//public int test05_reflection.Test01$Person.age
			//public boolean test05_reflection.Test01$Animal.sex
		} 
		
		Field[] fields2 = c.getDeclaredFields(); //获取本类中所有属性
		for (Field field : fields2) {
			System.out.println(field);
		}
		//private static final long test05_reflection.Test01$Student.serialVersionUID
	}
	
	
	/**
	 * 获取属性的权限修饰符、类型、属性名、属性值
	 * 
	 */
	@Test
	public void test03() throws Exception {
		Student<Integer> student = new Student<Integer>(); 
		Class<?> c = student.getClass();
		Field serialVersionUIDField = c.getDeclaredField("serialVersionUID");
		
		String modifier = Modifier.toString(serialVersionUIDField.getModifiers());
		System.out.println(modifier); //private static final
		
		Class<?> type = serialVersionUIDField.getType();
		System.out.println(type); //long
		System.out.println(type.isPrimitive()); //true 判断type是否为基本类型
		
		String name = serialVersionUIDField.getName();
		System.out.println(name); //serialVersionUID
		
		if (Modifier.isStatic(serialVersionUIDField.getModifiers())) { //判断是否是静态方法
			serialVersionUIDField.setAccessible(true);
			
			long serialVersionUID = 0;
			if (type.isAssignableFrom(long.class)) { //判断type是否是long.class的子类或父类
				serialVersionUID = (long) serialVersionUIDField.get(Student.class);
				System.out.println("类型转换成功！");
				System.out.println(serialVersionUID); //1
			}
		}
	}
	
	
	/**
	 * 获取运行时方法
	 */
	@Test
	public void test04() {
		Student<Integer> student = new Student<Integer>(); 
		Class<?> c = student.getClass();
		Method[] methods = c.getMethods(); //获取本类及所有父类的公开方法
		for (Method method : methods) {
			System.out.println(method);
		}
		/*
		public int test05_reflection.Test01$Student.compareTo(java.lang.Object)
		public int test05_reflection.Test01$Student.compareTo(test05_reflection.Test01$Student)
		public void test05_reflection.Test01$Student.show()
		public static final void test05_reflection.Test01$Student.haha()
		public java.lang.Object test05_reflection.Test01$Person.getName()
		public void test05_reflection.Test01$Person.setName(java.lang.Object)
		public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException
		public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException
		public final void java.lang.Object.wait() throws java.lang.InterruptedException
		public boolean java.lang.Object.equals(java.lang.Object)
		public java.lang.String java.lang.Object.toString()
		public native int java.lang.Object.hashCode()
		public final native java.lang.Class java.lang.Object.getClass()
		public final native void java.lang.Object.notify()
		public final native void java.lang.Object.notifyAll()
		*/
		
		Method[] methods2 = c.getDeclaredMethods(); //获本类中所有方法
		for (Method method : methods2) {
			System.out.println(method);
		}
		/*
		public int test05_reflection.Test01$Student.compareTo(java.lang.Object)
		public int test05_reflection.Test01$Student.compareTo(test05_reflection.Test01$Student)
		public void test05_reflection.Test01$Student.show()
		protected void test05_reflection.Test01$Student.dispaly(java.lang.String) throws java.lang.Exception
		public static final void test05_reflection.Test01$Student.haha()
		*/
	}
	
	
	/**
	 * 获取方法的权限修饰符、方法名称、形参列表、返回值类型、异常、注解、以及调用方法
	 * 
	 */
	@Test
	public void test05() throws Exception {
		Class<?> c = Student.class;
		Method method = c.getDeclaredMethod("dispaly", new Class[] {String.class});
		
		String modifier = Modifier.toString(method.getModifiers());
		System.out.println(modifier); //protected
		
		String name = method.getName();
		System.out.println(name); //dispaly
		
		Class<?>[] parameterTypes = method.getParameterTypes();
		for (Class<?> class1 : parameterTypes) {
			System.out.println(class1); //class java.lang.String
		}
		
		Class<?> returnType = method.getReturnType();
		System.out.println(returnType); //void
		System.out.println(returnType.isAssignableFrom(void.class)); //true
		
		Class<?>[] exceptionTypes = method.getExceptionTypes();
		for (Class<?> class1 : exceptionTypes) {
			System.out.println(class1); //class java.lang.Exception
		}
		
		Annotation[] annotations = method.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof MyAnnotation) {
				System.out.println(annotation); //@test05_reflection.Test01$MyAnnotation(value=method_dispaly)
				String value = ((MyAnnotation) annotation).value();
				System.out.println(value); 	//method_dispaly
			}
		}
		
		Student<String> student = new Student<String>();
		method.invoke(student, "张三"); //张三
		
		Method method2 = c.getDeclaredMethod("haha");
		Object obj = method2.invoke(Student.class); //haha
		System.out.println(obj); //null
	}
	
	
	/**
	 * 构造器方法
	 */
	@Test
	public void test06() throws Exception {
		Class<?> c = Student.class;
		Constructor<?> constructor = c.getConstructor();
		Student<?> student = (Student<?>) constructor.newInstance();
		student.dispaly("李四");
	}
	
	
	/**
	 * 获取父类、父类的泛型
	 */
	@Test
	public void test07() {
		Class<?> c = Student.class;
		Class<?> superclass = c.getSuperclass();
		System.out.println(superclass); //获取父类 class test05_reflection.Test01$Person
		
		Type genericSuperclass = c.getGenericSuperclass();
        //获取父类并且带带泛型  test05_reflection.Test01.test05_reflection.Test01$Person<java.lang.String>
		System.out.println(genericSuperclass); 
		
		Type genericSuperclass2 = c.getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) genericSuperclass2;
		Type[] actualTypeArguments = paramType.getActualTypeArguments();
		for (Type type : actualTypeArguments) {
			System.out.println((Class<?>)type); //获取父类的泛型  class java.lang.String
		}
	}
	
	
	/**
	 * 获取接口 及 接口的泛型
	 */
	@Test
	public void test08() {
		Class<?> c = Student.class;
		Class<?>[] interfaces = c.getInterfaces();
		for (Class<?> class1 : interfaces) {
            // 获取本类接口  
			System.out.println(class1);
			// interface java.lang.Comparable   interface test05_reflection.Test01$MyInterface
		}
		
		Type[] genericInterfaces = c.getGenericInterfaces();
		for (Type type : genericInterfaces) {
            // 获取本类接口并带泛型   
			System.out.println(type); 
			// java.lang.Comparable<test05_reflection.Test01.test05_reflection.Test01$Student<T>> interface test05_reflection.Test01$MyInterface
		}
		
		Type[] genericSuperclass2 = c.getGenericInterfaces();
		ParameterizedType paramType = (ParameterizedType) genericSuperclass2[0];
		Type[] actualTypeArguments = paramType.getActualTypeArguments();
		for (Type type : actualTypeArguments) {
			System.out.println(type); 
			// 获取本类接口的泛型  test05_reflection.Test01.test05_reflection.Test01$Student<T>
		}
	}
	
	
	/**
	 * 获取包
	 * 
	 */
	@Test
	public void test09() {
		Class<?> c = Student.class;
		Package package1 = c.getPackage();
		System.out.println(package1.getName()); //test05_reflection
	}
	
	/**
	 * 获取注解
	 * 
	 */
	@Test
	public void test10() {
		Class<?> c = Student.class;
		Annotation[] annotations = c.getAnnotations();
		for (Annotation annotation : annotations) {
			System.out.println(annotation); 
			//@test05_reflection.Test01$MyAnnotation(value=t_student)
		}
	}
	
}
