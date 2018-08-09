package test00_java8_and_javabin_cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

/**
 * 什么是Stream流？
 * 流是一种数据的渠道，用于操作数据源（集合、数组等）所生成的元素序列。
 * 注意stream自己不会存储元素，它也不会改变源对象，而是返回一个持有结果的新的stream。
 * stream操作是延迟执行的。这意味着它们会等到需要结果的时候才执行。
 * 
 * stream操作三个步骤：
 * a) 创建stream，一个数据源（集合、数组等）获取一个流
 * b) 中间操作，一个中间操作链，对数据源的数据进行处理
 * c) 终止操作，一个终止操作，执行中间操作链并产生结果
 *
 *
 * 创建流的四种方法：
 * 1. 通过Collection系列集合提供的 stream()获取串行流或parallelStream()获取并行流
 * 2. 通过 Arrays.stream(array..) 方法获取数组流
 * 3. 通过 Stream.of(..) 方法获取流
 * 4. 通过 Stream.iterate(seed, unaryOperator)或Stream.generate(supplier)方法 创建无限流
 * 
 * 
 * 多种多样的中间操作：
 * 多个中间操作连起来形成了一条数据处理流水线，除非在流水线上触发了终止操作，
 * 否则中间操作不会执行任何处理，而在终止操作时一次性全部处理，这称为“惰性取值”。
 * a) 筛选与切片：
 *    filter(predicate)：根据predicate从流中排除某些元素
 *    distinct()：通过流所生成的hashCode()和equals()方法取出重复性的元素
 *    limit(maxSize)：根据maxSize设置的大小截断流（这称为“短路”，可以极大提高效率），使其元素不超过给定的数量
 *    skip(n)：跳过元素，返回一个扔掉了前n个元素的流，若元素中元素不足n个则返回一个空流。它通常与limit(maxSize)互补使用。
 * b) 映射：
 *    map(Function)：接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素
 *    flatMap(Function)：接收一个函数作为参数，将流中的每个值都转换成另一个流，然后把所有的流连城一个流
 *    mapToDouble(ToDoubleFunction)：接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的DoubleStream
 *    mapToInt(ToIntFunction)：接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的IntStream
 *    mapToLong(ToLongFunction)：接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的LongStream
 * c) 排序：
 *    sorted()：产生一个新流，按自然顺序排序
 *    sorted(Comparator)：产生一个新流，按比较器顺序排序
 *    
 * 
 * 流的终止操作：
 * 终止操作会从流的流水线生成结果，其结果是任何不是流的值。如list、integer、void等。
 *    reduce(T identity, BinaryOperator b) &  reduce(BinaryOperator b)：将流中元素反复结合运算得到一个归约值
 *    collect(Collector) &  collect(Supplier)：将流转换为其他形式，接收一个Collector接口的实现，通常用于给Stream中元素做聚合汇总
 *    forEach(consumer)：根据consumer操作进行内部迭代
 *    allMatch(predicate)：检查是否匹配所有元素
 *    anyMatch(predicate)：检查是否至少匹配一个元素
 *    noneMatch(predicate)：检查是否没有匹配的元素
 *    findFirst()：返回第一个元素
 *    findAny()：返回当前流中的任意一个元素
 *    count：返回流中元素的总个数
 *    max(Comparator)：返回流中最大的那个元素
 *    min(Comparator)：返回流中最小的那个元素
 * 
 * 
 */
