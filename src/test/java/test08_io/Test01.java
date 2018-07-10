package test08_io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import org.junit.Test;

/**
 * 测试File类
 * @author zhangqingli
 *
 */
public class Test01 {
	
	/**
	 * 测试 FileInputStream / FileOutputStream
	 * 
	 */
	@Test
	public void test01() {
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			String filename = System.getProperty("user.dir") + "/src/test/java/test08_io/test.txt";
			String filename2 = filename + ".out";
			is = new FileInputStream(filename);
			os = new FileOutputStream(filename2);
			
			// core
			byte[] bs = new byte[8];
			int len = 0;
			while ((len=is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 测试 DataInputStream / DataOutputStream
	 * 
	 */
	@Test
	public void test02() {
		DataOutputStream os = null;
		DataInputStream is = null;
		try {
			String filename = System.getProperty("user.dir") + "/src/test/java/test08_io/test.txt.out";
			
			os = new DataOutputStream(new FileOutputStream(filename));
			os.writeDouble(3.14);
			os.writeInt(12);
			os.flush();
			
			is = new DataInputStream(new FileInputStream(filename));
			double pi = is.readDouble();
			int i = is.readInt();
			System.out.println(pi + " " + i);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 测试 ObjectInputSream / ObjectOutputStream 和对象序列化
	 * 
	 */
	static class Student implements Serializable {
		private static final long serialVersionUID = 1L;
		private String name;
		private transient int age;
		public Student(String name, int age) {
			this.name = name;
			this.age = age;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		@Override
		public String toString() {
			return name + "," + age;
		}
	}
	@Test
	public void test03() {
		ObjectOutputStream os = null;
		ObjectInputStream is = null;
		try {
			String filename = System.getProperty("user.dir") + "/src/test/java/test08_io/test.txt.out";
			os = new ObjectOutputStream(new FileOutputStream(filename));
			os.writeObject(new Student("张三", 23));
			os.writeObject(new Student("小娟", 21));
			os.flush();
			
			is = new ObjectInputStream(new FileInputStream(filename));
			Student stu01 = (Student) is.readObject();
			Student stu02 = (Student) is.readObject();
			System.out.println(stu01 + "  "  + stu02);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 测试 BufferedInputStream / BufferedOutputStream
	 * 
	 */
	@Test
	public void test04() {
		BufferedInputStream is = null;
		BufferedOutputStream os = null;
		try {
			String filename = System.getProperty("user.dir") + "/src/test/java/test08_io/test.txt";
			String filename2 = System.getProperty("user.dir") + "/src/test/java/test08_io/test.txt.out";
			is = new BufferedInputStream(new FileInputStream(filename));
			os = new BufferedOutputStream(new FileOutputStream(filename2));
			
			byte[] bs = new byte[8];
			int len = 0;
			while ((len=is.read(bs)) != -1) {
				os.write(bs, 0, len);
				os.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * 测试 BufferedReader / BufferedWriter
	 * 
	 */
	@Test
	public void test05() {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			String filename = System.getProperty("user.dir") + "/src/test/java/test08_io/test.txt";
			String filename2 = System.getProperty("user.dir") + "/src/test/java/test08_io/test.txt.out";
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename2)));
			String line = null;
			while ((line=br.readLine()) != null) {
				bw.write(line + System.getProperty("line.separator"));
				bw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 测试 BufferedReader / PrintWriter
	 * 
	 */
	@Test
	public void test06() {
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			String filename = System.getProperty("user.dir") + "/src/test/java/test08_io/test.txt";
			String filename2 = System.getProperty("user.dir") + "/src/test/java/test08_io/test.txt.out";
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			pw = new PrintWriter(new FileOutputStream(filename2));
			String line = null;
			while ((line=br.readLine()) != null) {
				pw.println(line);
				pw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pw.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * 测试其他：
	 * 回退字节流 PushbackInputStream
	 * 
	 */
	@Test
	public void test07() {
		PushbackInputStream pis = null;
		try {
			String str = "hello,world,haha!";
			pis = new PushbackInputStream(new ByteArrayInputStream(str.getBytes()));
			int ch = 0;
			while ((ch=pis.read()) != -1) {
				if (ch==',') { //判断读取的是否是逗号  
					pis.unread(ch);
					ch = pis.read();
					System.out.print("(回退"+ (char)ch +")");
				} else {
					System.out.print((char)ch);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				pis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 测试其他：
	 * 合并多个输入流
	 * 
	 */
	@Test
	public void test08() {
		SequenceInputStream sis = null;
		BufferedOutputStream bos = null;
		try {
			// 构建流集合
			String filename1 = System.getProperty("user.dir") + "/src/test/java/test08_io/test.txt";
			String filename2 = System.getProperty("user.dir") + "/src/test/java/test08_io/test2.txt";
			String filename3 = System.getProperty("user.dir") + "/src/test/java/test08_io/test3.txt";
			String filename4 = System.getProperty("user.dir") + "/src/test/java/test08_io/test.txt.out";
			
			Vector<InputStream> vector = new Vector<>(3);
			vector.add(new FileInputStream(filename1));
			vector.add(new FileInputStream(filename2));
			vector.add(new FileInputStream(filename3));
			Enumeration<? extends InputStream> es = vector.elements();
			sis = new SequenceInputStream(es);
			bos = new BufferedOutputStream(new FileOutputStream(filename4));
			
			byte[] bs = new byte[8];
			int len = 0;
			while ((len=sis.read(bs)) != -1) {
				bos.write(bs, 0, len);
				bos.flush();
				System.out.println(len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
				sis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
