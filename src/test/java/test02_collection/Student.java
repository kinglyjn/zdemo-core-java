package test02_collection;

public class Student implements Comparable<Student>{
	private String name;
	private int score;
	
	public Student(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}
	public Student() {
		super();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	@Override
	public String toString() {
		return "Student [name=" + name + ", score=" + score + "]";
	}
	
	
	//
	@Override
	public int compareTo(Student stu) {
		if (stu==null) {
			return 1;
		}
		if (this.score == stu.getScore()) {
			return this.getName().compareTo(stu.getName());
		} else if (this.score < stu.getScore()) {
			return 1;
		} else {
			return -1;
		}
	}
	
	/*
	  HashSet 只有在 hashCode 返回值冲突的时 候才会调用 equals 方法进行判断，
	  也就是说，两个对象，如果 hashCode 没有冲突，HashSet 就不会调用 equals 
	  方法判断而直接认为这两个对象是不同的对象。
	  综上：
	  如果要正常使用 HashSet 存放对象，为了保证对象的内容不重复，则要求这 个对象满足:
	  1. 覆盖 equals 方法。要求相同的对象，调用 equals 方法返回 true。
	  2. 覆盖 hashCode 方法。要求，相同对象的 hashCode 相同，不同对象的 hashCode 尽量不同。
	*/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + score;
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (score != other.score)
			return false;
		return true;
	}
}
