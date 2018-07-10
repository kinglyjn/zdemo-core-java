package test09_tcp_udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * URL编程测试
 *
 */
public class URLTest {
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		URLConnection conn = new URL("http://www.sina.com.cn").openConnection();
		InputStream is = conn.getInputStream();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line=br.readLine()) != null) {
			System.out.println(line);
		}
		br.close();
	}
}
