package test00_java8_and_javabin_cmd;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.Test;

/**
 * Java8规范了大量日期和时间相关类，这些类统一放在java.time包下
 * 这些时间相关类的实例都是不可变、线程安全的。
 *
 * 日期时间相关：
 * LocalDate、LocalTime、LocalDateTime
 * 这些类的实例对象都是不可变的，分别表示使用ISO-8601日历系统的日期、时间、日期时间，
 * ISO-8601日历系统是国际标准化组织制定的现代公民的日期和时间表示法。它们提供了简单
 * 的日期或时间，并不包含当前的时间信息也不包含与时区相关的信息。
 * 
 * 带时区的日期或时间：
 * ZonedDate、ZonedTime、ZonedDateTime
 * 每个时区都对应着id，地区id都为 “{区域/城市}” 的格式，例如 Asia/Shanghai
 * ZonedId类包含了所有的时区信息，getAvailableZoneIds()可以获取所有时区的信息，
 * of(id)用于指定时区信息获取ZoneId对象。
 * 
 * 日期时间格式化器：
 * DateTimeFormatter
 * 
 * 日期时间矫正器及其工具类：
 * TemporalAdjuster、TemporalAdjusters
 * 
 * 时间戳相关：时间戳以Unix元年即1970-01-01 00:00:00到某个时间点间的毫秒值
 * Instant
 * 
 */
public class Test07_Date_And_Time {

	/**
	 * 测试传统时间相关联类的线程安全性（不安全）
	 * 
	 */
	@Test
	public void test01() throws Exception { // 运行结果出现异常 NumberFormatException: multiple points
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ExecutorService pool = Executors.newFixedThreadPool(10);
		List<Future<Date>> futures = new ArrayList<>();
		
		for (int i = 0; i < 10; i++) {
			Future<Date> future = pool.submit(new Callable<Date>() {
				@Override
				public Date call() throws Exception {
					return sdf.parse("2018-08-08");
				}
			});
			futures.add(future);
		}
		
		for (Future<Date> future : futures) {
			Date result = future.get();
			System.out.println(result);
		}
		pool.shutdown();
	}
	
	/**
	 * 传统时间相关联类的线程安全性问题的解决
	 * 
	 */
	@Test
	public void test02() throws Exception {
		ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
			@Override
			protected SimpleDateFormat initialValue() {
				return new SimpleDateFormat("yyyy-MM-dd");
			}
		};
		ExecutorService pool = Executors.newFixedThreadPool(10);
		List<Future<Date>> futures = new ArrayList<>();
		
		for (int i = 0; i < 10; i++) {
			Future<Date> future = pool.submit(new Callable<Date>() {
				@Override
				public Date call() throws Exception {
					return sdf.get().parse("2018-08-08");
				}
			});
			futures.add(future);
		}
		
		for (Future<Date> future : futures) {
			Date result = future.get();
			System.out.println(result);
		}
		pool.shutdown();
	}
	
	
	/**
	 * 日期格式化相关：
	 * Java8中相关的时间类都设置为不可变的线程安全类型，类似于String类
	 * 
	 */
	@Test
	public void test03() throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		ExecutorService pool = Executors.newFixedThreadPool(10);
		List<Future<LocalDate>> futures = new ArrayList<>();
		
		for (int i = 0; i < 10; i++) {
			Future<LocalDate> future = pool.submit(new Callable<LocalDate>() {
				@Override
				public LocalDate call() throws Exception {
					return LocalDate.parse("2018-08-08", dtf);
				}
			});
			futures.add(future);
		}
		
		for (Future<LocalDate> future : futures) {
			LocalDate result = future.get();
			System.out.println(result);
		}
		pool.shutdown();
	}
	
	/**
	 * 日期格式化相关2：
	 * 
	 */
	@Test
	public void test04() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		LocalDateTime ldt = LocalDateTime.now();
		
		String ldtStr = ldt.format(dtf);
		System.out.println(ldtStr); //2018-08-09 20:41:28.197
		
		LocalDateTime ldt2 = LocalDateTime.parse("2018-08-09 20:41:28.197", dtf);
		System.out.println(ldt2);	//2018-08-09 20:41:28.197
	}
	
	/**
	 * 日期和时间相关类
	 * LocalDate、LocalTime、LocalDateTime
	 * 
	 */
	@Test
	public void test05() {
		LocalDateTime now = LocalDateTime.now();
		System.out.println(now); 	//2018-08-09T19:59:49.644
		
		LocalDateTime ldt1 = LocalDateTime.of(2018, 8, 8, 20, 20, 30);
		System.out.println(ldt1); 	//2018-08-08T20:20:30
		
		LocalDateTime ldt2 = ldt1.plusYears(2);
		System.out.printf("%s, %s\n", ldt2, ldt1==ldt2);	//2020-08-08T20:20:30, false
		
		LocalDateTime ldt3 = ldt1.minusMonths(2);
		System.out.println(ldt3);	//2018-06-08T20:20:30
		
		System.out.println(ldt3.getYear());
		System.out.println(ldt3.getMonthValue());
		System.out.println(ldt3.getDayOfMonth());
		System.out.println(ldt3.getHour());
		System.out.println(ldt3.getMinute());
		System.out.println(ldt3.getSecond());
		
		LocalDateTime ldt4 = ldt3.withDayOfMonth(10);
		System.out.println(ldt4); 	//2018-06-10T20:20:30
		
		LocalDateTime ldt5 = ldt4.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
		System.out.println(ldt5); 	//2018-06-17T20:20:30 通过实时间矫正器将时间调整到下一个周日的当前时间
	}
	
	/**
	 * 带时区的日期和时间相关类
	 * ZonedDate、ZonedTime、ZonedDateTime
	 * 
	 */
	@Test
	public void test06() {
		ZoneId.getAvailableZoneIds().forEach(System.out::println);
		
		LocalDateTime ldt1 = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
		System.out.println(ldt1); //2018-08-09T20:59:05.275
		
		ZonedDateTime zdt1 = ldt1.atZone(ZoneId.of("UTC"));
		System.out.println(zdt1); //2018-08-09T20:59:05.275Z[UTC]
		System.out.println(ldt1.atZone(ZoneId.of("Africa/Niamey"))); //2018-08-09T20:59:05.275+01:00[Africa/Niamey]
	}
	
	/**
	 * 时间戳相关
	 * 
	 */
	@Test
	public void test07() {
		Instant instant1 = Instant.now(); 
		System.out.println(instant1); //2018-08-09T12:15:48.310Z 默认获取当前UTC时区时间（时间协调时间）
		
		OffsetDateTime odt = instant1.atOffset(ZoneOffset.ofHours(8)); //2018-08-09T20:15:48.310+08:00 东八区时间
		System.out.println(odt);
		
		System.out.println(instant1.toEpochMilli()); 	//1533817133187
		System.out.println(Instant.ofEpochSecond(60)); 	//1970-01-01T00:01:00Z
		
		Instant instant2 = Instant.now();
		System.out.println("时间间隔1：" + Duration.between(instant1, instant2).toMillis() + "ms.");
		
		LocalDateTime ldt1 = LocalDateTime.now();
		LocalDateTime ldt2 = LocalDateTime.now();
		System.out.println("时间间隔2：" + Duration.between(ldt1, ldt2).toMillis());
		
		LocalDate ld1 = LocalDate.of(2017, 7, 8);
		LocalDate ld2 = LocalDate.now();
		System.out.println("日期间隔1：" + Period.between(ld1, ld2)); //P1Y1M1D
	}
	
}

