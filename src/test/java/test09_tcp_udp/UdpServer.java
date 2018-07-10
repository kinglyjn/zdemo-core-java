package test09_tcp_udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * UdpServer
 *
 */
public class UdpServer {
	public static void main(String[] args) throws Exception {
		//创建socket
		DatagramSocket socket = new DatagramSocket(9000);
		//收数据
		byte[] buf = new byte[100];
		DatagramPacket paper = new DatagramPacket(buf, 0, buf.length);
		socket.receive(paper);
		String str = new String(buf, 0, paper.getLength());
		System.out.println(str);
		
		//发数据
		byte[] data = "hello client".getBytes();
		DatagramPacket packet = new DatagramPacket(data, 0, data.length, paper.getSocketAddress());
		socket.send(packet);
		
		//关闭socket
		socket.close();
	}
}