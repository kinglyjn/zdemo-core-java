package test09_tcp_udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP服务端
 * @author zhangqingli
 *
 */
public class TcpServer {
	
	 /*
	 * main
	 * 
	 */
	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		ServerSocket ss = new ServerSocket(9000);
		while (true) {
			Socket s = ss.accept();
			new IoThread(s).start();
		}
	}
	
	/**
	 * IoThread
	 * 
	 */
	static class IoThread extends Thread {
		private Socket s;
		public IoThread(Socket s) {
			this.s = s;
		}
		@Override
		public void run() {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String line = br.readLine();
				
				PrintWriter pw = new PrintWriter(s.getOutputStream());
				pw.println("from server: " + line);
				pw.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
