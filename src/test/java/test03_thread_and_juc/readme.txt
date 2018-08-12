
0. 并发编程的三性和Java内存模型
	
	并发编程的三性:
	a.可见性
	b.原子性
	c.有序性

	Java内存模型:
	主存------------------...
	|             |
	|             |
	线程1本地内存   线程2本地内存


    JVM内存结构:
    
                                     JVM   
                                      |
                              ---------------------------------------------------------------------------
	                          |                                              |       |        |         |
	                          |                                              |       |        |         |
	                       堆(Heap)                                       Metaspace  本地方法区 程序计数器  虚拟机栈
    ------------------------------------------------------------         从JDK 8开始
    |    8      | 1  | 1  |                                     |        Java开始使用 
    |   eden    |from| to |                                     |       元空间取代永久代
    |           |(s1)|(s1)|                                     |      元空间并不在虚拟机中
    |           |    |    |                                     |      而是直接使用本地内存
    |___________|____|____|_____________________________________|
              新生代1    +15-—>            老年代2
	                    -XX:MaxTenuringThreshold(默认15)
	                    -XX:PretenureSizeThreshold 3M
	
	
	
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
	// 可灵活回收空闲线程，如无可回收，则创建新的线程，它完全依赖于JVM能创建线程的数量，可能引起内存不足。
	Executors.newCachedThreadPool() 
	
	// 创建一个定长线程池，可控制线程最大并发数，超出的线程会在对列中等待。企业中使用最多！
	// 线程池数的合理配置，需要分CPU密集型和IO密集型
	Executors.newFixedThreadPool(int) 
	
	// 返回以个包含单线程的Executor,将多个任务交给此Exector时，这个线程处理
	// 完一个任务后接着处理下一个任务，若该线程出现异常，将会有一个新的线程来替代。
	// 而 newFixedThreadPool(1) 则是线程遇到错误则中止，它是无法使用替代线程的。
	Executors.newSingleThreadExecutor()
	
	//创建一个定长线程池池，支持定时及周期性执行任务
	Executors.newScheduledThreadPool(int) 
	
	
	/*
	* 以上获取线程池的方法最终都会调用如下方法：
	* corePoolSize：核心池的大小，当有任务来之后，就会创建一个线程去执行任务，当线程池
	*                中的线程个数达到corePoolSize后，就会把到达的任务放到缓存对列中去
	* maximumPoolSize：最大线程池大小，如果核心线程池和任务队列都满了，就会将任务创建到最大线程池中。
	* keepAliveTime：表示线程诶有任务执行时最多能保持多久时间会终止
	*
	*/
	 public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {...}
    用户创建任务提交到线程池
    |
    |
    |/             满了                   队列满了                 最大线程池满了
    基本核心线程池5<--------->线程缓存队列-------------->最大线程池10---------------->拒绝任务策略
    |                                                   |
    |未满                                                |未满
    |                                                    |
    |/                                                   |
    执行任务                                            执行任务
	
	
	线程池的配置策略：
	a.根据任务性质设置
	  要想合理的配置线程池，就必须首先分析任务特性，可以从以下几个角度来进行分析：
	  任务的性质：CPU密集型任务，IO密集型任务和混合型任务。
	  任务的优先级：高，中和低。
	  任务的执行时间：长，中和短。
	  任务的依赖性：是否依赖其他系统资源，如数据库连接。
	  任务性质不同的任务可以用不同规模的线程池分开处理。CPU密集型任务配置尽可能小的线程，如配置CPU数+1个线程的线程池。
	  IO密集型任务则由于线程并不是一直在执行任务，则配置尽可能多的线程，如2*CPU数。混合型的任务，如果可以拆分，则将其
	  拆分成一个CPU密集型任务和一个IO密集型任务，只要这两个任务执行的时间相差不是太大，那么分解后执行的吞吐率要高于串
	  行执行的吞吐率，如果这两个任务执行时间相差太大，则没必要进行分解。
	  我们可以通过Runtime.getRuntime().availableProcessors()方法获得当前设备的CPU个数。
	  执行时间不同的任务可以交给不同规模的线程池来处理，或者也可以使用优先级队列，让执行时间短的任务先执行。
	  依赖数据库连接池的任务因为线程提交SQL后需要等待数据库返回结果，如果等待的时间越长CPU空闲时间就越长，那么线程数应该设置越大，这样才能更好的利用CPU。
	  建议使用有界队列，有界队列能增加系统的稳定性和预警能力，可以根据需要设大一点，比如几千。
	  我在测试一个线程池的时候，使用循环不断提交新的任务，造成任务积压在线程池，最后程序不断
	  的抛出抛弃任务的异常。如果使用无界队列，线程池的队列就会越来越多，有可能会撑满内存，导
	  致整个系统不可用，而不只是后台任务出现问题。通常这种设置方式是比较粗略的方式。
	2.利特尔法则
	  即一个系统请求数等于请求的到达率与平均每个单独请求花费的时间之乘积
	  
	
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
	
	

	
	
	
	
	