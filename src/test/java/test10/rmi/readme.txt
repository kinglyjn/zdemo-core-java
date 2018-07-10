
## 概述
	
	RMI（即Remote Method Invoke 远程方法调用）。在Java中，只要一个类extends了java.rmi.Remote接口，即可成为存在于服务器端
	的远程对象，供客户端访问并提供一定的服务。JavaDoc描述：Remote 接口用于标识其方法可以从非本地虚拟机上调用的接口。任何远程对象
	都必须直接或间接实现此接口。只有在“远程接口”（扩展 java.rmi.Remote 的接口）中指定的这些方法才可远程使用。
	注意：extends了Remote接口的类或者其他接口中的方法若是声明抛出了RemoteException异常，则表明该方法可被客户端远程访问调用。  
	
	同时，远程对象必须实现java.rmi.server.UniCastRemoteObject类，这样才能保证客户端访问获得远程对象时，该远程对象将会把自身
	的一个拷贝以Socket的形式传输给客户端，此时客户端所获得的这个拷贝称为“存根”，而服务器端本身已存在的远程对象则称之为“骨架”。其
	实此时的存根是客户端的一个代理，用于与服务器端的通信，而骨架也可认为是服务器端的一个代理，用于接收客户端的请求之后调用远程方法
	来响应客户端的请求。
	
	RMI 框架的基本原理大概如下图，应用了代理模式来封装了本地存根与真实的远程对象进行通信的细节。
	
	
          1.调用本地存根方法                  2.发送被包装后的调用参数                   3.调用服务器的方法
    客户端----------------->远程对象的存根| -------------------------> |远程对象的骨架<------------------->远程对象
          5.存根返回值或异常                  4.返回被包装后的值或异常
          
         
      
     
## 编写和使用
  
  	1.编写远程接口（需要继承java.rmi.Remote接口）
  	2.编写远程接口实现（继承UnicastRemoteObject，并实现1所写的远程接口）
  	3.编写远程服务端启动和注册类，将远程对象注册到RMI注册服务器
  	4.启动RMI注册器（这里有两种方法：在远程服务器代码中创建和启动（LocateRegistry.createRegistry(1099)），和在相应目录命令行启动> rmiregister）
  	5.编译和运行服务端代码，并在服务端生成客户端stub（例如：rmic HelloImpl），将生成的客户端stub（例如：HelloImpl_Stub.class）拷贝到客户端与服务端代码相同的目录下
  		|>javac HelloServer.java HelloImpl.java
	    |>rmic HelloImpl
	    |>java HelloServer
	    | 或
	    |>java -Djava.security.policy=$JAVA_HOME/jre/lib/security/policy.all HelloServer (事先编写了policy.all文件)
	    | 服务端指定了安全管理器 System.setSecurityManager(new RMISecurityManager());（客户端类似）
	    |
	    |>sudo vi policy.all
	    |   grant{
	    |       permission java.security.AllPermission "","";
	    |   };
	6.编写、编译、运行客户端代码 
	
	
	
## RMI局限性：

	1.RMI对服务器的IP地址和端口依赖很紧密，但是在开发的时候不知道将来的服务器IP和端口如何，但是客户端程序依赖这个IP和端口。
	  这个问题有两种解决途径：一是通过DNS来解决，二是通过封装将IP暴露到程序代码之外。
	2.RMI是Java语言的远程调用，两端的程序语言必须是Java实现，对于不同语言间的通讯可以考虑用Web Service或者公用对象请求
	  代理体系（CORBA）来实现。
	  
	  

  	