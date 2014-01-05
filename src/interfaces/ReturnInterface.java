package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReturnInterface extends Remote{
	void recieveMessage(String message, String sender) throws RemoteException;
}
