package test02_design_patterns;

/**
 * 测试 策略模式
 *
 */
public class Test04Comparator {
	
	 /*
	 * main
	 * 
	 */
	public static void main(String[] args) {
		Dog[] dogs = {new Dog(7, 3), new Dog(2, 2), new Dog(5, 6)};
		Arrays.sort(dogs);  //要求Dog类实现Compareble接口，默认按狗的重量排序
		for (int i = 0; i < dogs.length; i++) {
	        System.out.println(dogs[i].getWeight());
	    }
	}
	
	/**
	 * 比较对象
	 * 
	 */
	static class Dog implements Comparable<Dog> {
		private int weight;
		private int height;
		
		public Dog(int weight, int height) {
			this.weight = weight;
			this.height = height;
		}
		
		public int getWeight() {
			return weight;
		}
		public void setWeight(int weight) {
			this.weight = weight;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
		
		@Override
		public int compareTo(Dog t) {
			return new WeightComparator().compare(this, t);
		}
	}
	
	/**
	 * Comparable 接口
	 * 
	 */
	static interface Comparable<T> {
		int compareTo(T t);
	}
	
	/**
	 * Comparator 接口
	 * 
	 */
	static interface Comparator<T> {
		int compare(T o1, T o2);
	}
	/**
	 * Comparator 1 2
	 * 
	 */
	static class WeightComparator implements Comparator<Dog> {
		@Override
		public int compare(Dog o1, Dog o2) {
			if (o1.getWeight() < o2.getWeight()) {
				return 1;
			} else if (o1.getWeight() > o2.getWeight()) {
				return -1;
			}
			return 0;
		}
	}
	static class HeightComparator implements Comparator<Dog> {
		@Override
		public int compare(Dog o1, Dog o2) {
			if (o1.getHeight() < o2.getHeight()) {
				return 1;
			} else if (o1.getHeight() > o2.getHeight()) {
				return -1;
			}
			return 0;
		}
	}
	
	/**
	 * 工具类
	 * 
	 */
	static class Arrays {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public static void sort(Object[] os) {
			for (int i = 0; i < os.length; i++) {
				for (int j = 0; j < os.length-i-1; j++) {
					Comparable o1 = (Comparable) os[j];
	                Comparable o2 = (Comparable) os[j+1];
	                if (o1.compareTo(o2) < 0) {
	                	Object tmp = os[j];
	                    os[j] = os[j+1];
	                    os[j+1] = tmp;
	                }
				}
			}
		}
	}
	
}
