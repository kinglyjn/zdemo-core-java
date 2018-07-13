package test00_java8_and_javabin_cmd;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * Stream API
 * 这是目前为止最大的一次对Java库的完善
 * Steam API极大得简化了集合操作（后面我们会看到不止是集合）
 * 
 */
public class Test02StreamAPI {
	
	/**
	 * 测试 StreamAPI集合操作
	 *
	 */
	static class Classroom {
		private boolean open; 
		private int stuNum;
		public Classroom(boolean open, int stuNum) {
			this.open = open;
			this.stuNum = stuNum;
		}
		public boolean isOpen() {
			return open;
		}
		public int getStuNum() {
			return stuNum;
		}
		@Override
		public String toString() {
			return "Classroom [open=" + open + ", stuNum=" + stuNum + "]";
		}
	}
	
	private Collection<Classroom> classrooms = Arrays.asList(
			new Classroom(true, 5), 
			new Classroom(true, 13), 
			new Classroom(false, 3));
	
	/**
	 * 此例中：
	 * 1. classrooms集合会被转换成steam表示
	 * 2. filter操作会筛选出所有classroom#open=true的教室
	 * 3. mapToInt操作基于每个classroom实例的Classroom::getStuNum方法将classroom流转化为IntStream流
	 * 4. sum操作计算总和，得出最后的结果
	 * 
	 * Steam之上的操作可分为中间操作和晚期操作：
	 * 中间操作会返回一个新的steam——执行一个中间操作（例如filter）并不会执行实际的过滤操作，而是创建一个新的steam，
	 * 并将原steam中符合条件的元素放入新创建的steam。晚期操作（例如forEach或者sum），会遍历steam并得出结果或者附
	 * 带结果；在执行晚期操作之后，steam处理线已经处理完毕，就不能使用了。在几乎所有情况下，晚期操作都是立刻对steam
	 * 进行遍历。
	 * 
	 */
	@Test
	public void test01() {
		int totalNum = classrooms.stream()
			.filter(classroom->classroom.isOpen()==true)
			.mapToInt(classroom->classroom.getStuNum())
			.sum();
		System.out.println("所有开放教室学生总数为：" + totalNum); // 18
	}
	
	
	/**
	 * 测试 并行计算
	 * 这里我们使用parallel方法并行处理所有的task，并使用reduce方法计算最终的结果。
	 * 
	 */
	@Test
	public void test02() {
		Integer totalNum = classrooms.stream()
			.parallel()
			.map(classroom->classroom.getStuNum()) //or Classroom::getStuNum
			.reduce(0, Integer::sum);
		System.out.println("所有教室的学生总数为：" + totalNum); // 21
	}
	
	
	/**
	 * 测试 根据某些条件对集合的元素进行分组
	 * 根据教室是否开放对教室进行分组
	 * 
	 */
	@Test
	public void test03() {
		Map<Boolean, List<Classroom>> collectMap = 
				classrooms.stream()
				.collect(Collectors.groupingBy(Classroom::isOpen));
		collectMap.forEach((k,v)->System.out.println(k+ " " + v));
	}
	
	
	/**
	 * 测试 计算集合中每个节点在集合中所占的比重
	 * 计算每个教室学生数占总学生数的百分比
	 * 
	 */
	@Test
	public void test04() {
		Integer totalNum = classrooms.stream()
				.parallel()
				.map(classroom->classroom.getStuNum()) //or Classroom::getStuNum
				.reduce(0, Integer::sum);
		
		List<String> result = classrooms.stream()		//Stream< String >
			.mapToInt(Classroom::getStuNum)				// IntStream
			.asLongStream()								// LongStream
			.mapToDouble(stuNum->1.0*stuNum/totalNum)	// DoubleStream
			.boxed() 									// 转换为对象流 (类似装箱)，即 Stream< Double >
			.mapToLong(weight->Math.round(weight*100))	// LongStream
			.mapToObj(percent->percent+"%")				// Stream<String> 
			.collect(Collectors.toList());				// List<String>
		System.out.println(result); 					// [24%, 62%, 14%]
	}
	
	
	/**
	 * Steam API不仅可以作用于Java集合，传统的IO操作（从文件或者网络一行一行得读取数据）可以受益于steam处理
	 * 
	 */
	@Test
	public void test05() throws IOException {
		String filename = System.getProperty("user.dir") + "/src/test/java/test00_java8_and_javabin_cmd/test.txt";
		final Path path = new File( filename ).toPath();
		try( Stream< String > lines = Files.lines( path, StandardCharsets.UTF_8 ) ) {
		    lines.onClose( () -> System.out.println("Done!") ).forEach( System.out::println );
		}
	}
	
	
	/**
	 * 并行数组
	 * Java8版本新增了很多新的方法，用于支持并行数组处理。
	 * 最重要的方法是parallelSort()，可以显著加快多核机器上的数组排序。
	 * 下面代码使用parallelSetAll()方法生成20000个随机数，然后使用parallelSort()方法进行排序。
	 * 这个程序会输出乱序数组和排序数组的前10个元素。
	 * 
	 */
	@Test
	public void test06() {
		long[] array = new long[2000];
		Arrays.parallelSetAll(array, index->ThreadLocalRandom.current().nextInt(1000000));
		Arrays.stream(array)
			.limit(10)
			.forEach(i->System.out.print(i + "  "));
		System.out.println();
		
		Arrays.parallelSort(array);
		Arrays.stream(array)
			.limit(10)
			.forEach(i -> System.out.print(i + " " ));
	}
	
}















