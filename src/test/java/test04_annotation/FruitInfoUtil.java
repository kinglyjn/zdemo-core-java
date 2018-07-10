package test04_annotation;

import java.lang.reflect.Field;

/**
 * 根据反射获取 Fruit 注解数据工具类
 * @author zhangqingli
 *
 */
public class FruitInfoUtil {
	
	public static void getFruitInfo(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
        	if(field.isAnnotationPresent(FruitName.class)){
        		FruitName fruitName = (FruitName) field.getAnnotation(FruitName.class);
        		System.out.println("水果名称为：" + fruitName.value());
        	} else if (field.isAnnotationPresent(FruitColor.class)) {
        		FruitColor fruitColor= (FruitColor) field.getAnnotation(FruitColor.class);
        		System.out.println("水果颜色为：" + fruitColor.value().name());
        	} else if (field.isAnnotationPresent(FruitProvider.class)) {
        		 FruitProvider fruitProvider= (FruitProvider) field.getAnnotation(FruitProvider.class);
        		 System.out.println("供货商编号：" + fruitProvider.id() + "，供应商名称：" + fruitProvider.name() + "，供应商地址：" + fruitProvider.address());
        	}
		}
	}
}
