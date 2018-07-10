package test09_tcp_udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * UdpClient
 *
 */
public class UdpClient {
	
	public static void main(String[] args) throws Exception {
		DatagramSocket socket = new DatagramSocket();
		String str = "hello server";
		byte[] data = str.getBytes();
		DatagramPacket packet = new DatagramPacket(data, 0, data.length, new InetSocketAddress("127.0.0.1", 9000));
		socket.send(packet);
		
		byte[] buf = new byte[100];
		DatagramPacket paper = new DatagramPacket(buf, 0, buf.length);
		socket.receive(paper);
		String smg = new String(buf, 0, paper.getLength());
		System.out.println(smg);
		
		socket.close();
	}
}
