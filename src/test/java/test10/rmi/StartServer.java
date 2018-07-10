package test10.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * RMI服务端启动类
 * @author zhangqingli
 *
 */
public class StartServer {
	
	/*
     * 1.java1.4以后根和茎统一使用一个类Xxx_Stub.class，这个类帮助我们实现服务端和客户端RMI功能类的序列化和反序列化
     * 2.这个stub类需要借助于java rmi编译器[rmic]来生成（类似于javac）
     * 3.服务端需要开启rmi注册服务[rmiregistry]（也可以在程序中使用LocateRegistry.createRegistry(1099)启动）
     */
    public static void main(String[] args) throws Exception {
        //启动rmi注册服务器
        LocateRegistry.createRegistry(1099);
        //注册功能类
        TimeServer ts = new TimeServer();
        //Naming.bind("ts1", ts); //如果发现rmiregistry服务器中已经绑定了ts1，则会抛出异常
        Naming.rebind("ts1", ts); //如果发现rmiregistry服务器中已经绑定了ts1，则会替换之前绑定的ts1对象
        System.out.println("bind timeServer complete!");
    }
    
}
