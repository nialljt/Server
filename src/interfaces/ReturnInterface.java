package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface ReturnInterface extends Remote{
	void recieveMessage(String message, String sender) throws RemoteException;
	void updateOnlineList(Vector<String> whosOnline) throws RemoteException;
}
