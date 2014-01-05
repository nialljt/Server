package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageInterface extends Remote{
	
	public void createMessage(String destination, String data, String sender) throws RemoteException;
}
