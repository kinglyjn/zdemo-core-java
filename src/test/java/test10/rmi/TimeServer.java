package test10.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 实现 RMI接口方法
 * 继承UnicastRemoteObject类，并实现刚定义的RMI接口
 * @author zhangqingli
 *
 */
public class TimeServer extends UnicastRemoteObject implements ITimeServer {
	private static final long serialVersionUID = 1L;

	protected TimeServer() throws RemoteException {
		super();
	}

	@Override
	public long getServerTime() throws RemoteException {
		return System.currentTimeMillis();
	}
}
