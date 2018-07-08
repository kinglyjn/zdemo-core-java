package test02_collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.Test;

/**
 * 集合测试
 * @author zhangqingli
 *
 */
public class Test01 {
	
	/**
	 * 测试 使用Comparable接口内部实现排序
	 * 
	 */
	@Test
	public void test01() {
		Set<Student> set = new TreeSet<Student>();
		set.add(new Student("张三", 23));
		set.add(new Student("张三", 23));
		set.add(new Student("小娟", 23));
		set.add(new Student("赵六", 28));
		set.add(new Student("王五", 21));
		set.add(new Student("田七", 90));
		
		for (Student student : set) {
			System.out.println(student);
		}
		
		// Student [name=田七, score=90]
		// Student [name=赵六, score=28]
		// Student [name=小娟, score=23]
		// Student [name=张三, score=23]
		// Student [name=王五, score=21]
	}
	
	
	/**
	 * 使用Comparator实现外部排序 之 List
	 * 
	 */
	@Test
	public void test02() {
		List<Integer> list = new ArrayList<Integer>();
	    list.add(-3);
	    list.add(1);
	    list.add(2);
	    
	    for (Integer i : list) {
	    	System.out.println(i);
	    } //-3 1 2
	    
	    Collections.sort(list, new Comparator<Integer>() {
	    	@Override
	    	public int compare(Integer o1, Integer o2) {
	    		return Math.abs(o1)-Math.abs(o2);
	    	}
		});
	    
	    for (Integer i : list) {
			System.out.println(i);
		} //1 2 -3
	}
	
	
	/**
	 * 使用Comparator实现外部排序 之 Map
	 * 
	 */
	@Test
	public void test03() {
		Map<Integer, String> map = new TreeMap<Integer,String>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return Math.abs(o1)-Math.abs(o2);
			}
		});
		
		map.put(-3, "aaa");
		map.put(1, "bbb");
		map.put(2, "ccc");
		
		Set<Integer> keySet = map.keySet();
		for (Integer key : keySet) {
			System.out.println(key + "--->" + map.get(key));
		}
		
		//1--->bbb
        //2--->ccc
        //-3--->aaa
	}
	
	
	/**
	 * Collections包装collection和map，实现集合类的线程安全
	 * 类似的可以生成线程安全的set和map
	 * 
	 */
	@Test
	public void test04() {
	    ArrayList<Integer> list = new ArrayList<Integer>(); //list是线程不安全的

	    Collection<Integer> sCollection = Collections.synchronizedCollection(list); //
		System.out.println(sCollection);
		
		List<Integer> sList = Collections.synchronizedList(list); //sList是线程安全的
		System.out.println(sList);
	}
	
	
}
