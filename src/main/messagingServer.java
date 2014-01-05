package main;

import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class MessagingServer {

	public static MessageStore msgStore;
	public static Clients clients;
	public static void main(String[] args) {
		 msgStore = new MessageStore();
		 clients = new Clients();
		 clients.addToClients("niall", "test");
		 clients.addToClients("mark", "test");
		 clients.addToClients("barry", "test");
		String registryUrl = "rmi://localhost:1109/messaging";
		try {
			startRegistry();
			MessageImpl exportedObj = new MessageImpl();
			Naming.rebind(registryUrl, exportedObj);
			while(true){
				exportedObj.sendMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static void startRegistry() throws RemoteException{
		try{
			Registry registry = LocateRegistry.getRegistry(1109);
			registry.list();
		}catch(RemoteException e){
			Registry registry = LocateRegistry.createRegistry(1109);
		}
		
	}
	//RUN THREADS HERE AKA CREATE THREAD CLASS TO SEND MESSAGES FROM VECTOR OF MESSAGES DEPENDING ON DESTINATION

}
