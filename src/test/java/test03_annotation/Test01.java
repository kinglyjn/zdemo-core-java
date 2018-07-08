package test03_annotation;

import java.lang.annotation.Annotation;
import org.junit.Test;

import test03_annotation.FruitColor.Color;

public class Test01 {
	@MyTable
	static class Student {}
	@MyTable2
	static class Student2 extends Student {
		@FruitName("苹果")
		private String f1;
		@FruitColor(Color.RED)
		private String f2;
		@FruitProvider(id=1001, name="xxxName", address="xxxAddress")
		private String f3;
	}
	
	
	/**
	 * 测试 元注解 @Inherited 
	 * 
	 */
	@Test
	public void test01() {
		Annotation[] annotations = Student2.class.getAnnotations();
		for (Annotation annotation : annotations) {
			System.out.println(annotation);
		}
		
		// @test03_annotation.MyTable(tableName=className)
		// @test03_annotation.MyTable2(tableName=className)
		
		// 如果将 @MyTable 注解的 @Inherited 元注解注释掉，结果为：
		// @test03_annotation.MyTable2(tableName=className)
	}
	
	/**
	 * 测试 根据反射获取注解数据
	 * 
	 */
	@Test
	public void test02() {
		FruitInfoUtil.getFruitInfo(Student2.class);
	}
	
}
