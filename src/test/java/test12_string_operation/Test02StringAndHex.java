package test12_string_operation;

import java.util.Locale;

import org.junit.Test;

/**
 * 字符串和16进制数字之间的转化
 * @author zhangqingli
 *
 */
public class Test02StringAndHex {
	
	@Test
	public void testStrToHexs() {
		String str = "好雨知时节";
		
		byte[] bs = str.getBytes();
		StringBuffer sb = new StringBuffer();
		for (byte b : bs) {
			sb.append("\\x" + String.format(Locale.getDefault(), "%x", b));
		}
		
		String result = sb.toString();
		System.out.println(result);
		// \xe5\xa5\xbd\xe9\x9b\xa8\xe7\x9f\xa5\xe6\x97\xb6\xe8\x8a\x82
	}
	
	@Test
	public void test02() {
		String hexs = "\\xe5\\xa5\\xbd\\xe9\\x9b\\xa8\\xe7\\x9f\\xa5\\xe6\\x97\\xb6\\xe8\\x8a\\x82";
		String[] splits = hexs.split("[\\\\x\\\\X]");
		byte[] bs = new byte[splits.length];
		int i = 0;
		for (String s : splits) {
			if (s==null || s.isEmpty()) {
	     		continue;
	      	}
			bs[i] = ((byte)(0xff & Integer.parseInt(s, 16)));
			i++;
		}
		String result = new String(bs);
		System.out.println(result);
		// 好雨知时节                
	}
}
