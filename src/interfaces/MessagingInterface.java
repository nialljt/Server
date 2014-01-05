package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import main.Message;


public interface MessagingInterface extends Remote{

	public boolean login(String userName, String password) throws RemoteException;
	public void createMessage(String destination, String data, String sender) throws RemoteException;
	public void printQue() throws RemoteException;
	
	public void registerWithServer(ReturnInterface clientObj, boolean login, String user) throws RemoteException;
	public void unRegisterWithServerAndLogin(ReturnInterface clientObj, boolean logOut, String user) throws RemoteException;
}
