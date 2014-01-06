package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessagingInterface extends Remote{

	public boolean login(String userName, String password) throws RemoteException;
	public void createMessage(String destination, String data, String sender) throws RemoteException;
	public void printQue() throws RemoteException;
	
	
	public void tmpRegisterWithServer(ReturnInterface clientObj) throws RemoteException;
	public void tmpunRegisterWithServer(ReturnInterface clientObj) throws RemoteException;
	
	public void newRegisterWithServer(ReturnInterface clientObj, String user) throws RemoteException;
	public void newUnregisterWithServer(String user) throws RemoteException;
}
