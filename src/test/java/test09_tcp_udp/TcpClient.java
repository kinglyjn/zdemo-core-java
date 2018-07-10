package test09_tcp_udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * TCP客户端
 *
 */
public class TcpClient {
	
	 /*
	 * main
	 * 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		Socket s = new Socket("127.0.0.1", 9000);
		
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		System.out.print("请输入：");
		String line = new BufferedReader(new InputStreamReader(System.in)).readLine();
		pw.println(line);
		pw.flush();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String line2 = br.readLine();
		System.out.println(line2);
	}
	
}
