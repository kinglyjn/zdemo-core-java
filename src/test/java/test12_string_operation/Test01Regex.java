package test12_string_operation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * 正则表达式
 * @author zhangqingli
 *
 */
public class Test01Regex {
	
	/**
	 * 测试匹配
	 * 
	 */
	@Test
	public void test01() {
		String str = "asdasdasdsadasdasdasssssss";
		System.out.println(str.matches(".*ssssss.*"));
	}
	
	/**
	 * 测试替换
	 * 
	 */
	@Test
	public void test02() {
		String str = "窗前明月光，asdasdad asdad";
		str = str.replaceAll("[\\u4e00-\\u9fa5]", "c");
		System.out.println(str);
	}
	
	/**
	 * 测试萃取
	 * 
	 */
	@Test
	public void test03() {
		String str = "100,2000, asd9090 898窗前明月光，asdasdad asdad";
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
        	System.out.println(matcher.group());
        }
	}
	
	/**
	 * 测试分割
	 * 
	 */
	@Test
	public void test04() {
		String str = "100,2000, asd9090 898窗前明月光，asdasdad asdad";
        String[] strs = str.split("\\d+");
        for (int i = 0; i < strs.length; i++) {
            System.out.println(strs[i]);
        }
	}
	
}
