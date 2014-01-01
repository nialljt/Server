package main;

import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class messagingServer {

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
		try {
			startRegistry();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private static void startRegistry() throws RemoteException
	{
		try{
			Registry registry = LocateRegistry.getRegistry(1099);
			registry.list();
			// If a Registry does not already exist on port 1099, a RemoteException will be thrown
		}
		catch(RemoteException re)
		{
			System.out.println("RMI Registry cannot be located at port 1099");
			Registry registry = LocateRegistry.createRegistry(1099);
			System.out.println("RMI Registry created at port 1099");
		}
	}

}
