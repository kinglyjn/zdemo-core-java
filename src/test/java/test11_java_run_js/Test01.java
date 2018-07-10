package test11_java_run_js;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.junit.Test;

/**
 * 测试 使用Java运行JS
 * 
 */
public class Test01 {

	/**
	 * 测试1 使用ScriptEngineManager执行js脚本
	 * 采用解释运行的方式，每次运行都会解释运行，这是最慢的运行方式。
	 * 
	 * 
	 */
	@Test
	public void test01() throws FileNotFoundException, ScriptException {
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine engine = engineManager.getEngineByExtension("js"); // 或 engineManager.getEngineByName("javascript");
		engine.put("flag", true); // 向javascript执行引擎传递一个参数，优先级低于脚本中同名参数
		
		String filename = Test01.class.getResource("/test06_java_run_js/test01.js").getFile();
		FileReader reader = new FileReader(new File(filename));
		engine.eval(reader);
		//Double result = (Double) engine.eval("add(1, 2)");
		//System.out.println(result);
		
		Double hours = (Double) engine.eval("var d=new Date(); d.getHours();");
		System.out.printf("%s%n", hours);
	}
	
	
	/**
	 * 测试2 使用ScriptEngineManager执行js脚本（运行之前进行编译）
	 * 
	 */
	@Test
	public void test02() throws FileNotFoundException, ScriptException {
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine engine = engineManager.getEngineByExtension("js");
		engine.put("flag", true); // 向javascript执行引擎传递一个参数，优先级低于脚本中同名参数
		
		if (engine instanceof Compilable) { // 判断这个脚本引擎是否支持编译功能
			Compilable compilableEngine = (Compilable)engine;
			String filename = Test01.class.getResource("/test06_java_run_js/test01.js").getFile();
			FileReader reader = new FileReader(new File(filename));
			CompiledScript script = compilableEngine.compile(reader);
			Object object = script.eval();
			System.out.println(object);
			
			CompiledScript script2 = compilableEngine.compile("var d=new Date(); d.getHours();");
			Double hours = (Double) script2.eval();
			System.out.printf("%s%n", hours);
		}
	}
	
	
	/**
	 * 测试3 动态调用
	 * 上面的例子只有一个函数，可以通过eval进行调用并将它的值返回。但如果脚本中有多个函数或想通过
	 * 用户的输入来决定调用哪个函数，这就需要使用invoke方法进行动态调用。和编译一样，脚本引擎必须
	 * 实现Invocable接口才可以动态调用脚本语言中的方法。下面的例子将演示如何通过动态调用的方式来
	 * 运行上面的翻转字符串的javascript脚本。
	 * 
	 */
	@Test
	public void test03() throws FileNotFoundException, ScriptException, NoSuchMethodException {
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine engine = engineManager.getEngineByName("javascript");
		if (engine instanceof Invocable) {
			Invocable invokeEngine = (Invocable) engine;
			
			String filename = Test01.class.getResource("/test06_java_run_js/test01.js").getFile();
			FileReader reader = new FileReader(new File(filename));
			engine.eval(reader);
			Object result = invokeEngine.invokeFunction("reverse", "abcdefg");
			System.out.println("result1=" + result);
			
			engine.eval("function reverse2(name) {" +
	                " var output =' ';" +
	                " for (i = 0; i <= name.length; i++) {" +
	                " output = name.charAt(i) + output" +
	                " } return output;}");
			Object result2 = invokeEngine.invokeFunction("reverse2", "123456");
			System.out.println("result2=" + result2);
		}
	}
	
	
	/**
	 * 测试 异步动态调用 
	 * 如我们要想让脚本异步地执行，即通过多线程来执行，那InvokeEngine类必须实现 Runnable接口才可以通过Thread启动多线程。
	 * 可以通过getInterface方法来使InvokeEngine动态地实现 Runnable接口。这样一般可分为3步进行：
	 * 1、使用javascript编写一个run函数
	 *    engine.eval("function run() {print(异步执行);}");
	 * 2、通过getInterface方法实现Runnable接口
	 *    Runnable runner = invokeEngine.getInterface(Runnable.class);
	 * 3、使用Thread类启动多线程
	 *    new Thread(runner).start();
	 *    
	 */
	@Test
	public void test04() throws ScriptException, InterruptedException, FileNotFoundException {
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine engine = engineManager.getEngineByName("javascript");
        
		if (engine instanceof Invocable) {
			Invocable invokeEngine = (Invocable)engine;
			
			String filename = Test01.class.getResource("/test06_java_run_js/test01.js").getFile();
			FileReader reader = new FileReader(new File(filename));
			engine.eval(reader);
			
			// 必须是无参的方法名为 run 的方法
			engine.eval("function run() { var result=reverse('abcdefg'); print(result); }"); 
	        Runnable runner = invokeEngine.getInterface(Runnable.class);
	        Thread t = new Thread(runner);
	        t.start();
	        t.join();
		}
	}
	
}

