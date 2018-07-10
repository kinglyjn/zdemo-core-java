package test06_inner_class;

/**
 * 匿名内部类
 * 1、有时候我为了免去给内部类命名，便倾向于使用匿名内部类，因为它没有名字。
 * 2、匿名内部类不能加访问修饰符。
 * 3、被内部类使用到的外部类的方法的形参（注意city没有被内部类使用到），必须为final。
 *    为什么要定义为final呢？简单理解就是，在拷贝引用时，为了避免引用值发生改变，例如
 *    被外部类的方法修改，而导致内部类得到的值不一致，于是用final来让该引用不可改变。
 *    
 */
public class Outer04 {
	interface Spider {
		String getName();
	}
	
	public Spider getSpiderman(final String name, String city) {
		return new Spider() {
			private String nameStr = name;
			@Override
			public String getName() {
				return nameStr;
			}
		};
	}
}
