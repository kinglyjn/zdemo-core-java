package test10.rmi;

import java.rmi.Naming;
/**
 * 这是RMI服务客户端
 * 拷贝服务端的功能类接口（ITimeSevrer）和服务端生成的XXX_Stub.class到客户端对应的目录下，就可以直接调用服务端暴露的RMI接口
 * @author zhangqingli
 *
 */
public class TimeClient1 {

    public static void main(String[] args) throws Exception {
        ITimeServer ts1 = (ITimeServer) Naming.lookup("rmi://keyllopcm:1099/ts1");
        System.out.println("server time is: " + ts1.getServerTime()); 
    }
}