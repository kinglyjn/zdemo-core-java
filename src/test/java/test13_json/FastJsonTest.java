package test13_json;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * FastJson对于json的解析主要用到了三个类：
 * JSON：fastJson的解析器，用于JSON格式字符串与JSON对象及javaBean之间的转换
 *       public abstract class JSON implements JSONStreamAware, JSONAware {...}
 * JSONObject：fastJson提供的json对象，可以把它当成一个Map<String,Object>来看，只是JSONObject提供了更为丰富便捷的方法
 *       public class JSONObject extends JSON implements Map<String, Object>, Cloneable, Serializable, InvocationHandler{...}
 * JSONArray：fastJson提供json数组对象，可以把它看成JSONObject对象的一个集合。
 * 
 * 解析过程中数据的三种状态：
 * JSON字符串 <---> JSONObject/JSONArray <---> POJO
 * 
 */
public class FastJsonTest {
	
	/**
	 * （一）JSON_OBJECT
	 * 
	 */
	private static final String JSON_OBJECT1 = "{\"int0\":24, "
											+ "\"long0\":24L, "
											+ "\"float0\":24.12F, "
											+ "\"double0\":24.12D, "
											+ "\"bigDecimal0\":24.12,"
											+ "\"bool0\":true, "
											+ "\"date0_1\":\"2018-09-30 09:09:09\", "
											+ "\"date0_2\":\"1538289780052\", "
											+ "\"date0_3\":1538289780052}";
	private static final String JSON_OBJECT2 = "{\"int0_1\":24, "
											+ "\"long0\":24, "
											+ "\"float0\":24.12, "
											+ "\"double0\":24.12, "
											+ "\"bigDecimal0\":24.12,"
											+ "\"bool0\":true, "
											+ "\"date0_1\":\"2018年9月30日 09:09:09\", "
											+ "\"date0_2\":\"1538289780052\", "
											+ "\"date0_3\":1538289780052}";
	static class TargetObject1 {
		@JSONField(name="int0_1")
		public Integer int0;
		@JSONField(deserialize=false, serialize=false)
		public Long long0;
		public Float float0;
		public Double double0;
		public BigDecimal bigDecimal0;
		public Boolean bool0;
		@JSONField(format="yyyy年MM月dd日 HH:mm:ss")
		public Date date0_1;
		public Date date0_2;
		public Date date0_3;
		// 若不使用get/set方法，则访问修饰符必须为"public"
		// 若为私有变量也可以使用get/set获取和设置
		@Override
		public String toString() {
			return "TargetObject1 [int0=" + int0 + ", long0=" + long0 + ", float0=" + float0 + ", double0=" + double0
					+ ", bigDecimal0=" + bigDecimal0 + ", bool0=" + bool0 + ", date0_1=" + date0_1 + ", date0_2="
					+ date0_2 + ", date0_3=" + date0_3 + "]";
		}
	}
	@Test
	public void test01() {
		/**
		 * 解析json串为JSONObject
		 * 能够自动识别如下日期格式 ISO-8601日期格式、yyyy-MM-dd、yyyy-MM-dd HH:mm:ss、yyyy-MM-dd HH:mm:ss.SSS、毫秒数字、毫秒数字字符串
		 */
		JSONObject jsonObject = JSON.parseObject(JSON_OBJECT1);
		Integer int0 = (Integer) jsonObject.get("int0");
		Float float0 = (Float) jsonObject.get("float0");
		Double double0 = (Double) jsonObject.get("double0");
		BigDecimal bigDecimal0 = (BigDecimal) jsonObject.get("bigDecimal0");
		Boolean bool0 = (Boolean) jsonObject.get("bool0");
		Date date0_1 = jsonObject.getDate("date0_1");
		Date date0_2 = jsonObject.getDate("date0_2");
		Date date0_3 = jsonObject.getDate("date0_3");
		System.out.println("int0="+int0+",float0="+float0+",double0="+double0+",bigDecimal0="+bigDecimal0+",bool0="+bool0
				+",date0_1="+date0_1+",date0_2="+date0_2+",date0_3="+date0_3);
		// int0=24,float0=24.12,double0=24.12,bigDecimal0=24.12,bool0=true,date0_1=Sun Sep 30 09:09:09 CST 2018,date0_2=Sun Sep 30 14:43:00 CST 2018,date0_3=Sun Sep 30 14:43:00 CST 2018

		/**
		 * JSONObject转为POJO
		 * 可以使用 @JSONField 注解对带转化的POJO进行设置
		 */
		TargetObject1 targetObject1 = jsonObject.toJavaObject(TargetObject1.class);
		System.out.println(targetObject1);
		// TargetObject1 [int0=null, long0=null, float0=24.12, double0=24.12, bigDecimal0=24.12, bool0=true, date0_1=Sun Sep 30 09:09:09 CST 2018, date0_2=Sun Sep 30 14:43:00 CST 2018, date0_3=Sun Sep 30 14:43:00 CST 2018]
		
		/**
		 * 解析json串为POJO对象
		 * 可以使用 @JSONField 注解对带转化的POJO进行设置
		 */
		TargetObject1 targetObject2 = JSON.parseObject(JSON_OBJECT2, TargetObject1.class);
		System.out.println(targetObject2);
		// TargetObject1 [int0=24, long0=null, float0=24.12, double0=24.12, bigDecimal0=24.12, bool0=true, date0_1=Sun Sep 30 09:09:09 CST 2018, date0_2=Sun Sep 30 14:43:00 CST 2018, date0_3=Sun Sep 30 14:43:00 CST 2018]
		
		/**
		 * 转化POJO对象为指定格式的JSON串
		 * 尤其注意时间格式
		 */
		String json2_1 = JSON.toJSONString(targetObject2);
		String json2_2 = JSON.toJSONString(targetObject2, 
				SerializerFeature.WriteMapNullValue,
				SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteDateUseDateFormat);
		System.out.println(json2_1);
		System.out.println(json2_2);
		// {"bigDecimal0":24.12,"bool0":true,"date0_1":"2018年09月30日 09:09:09","date0_2":1538289780052,"date0_3":1538289780052,"double0":24.12,"float0":24.12,"int0_1":24}
		// {"bigDecimal0":24.12,"bool0":true,"date0_1":"2018年09月30日 09:09:09","date0_2":"2018-09-30 14:43:00","date0_3":"2018-09-30 14:43:00","double0":24.12,"float0":24.12,"int0_1":24}
	}
	
	
	/**
	 * （二）JSON_ARRAY
	 * 
	 */
	private static final String JSON_ARRAY = "["
			+ "{\"num0\":24, \"numf0\":24.12, \"bool0\":true, \"date0\":\"2018-09-30 09:09:09\"}, "
			+ "{\"num0\":25, \"numf0\":25.12, \"bool0\":false, \"date0\":\"2018-09-30 10:10:10\"}"
			+ "]";
	static class TargetObject2 {
		public int num0;
		public float numf0;
		public boolean bool0;
		@JSONField(format="yyyy-MM-dd HH:mm:ss")
		public Date date0;
		@Override
		public String toString() {
			return "TargetObject2 [num0=" + num0 + ", numf0=" + numf0 + ", bool0=" + bool0 + ", date0=" + date0 + "]";
		}
	}
	@Test
	public void test02() {
		/**
		 * 解析json串为JSONArray
		 */
		JSONArray jsonArray = JSON.parseArray(JSON_ARRAY);
		// 遍历方式1
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			System.out.println(jsonObject);
		}
		// 遍历方式2
		for (Object obj : jsonArray) {
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println(jsonObject);
		}
		
