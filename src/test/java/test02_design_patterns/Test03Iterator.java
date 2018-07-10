package test02_design_patterns;

/**
 * 测试 遍历器模式
 *
 */
public class Test03Iterator {
	
	public static void main(String[] args) {
		Collection<String> list = new LinkedList<String>();
		list.add("张三");
		list.add("李四");
		list.add("小娟");
		
		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	/**
	 * 遍历器
	 *
	 */
	static interface Iterator<E> {
		boolean hasNext();
		E next();
	}
	/**
	 * 集合接口
	 * 
	 */
	static interface Collection<E> {
		void add(E e);
		int size();
		Iterator<E> iterator();
	}
	/**
	 * 数组列表
	 * 
	 */
	static class ArrayList<E> implements Collection<E> {
		private static final int DEFAULT_CAPACITY = 10;
		private Object[] array = new Object[DEFAULT_CAPACITY];
		private int index = 0;
		
		@Override
		public void add(E e) {
			array[index] = e;
			index++;
			if (index == array.length) {
				Object[] newArray = new Object[array.length*2];
				System.arraycopy(array, 0, newArray, 0, array.length);
				array = newArray;
			}
		}

		@Override
		public int size() {
			return index;
		}

		@Override
		public Iterator<E> iterator() {
			return new Iterator<E>() {
				private int currentIndex = 0;
				
				@Override
				public boolean hasNext() {
					if (currentIndex == index) {
						return false;
					}
					return true;
				}

				@Override
				@SuppressWarnings("unchecked")
				public E next() {
					E e = (E) array[currentIndex];
					currentIndex++;
					return e;
				}
			};
		}
	}
	
	/**
	 * 链表列表
	 * 
	 */
	static class LinkedList<E> implements Collection<E> {
		private Node preHead;
		private Node head;
		private Node tail;
		private int index = 0;
		
		class Node {
			private E data;
			private Node next;
			public E getData() {
				return data;
			}
			public void setData(E data) {
				this.data = data;
			}
			public Node getNext() {
				return next;
			}
			public void setNext(Node next) {
				this.next = next;
			}
		}
		
		@Override
		public void add(E e) {
			if (head == null) {
				head = tail = new Node();
				head.setData(e);
				preHead = new Node();
				preHead.setNext(head);
			} else {
				Node newNode = new Node();
				newNode.setData(e);
				tail.setNext(newNode);
				tail = newNode;
			}
			index++;
		}

		@Override
		public int size() {
			return index;
		}

		@Override
		public Iterator<E> iterator() {
			return new Iterator<E>() {
				private Node currentNode = preHead;
				
				@Override
				public boolean hasNext() {
					return currentNode.getNext()==null ? false : true;
				}

				@Override
				public E next() {
					currentNode = currentNode.getNext();
					return currentNode.getData();
				}
			};
		}
		
	}
}
