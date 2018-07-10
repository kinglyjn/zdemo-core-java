package test09_tcp_udp;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 基于线程池技术的简单web服务器
 * 
 * 目前的浏览器都支持多线程访问，比如在请求一个html页面的时候，页面中包含的图片资源、
 * 样式资源会被浏览器并发地发起访问获取，这样用户就不会遇到一直等到一个图片完全下载完
 * 才能查看文字内容的尴尬情况。
 * 
 * 如果服务器是单线程的，多线程的浏览器也没有用武之地，因为服务器还是一个请求一个请求
 * 的顺序处理。因此，大部分web服务器都是支持并发访问的。常用的web服务器是tomcat、
 * jetty，其在处理请求的过程中都使用了线程池技术。
 * 
 * 下面通过上述线程池实现来构造一个简单的web服务器，这个web服务器用来处理http请求，
 * 目前只能处理简单的文本和jpg图片内容。这个服务器使用main线程不断接收客户端socket
 * 连接，将连接以及请求提交给线程池处理，这使得web服务器能够同时处理多个客户端请求。
 *
 */
public class SimpleHttpServer {
	// 处理HttpRequest的线程池
	private ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 20, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
	// SimpleHttpServer的根路径
	private String basePath = System.getProperty("user.dir") + "/src/test/java/test09_tcp_udp";
	// 服务器 SocketServer
	private ServerSocket serverSocket;
	// 服务器监听端口
	private int port = 8080;
	
	/**
	 * 请求处理线程
	 *
	 */
	class HttpRequestHandler implements Runnable {
		private Socket socket;
		public HttpRequestHandler(Socket socket) {
			this.socket = socket;
		}
		@Override
		public void run() {
			BufferedReader br = null;
			PrintWriter pw = null;
			FileInputStream fin = null;
			BufferedReader br2 = null;
			try {
				//由相对路径计算得出绝对路径
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String header = br.readLine();
				String filepath = basePath + header.split(" ")[1];
				
				// 打印http请求
				System.out.println(header);
				String line = null;
				while(true) {
					line = br.readLine();
					System.out.println(line);
					if (line==null || "".equals(line.trim())) {
						break;
					}
				}
				
				// 如果请求jpg或jpeg资源
				pw = new PrintWriter(socket.getOutputStream());
				if (filepath.endsWith("jpg") || filepath.endsWith("jpeg")) {
					pw.println("HTTP/1.1 200 OK");
					pw.println("Content-Type: image/jpeg");
					pw.println("");
					pw.flush();
					
					fin = new FileInputStream(filepath);
					OutputStream os = socket.getOutputStream();
					byte[] bs = new byte[1024];
					int len = 0;
					while ((len=fin.read(bs)) != -1) {
						os.write(bs, 0, len);
					}
					os.flush();
				} else {
                    pw.println("HTTP/1.1 200 OK");
                    pw.println("Content-Type: text/html; charset=UTF-8");
                    pw.println("");
                    br2 = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
                    while ((line = br2.readLine()) != null) {
                    	pw.println(line);
                    }
                    pw.flush();
				}
			} catch (Exception e) {
				pw.println("HTTP/1.1 500");
				pw.println("");
				pw.flush();
			} finally {
				close(br, pw, fin, br2, socket);
			}
		}
		
		public void close(Closeable...closeables) {
			if (closeables != null) {
				for (Closeable closeable : closeables) {
					if (closeable==null) {
						continue;
					}
					try {
						closeable.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	// 启动
	public void start() throws Exception {
		this.serverSocket = new ServerSocket(this.port);
		Socket socket = null;
		while ((socket=serverSocket.accept()) != null) {
			 // 接收一个客户端Socket，生成一个HttpRequestHandler，放入线程池中执行
			poolExecutor.execute(new HttpRequestHandler(socket));
		}
		serverSocket.close();
	}

	
	/**
	 * main
	 * 
	 */
	public static void main(String[] args) throws Exception {
		SimpleHttpServer server = new SimpleHttpServer();
		server.start();
	}
}
