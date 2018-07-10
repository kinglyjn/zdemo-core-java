package test10.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 定义 RMI接口方法
 * 继承Remote接口，并抛出RemoteException异常
 * @author zhangqingli
 *
 */
public interface ITimeServer extends Remote {
	long getServerTime() throws RemoteException;
}