public class Test03_Stream {
	private List<Student> students = new ArrayList<>();
	static class Student {
		private String name;
		private Integer age;
		public Student() {}
		public Student(String name, Integer age) {
			this.name = name;
			this.age = age;
		}
		public String getName() {
			return name;
		}
		public Integer getAge() {
			return age;
		}
		@Override
		public String toString() {
			return "Student [name=" + name + ", age=" + age + "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((age == null) ? 0 : age.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Student other = (Student) obj;
			if (age == null) {
				if (other.age != null)
					return false;
			} else if (!age.equals(other.age))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
	}
	@Before
	public void before() {
		students.add(new Student("张三", 23));
		students.add(new Student("李四", 24));
		students.add(new Student("王五", 30));
		students.add(new Student("赵六", 14));
		students.add(new Student("赵六", 34));
		students.add(new Student("田七", 47));
		students.add(new Student("周八", 50));
		students.add(new Student("臧九", 22));
	}
	
	
	/**
	 * 创建流：
	 * 创建流的四种方法
	 * 
	 */
	@Test
	public void test01() {
		Stream<String> stream11 = new ArrayList<String>().stream();
		Stream<String> stream12 = new ArrayList<String>().parallelStream();
		System.out.println(stream11.getClass()); //java.util.stream.ReferencePipeline$Head
		System.out.println(stream12.getClass()); //java.util.stream.ReferencePipeline$Head
		
		IntStream stream2 = Arrays.stream(new int[10]);
		System.out.println(stream2.getClass()); //java.util.stream.IntPipeline$Head
		
		Stream<String> stream3 = Stream.of("aa", "bb", "cc");
		System.out.println(stream3.getClass()); //java.util.stream.ReferencePipeline$Head
		
		Stream<Integer> stream4 = Stream.iterate(0, (x)->x+2);
		System.out.println(stream4.getClass()); //java.util.stream.ReferencePipeline$Head
		Stream<Double> stream5 = Stream.generate(() -> Math.random());
		System.out.println(stream5.getClass()); //java.util.stream.ReferencePipeline$Head
	}
	
	
	/**
	 * 中间操作：
	 * map & flatMap   
	 * 如果元素是流的话，map是将每个流加入到当前流中，flatMap是将每个流中的元素加入到当前流中
	 * add(E) & addAll(Collection)  
	 * 如果元素是集合的话，add是将每个集合加入到当前集合中，addAll是将每个集合中的元素加入到当前集合中
	 * 
	 */
	@Test
	public void test02() {
		// map
		students.stream()
				.distinct()
				.filter(student-> { System.out.println("短路吧阿门！"); return student.getAge()<26; })
				.skip(1)
				.limit(3)
				.map(Student::getName)
				.forEach(System.out::println);
		
		// map & flatMap
		System.out.println("------------------------------");
		List<String> ss = Arrays.asList("aaa", "bbb", "ccc");
		ss.stream()
				.map(v->getCharacters(v))	//Stream<Stream<Character>>  {{a,a,a}, {b,b,b}, {c,c,c}}
					.forEach(v -> v.forEach(System.out::println) );
		
		System.out.println("------------------------------");
		ss.stream()
			.flatMap(v->getCharacters(v))	//Stream<Character>  {a,a,a,b,b,b,c,c,c}
			.forEach(System.out::println);
				
	}
	private Stream<Character> getCharacters(String s) {
		if (StringUtils.isEmpty(s)) {
			return null;
		}
		ArrayList<Character> resultList = new ArrayList<>();
		for (int i = 0; i < s.length(); i++) { 
			resultList.add(s.charAt(i));
		}
		return resultList.stream();
	}
	
	
	/**
	 * 终止操作：
	 * 
	 */
	@Test
	public void test03() {
		// 求首值
		Optional<Student> opt1 = students.stream()
										.sorted( (v1,v2) -> -v1.getAge().compareTo(v2.getAge()) )
										.findFirst();
		System.out.println(opt1.get());
		
		// 计数
		System.out.println("----------------------------");
		long count = students.stream()
							.count();
		System.out.println(count);
		
		// 求最大年龄的学生
		System.out.println("----------------------------");
		Optional<Student> opt2 = students.stream()
					.max( (v1,v2)->Integer.compare(v1.getAge(), v2.getAge()) );
		System.out.println(opt2.get());
		
		// 求最大年龄
		System.out.println("----------------------------");
		Optional<Integer> opt3 = students.stream()
										.map(Student::getAge)
										.max( (v1,v2)->Integer.compare(v1, v2) );
		System.out.println(opt3.orElse(0));
		
		// 归约求和
		System.out.println("----------------------------");
		List<Integer> nums = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		Integer sum = nums.stream()
				.reduce(0, (x,y)->x+y);
		System.out.println(sum);
		
		// 归约求学生年龄总和
		System.out.println("----------------------------");
		Integer sumAge = students.stream()
							.map(Student::getAge)
							.reduce(Integer::sum)
							.get();
		System.out.println(sumAge);
		
		// 收集所有学生的名字收集到list中
		System.out.println("----------------------------");
		List<String> nameList1 = students.stream()
					.map(Student::getName)
					.collect( Collectors.toList() );
		nameList1.forEach(System.out::println);
		
		// 手机所有学生的名字收集到定制化的LinkedList中
		System.out.println("----------------------------");
		List<String> nameList2 = students.stream()
				.map(Student::getName)
				.collect(Collectors.toCollection(LinkedList<String>::new) );
		nameList2.forEach(System.out::println);
		
		// 取出学生姓名并拼接成指定格式的字符串  begin>>张三,李四,王五,赵六,赵六,田七,周八,臧九<<end
		System.out.println("----------------------------");
		String collectionJoinName = students.stream()
					.map(Student::getName)
					.collect(Collectors.joining(",", "begin>>", "<<end"));
		System.out.println(collectionJoinName);
		
		// 将结果收集为学生总数。除此之外还可以求 总和值、最大值、最小值、平均值等
		System.out.println("----------------------------");
		Long syudentCount = students.stream().collect(Collectors.counting());
		System.out.println(syudentCount);
		
		// 将结果收集为统计表
		System.out.println("----------------------------");
		DoubleSummaryStatistics statistics1 = students.stream().collect(Collectors.summarizingDouble( Student::getAge ));
		IntSummaryStatistics statistics2 = students.stream().collect(Collectors.summarizingInt(Student::getAge));
		System.out.println(statistics1); //{count=8, sum=244.000000, min=14.000000, average=30.500000, max=50.000000}
		System.out.println(statistics2); //{count=8, sum=244, min=14, average=30.500000, max=50}

		// 根据学生的名字进行分组
		System.out.println("----------------------------");
		Map<String, List<Student>> studentsGroupByName = students.stream()
					.collect(Collectors.groupingBy(Student::getName));
		studentsGroupByName.keySet().forEach( k->System.out.println(k + studentsGroupByName.get(k)) );
		
		// 根据学生的年龄段进行分组
		System.out.println("----------------------------");
		Map<String, List<Student>> studentGroupByAgeRange = students.stream()
					.collect(Collectors.groupingBy(stu->{
						if (stu.getAge() < 20) {
							return "少年";
						} else if (stu.getAge() < 30) {
							return "青年";
						} else if (stu.getAge() < 50) {
							return "壮年";
						} else {
							return "老年";
						}
					}));
		studentGroupByAgeRange.entrySet()
			.forEach(v->System.out.println(v.getKey() + ": " + v.getValue()));
		
		// 根据学生的姓名和年龄进行分组
		System.out.println("----------------------------");
		Map<String, Map<String, List<Student>>> studentGroupByNameAndAge = students.stream()
					.collect( Collectors.groupingBy(Student::getName, Collectors.groupingBy(stu->{
						if (stu.getAge() < 20) {
							return "少年";
						} else if (stu.getAge() < 30) {
							return "青年";
						} else if (stu.getAge() < 50) {
							return "壮年";
						} else {
							return "老年";
						}
					})) );
		studentGroupByNameAndAge.entrySet()
			.forEach(v->System.out.println(v.getKey() + ": " + v.getValue()));
		
		// 根据学生年龄是否满足条件进行分区
		System.out.println("----------------------------");
		Map<Boolean, List<Student>> partitions1 = students.stream()
					.collect(Collectors.partitioningBy( v->v.getAge()>30 ));
		partitions1.entrySet()
					.forEach(v->System.out.println(v.getKey() + ":" + v.getValue()));
		
		// 根据学生年龄分区，然后对每个分区根据姓名分组
		System.out.println("----------------------------");
		Map<Boolean, Map<String, List<Student>>> partitions2 = students.stream()
					.collect(Collectors.partitioningBy(v->v.getAge()>30, 
							Collectors.groupingBy( Student::getName )));
		partitions2.entrySet()
					.forEach(v->System.out.println(v));
		
		// 流的各种转换
		List<String> resultList = students.stream()
					.mapToInt(Student::getAge)
					.asLongStream()
					.mapToDouble(v->v*1.0)
					.boxed()
					.mapToLong(v->v.longValue())
					.mapToObj(v->v+"%")
					.collect(Collectors.toList());
		System.out.println(resultList);
		
		
	}
	
}