		/**
		 * JSONArray转为POJO LIST
		 */
		List<TargetObject2> list1 = jsonArray.toJavaList(TargetObject2.class);
		for (TargetObject2 t : list1) {
			System.out.println(t);
		}
		
		/**
		 * 解析json串为POJO LIST对象
		 */
		List<TargetObject2> list2 = JSON.parseArray(JSON_ARRAY, TargetObject2.class);
		for (TargetObject2 t : list2) {
			System.out.println(t);
		}
		
		/**
		 * 转化POJO LSIT为指定格式的JSON串
		 */
		String json2_1 = JSON.toJSONString(list2);
		System.out.println(json2_1);
	}
	
	
	/**
	 * （三）JSON_COMPLEX
	 * 
	 */
	private static final String JSON_CONPLEX = "{\"teacherName\":\"小娟\", "
			+ "\"teacherAge\":24, "
			+ "\"course\":{\"courseName\":\"英语\", \"code\":1270}, "
			+ "\"students\":["
			+ "		{\"studentName\":\"张三\", \"studentBirthday\":\"1992-09-09 12:12:12\", "
			+ "			\"address\":{\"city\":\"北京\", \"street\":\"西二旗\"}},"
			+ "		{\"studentName\":\"李四\", \"studentBirthday\":\"1990-07-07 11:11:11\", "
			+ "			\"address\":{\"city\":\"昆明\", \"street\":\"凤凰山\"}},"
			+ "		{\"studentName\":\"王五\", \"studentBirthday\":\"1992-06-06 10:10:10\", "
			+ "			\"address\":{\"city\":\"聊城\", \"street\":\"光岳楼\"}}]}";
	static class Teacher {
		public String teacherName;
		public Integer teacherAge;
		public Course course;
		public List<Student> students;
		@Override
		public String toString() {
			return "Teacher [teacherName=" + teacherName + ", teacherAge=" + teacherAge + ", course=" + course
					+ ", students=" + students + "]";
		}
	}
	static class Course {
		public String courseName;
		public Integer code;
		@Override
		public String toString() {
			return "Course [courseName=" + courseName + ", code=" + code + "]";
		}
	}
	static class Student {
		public String studentName;
		@JSONField(format="yyyy-MM-dd HH:mm:ss")
		public Date studentBirthday;
		public Address address;
		@Override
		public String toString() {
			return "Student [studentName=" + studentName + ", studentBirthday=" + studentBirthday + ", address="
					+ address + "]";
		}
	}
	static class Address {
		public String city;
		public String street;
		@Override
		public String toString() {
			return "Address [city=" + city + ", street=" + street + "]";
		}
	}
	@Test
	public void test03() {
		/**
		 * 解析json串为JSONObject
		 */
		JSONObject jsonObject = JSON.parseObject(JSON_CONPLEX);
		System.out.println(jsonObject.getString("teacherName"));
		System.out.println(jsonObject.getJSONObject("course").getString("courseName"));
		System.out.println(jsonObject.getJSONArray("students").getJSONObject(0).getJSONObject("address").getString("city"));
		
		/**
		 * JSONObject转化为POJO
		 */
		Teacher teacher1 = jsonObject.toJavaObject(Teacher.class);
		System.out.println(teacher1);
		
		/**
		 * JSON串直接转化为POJO
		 */
		Teacher teacher2 = JSON.parseObject(JSON_CONPLEX, Teacher.class);
		System.out.println(teacher2);
		
		/**
		 * POJO转化为JSON串
		 */
		String json1 = JSON.toJSONString(teacher2);
		System.out.println(json1);
	}
	
	
	/**
	 *（四）
	 * 测试使用@JSONCreator注解指定构造方法
	 * 测试枚举类型的序列化和反序列化
	 * 
	 */
	private static final String JSON_USER = "{\"username\":\"zhangsan\", \"gender\":\"男\"}";
	static class User {
		public String username;
		@JSONField(serialize=false, deserialize=false)
		public Gender genderEnum;
		
		@JSONCreator
		public User(@JSONField(name="username")String username, 
				@JSONField(name="gender")String genderName) {
			this.username = username;
			this.genderEnum = Gender.getGenderByName(genderName);
		}
		
		@JSONField(name="gender") // obj转json用（geter/setter方法存在时，Fastjson使用时关键找其上的 @JSONField注解）
		public String getGender() {
			return genderEnum.getName();
		}
		@JSONField(name="gender") // json转obj用
		public void setGender(String genderName) {
			this.genderEnum = Gender.getGenderByName(genderName);
		}
		
		@Override
		public String toString() {
			return "User [username=" + username + ", genderEnum=" + genderEnum + "]";
		}
	}
	static enum Gender {
		MALE("男",1), FEMALE("女",0);
		private String name;
		private int code;
		private Gender(String name, int code) {
			this.name = name;
			this.code = code;
		}
		public String getName() {
			return name;
		}
		public int getCode() {
			return code;
		}
		public static Gender getGenderByName(String genderName) {
			for (Gender gender : Gender.values()) {
				if (gender.getName().equals(genderName)) {
					return gender;
				}
			}
			return null;
		}
	}
	@Test
	public void test04() {
		User user = JSON.parseObject(JSON_USER, User.class);
		System.out.println(user);
		// User [username=zhangsan, genderEnum=MALE]
		
		String json = JSON.toJSONString(user);
		System.out.println(json);
		// {"gender":"男","username":"zhangsan"}
	}
	
	
	/**
	 * （五）使用TypeReference
	 * 
	 */
	@Test
	public void test05() {
		Map<String, Object> map = JSON.parseObject(JSON_CONPLEX, new TypeReference<Map<String,Object>>(){});
		System.out.println(map); // Map的时间仍为字符串形式
		// {teacherAge=24, teacherName=小娟, course={"courseName":"英语","code":1270}, students=[{"studentBirthday":"1992-09-09 12:12:12","address":{"city":"北京","street":"西二旗"},"studentName":"张三"},{"studentBirthday":"1990-07-07 11:11:11","address":{"city":"昆明","street":"凤凰山"},"studentName":"李四"},{"studentBirthday":"1992-06-06 10:10:10","address":{"city":"聊城","street":"光岳楼"},"studentName":"王五"}]}
	}
	
	
	/**
	 *（六）
	 * 当需要处理超大JSON文本时，需要Stream API
	 * 在fastjson-1.1.32版本中开始提供Stream API
	 * 
	 */
	static class Student2 {
		public String name;
		@JSONField(format="yyyy-MM-dd HH:mm:ss")
		public Date birthday;
		
		public Student2(String name, Date birthday) {
			this.name = name;
			this.birthday = birthday;
		}
		public Student2() {}

		@Override
		public String toString() {
			return "Student2 [name=" + name + ", birthday=" + birthday + "]";
		}
	}
	@Test
	public void test06() throws IOException { // 超大JSON数组序列化（测试数组大小为1千万）
		String filename = System.getProperty("user.dir")+"/src/test/java/test13_json/students.json";
		JSONWriter jsonWriter = new JSONWriter(new FileWriter(filename));
		jsonWriter.startArray();
		for (int i = 0; i < 10000000; i++) {
			Student2 student2 = new Student2("name"+i, new Date());
			jsonWriter.writeValue(student2);
		}
		jsonWriter.endArray();
		jsonWriter.close();
	}
	@Test
	public void test07() throws IOException { // 超大JSON数组反序列化
		String filename = System.getProperty("user.dir")+"/src/test/java/test13_json/students.json";
		JSONReader reader = new JSONReader(new FileReader(filename));
		reader.startArray();
		while (reader.hasNext()) {
			Student2 student2 = reader.readObject(Student2.class);
			System.out.println(student2);
		}
		reader.endArray();
		reader.close();
	}
	@Test
	public void test08() throws IOException { // 超大JSON对象序列化
		String filename = System.getProperty("user.dir")+"/src/test/java/test13_json/students.json";
		JSONWriter writer = new JSONWriter(new FileWriter(filename));
		writer.startObject();
		for (int i = 0; i < 10; i++) {
			writer.writeKey("key" + i);
			writer.writeValue(new Student2("name"+i, new Date()));
		}
		writer.endObject();
		writer.close();
	}
	@Test
	public void test09() throws IOException { // 超大JSON对象序反序列化
		String filename = System.getProperty("user.dir")+"/src/test/java/test13_json/students.json";
		JSONReader reader = new JSONReader(new FileReader(filename));
		reader.startObject();
		while(reader.hasNext()) {
			String key = reader.readString();
			Student2 student2 = reader.readObject(Student2.class);
			System.out.println(key + ":" + student2);
		}
		reader.endObject();
		reader.close();
	}
	
	
	/**
	 *（七）
	 * 测试值过滤器ValueFilter
	 * 测试属性取舍过滤器PropertyFilter
	 * 
	 */
	private static final String JSON_ACCOUNT = "["
			+ "{\"name\":\"张三\", \"mobile\":\"18910019878\"}, "
			+ "{\"name\":\"李四\", \"mobile\":\"18209098765\"}, "
			+ "{\"name\":\"小娟\", \"mobile\":\"18291887676\"}]";
	static class Account {
		public String name;
		public String mobile;
		@Override
		public String toString() {
			return "Account [name=" + name + ", mobile=" + mobile + "]";
		}
	}
	@Test
	public void test10() { // 只要字段名中包含mobile，则值输出为一串星号
		List<Account> list = JSON.parseArray(JSON_ACCOUNT, Account.class);
		String resultJson = JSON.toJSONString(list, new ValueFilter(){
			@Override
			public Object process(Object object, String name, Object value) {
				// object表示该对象，name和value分别是object的属性和属性值
				if (name.toLowerCase().contains("mobile")) {
					return "****";
				}
				return value;
			}}
		);
		System.out.println(resultJson);
		// [{"mobile":"****","name":"张三"},{"mobile":"****","name":"李四"},{"mobile":"****","name":"小娟"}]
	}
	@Test
	public void test11() { // 只要字段名中包含mobile，则忽略。（也可以使用注解@JSONField 或 transient关键字实现）
		List<Account> list = JSON.parseArray(JSON_ACCOUNT, Account.class);
		String resultJson = JSON.toJSONString(list, new PropertyFilter() {
			@Override
			public boolean apply(Object object, String name, Object value) {
				if (name.toLowerCase().contains("mobile")) {
					return false;
				}
				return true;
			}}
		);
		System.out.println(resultJson);
		// [{"name":"张三"},{"name":"李四"},{"name":"小娟"}]
	}
	
}
