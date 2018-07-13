
1. JDK8并发编程的基础	
	a.volatile关键字保证变量在不同线程之间的可见性（volatile关键字除了保证变量在不同线程之间的可见性，还会阻止JVM对代码进行重排序）
	b.CAS算法保证操作的互斥和原子性（JDK8的CAS算法是由底层硬件支持的）
	

2. 实现volatile和CAS算法的原子变量
	java.util.concurrent.atomic.AtomicXXX
   常用并发集合
    java.util.concurrent.ConcurrentHashMap
	
	
3. 并发编程锁的机制
	a. synchronized代码块 和 synchronized方法
	b. lock (常用的有 ReentrantLock、ReentrantReadWriteLock)

	LOCK锁机制的优势：
	a.序列的保证 - 同步块不提供对等待线程进行访问的序列的任何保证，但Lock接口处理它。
	b.无超时，如果未授予锁，则同步块没有超时选项。Lock接口提供了这样的选项。
	c.单一方法同步块必须完全包含在单个方法中，而Lock接口的方法lock()和unlock()可以以不同的方式调用。


4. 并发编程线程间的数据通信和流程控制
	a. synchronized-wait-notify(notifyAll)
	b. lock.newCondition().await-condition.signal(signalAll)
	c. sleep
	d. join
	e. thread.interrupt()-InterruptedException handle code
	f. public args and resource


6. 闭锁（CountDownLatch）
	对线程执行流程的有效控制。闭锁相当于一扇门，在闭锁到达结束状态之前，这扇门一直是关闭着的，不允许任
	何线程通过，当到达结束状态时，这扇门会打开并允许所有的线程通过。且当门打开了，就永远保持打开状态。


7. JAVA中新建线程的四种方式
	a. 继承Thread
	b. 实现Runnable
	c. 实现Callable
	d. 线程池预设线程
	
	
8. 线程池
	线程池提供了一个线程队列，队列中保持着所有等待状态的线程。避免了创建线程的额外开销，提高了响应速度。
	
	java.util.concurrent.Executor：线程使用和调度的根接口
		|---ExecutorService 子接口：线程池的主要接口（注意观察其JAVA API DOCS）
			|---ThreadPoolExecutor：线程池的基本实现类
			|---ScheduledExecutorService：子接口：负责线程的调度
				|---ScheduledThreadPoolExecutor：继承ThreadPoolExecutor，实现ScheduledExecutorService
	
	注意应该使用更加方便的 Executors 线程池工具类	进行线程的创建工作，它能够满足大部分线程池的需求。
	
	// 根据用户的任务数创建相应的线程来处理，该线程池不会对线程数目加以限制，
	// 完全依赖于JVM能创建线程的数量，可能引起内存不足。
	Executors.newCachedThreadPool() 
	
	// 返回一个包含指定数目线程的线程池，如果任务数量多于线程数目，那么没有
	// 没有执行的任务必须等待，直到有任务完成为止。
	Executors.newFixedThreadPool(int) 
	
	// 返回以个包含单线程的Executor,将多个任务交给此Exector时，这个线程处理
	// 完一个任务后接着处理下一个任务，若该线程出现异常，将会有一个新的线程来替代。
	// 而 newFixedThreadPool(1) 则是线程遇到错误则中止，它是无法使用替代线程的。
	Executors.newSingleThreadExecutor()
	

9. Fork-Join框架
	必要条件下将一个多任务拆分（fork）成若干小任务（拆到不可再拆时），再将一个个的小任务进行合并汇总（join）。
	采用的是“工作窃取”的模式（work-stealing）:
	当执行新的任务时，它可以将其拆分成更小的任务执行，并将小任务加入到线程队列中，然后从一个随机线程的队列中
	偷一个放到自己的队列中。相对于一般的线程池实现，fork/join框架的有事体现在对其包装的任务的处理方式上。在
	一般的线程池中，如果一个线程正在执行的任务由于某些原因无法继续执行，那么该线程会处于等待状态。而在fork/join
	框架的实现中，如果某个子问题由于等待另外一个子问题的完成而无法继续进行下去，那么处理该问题的线程会主动找寻
	其他尚未运行的子问题来执行。这种方式减少了线程的等待时间，提高了性能。
	
	
10. ThreadLocal
	用于创建只能由同一个线程读取和写入的线程局部变量。
	public T get()	返回当前线程的线程局部变量的副本中的值。
	protected T initialValue()	返回此线程局部变量的当前线程的“初始值”。
	public void remove()	删除此线程局部变量的当前线程的值。
	public void set(T value)	将当前线程的线程局部变量的副本设置为指定的值。
	
	
	

	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	