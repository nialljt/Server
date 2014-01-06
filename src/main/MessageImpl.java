package main;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.text.html.HTMLDocument.Iterator;

import interfaces.MessagingInterface;
import interfaces.ReturnInterface;

public class MessageImpl extends UnicastRemoteObject implements
		MessagingInterface {

	private Vector<ReturnInterface> tmpRegisteredClients;
	private Vector<ReturnInterface> registeredClients;

	private HashMap hm = new HashMap();

	public MessageImpl() throws RemoteException {
		super();
		registeredClients = new Vector<ReturnInterface>();
		tmpRegisteredClients = new Vector<ReturnInterface>();

	}

	@Override
	public synchronized void createMessage(String destination, String data,
			String sender) throws RemoteException {
		Message msg = new Message();
		msg.createMessage(destination, data, sender);
		MessagingServer.msgStore.addToQue(msg);
	}

	@Override
	public void printQue() {
		while (MessagingServer.msgStore.que.size() > 0) {
			Message m = MessagingServer.msgStore.getMessageFromQue();
			System.out.println("To: " + m.destination + "\nFrom: " + m.sender
					+ "\nContent: " + m.data + "\n\n");
			System.out.println(MessagingServer.msgStore.que.size());
		}
		printOtherUsersON();
	}

	public void printOtherUsersON() {
		InetAddress address = null;
		try {
			address = InetAddress.getByName("GSwizil");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(address.getHostName() + " = "
				+ address.getHostAddress());
	}

	@Override
	public synchronized boolean login(String userName, String password)
			throws RemoteException {
		for (int i = 0; i < MessagingServer.clients.allClients.size(); i++) {
			System.out.println("tmp");
			if (MessagingServer.clients.allClients.get(i).getUserName()
					.equals(userName)) {
				System.out.println("tmp1");
				if (MessagingServer.clients.allClients.get(i).getPassword()
						.equals(password)) {
					return true;
				}
				break;
			}
		}
		return false;
	}

	@Override
	public synchronized void tmpRegisterWithServer(ReturnInterface clientObj)
			throws RemoteException {

		if (!tmpRegisteredClients.contains(clientObj)) {
			tmpRegisteredClients.add(clientObj);
			System.out.println("temp Registered client ");
		} else {
			System.out
					.println("tmp Register failed - client was already registered.");
		}
	}

	@Override
	public synchronized void tmpunRegisterWithServer(ReturnInterface clientObj)
			throws RemoteException {
		if (tmpRegisteredClients.contains(clientObj)) {
			// Remove the client from the list so they don't get any more
			// callback messages
			tmpRegisteredClients.remove(clientObj);
			System.out.println("Unregistered tmp client ");
		} else {
			System.out.println("tmp unregister: clientwasn't registered.");
		}
	}

	public synchronized void sendMessage() {
		if (MessagingServer.msgStore.que.size() > 0) {
			Message tmpmsg = MessagingServer.msgStore.getMessageFromQue();
			if (tmpmsg.destination.equalsIgnoreCase("ALL")) {
				for (Object obj : hm.keySet()) {
					String key = obj.toString();
					ReturnInterface tmpInter = (ReturnInterface) hm.get(key);
					try {
						tmpInter.recieveMessage(tmpmsg.data, tmpmsg.sender
								+ "~~ALL~~");
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				for (Object obj : hm.keySet()) {
					String key = obj.toString();
					if (key.equals(tmpmsg.destination)) {
						ReturnInterface tmpClientObj = (ReturnInterface) hm
								.get(key);
						try {
							tmpClientObj.recieveMessage(tmpmsg.data,
									tmpmsg.sender);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}

	}

	private synchronized void updateOnlineList() {
		Vector<String> tmpVec = new Vector<String>();
		for (Object obj : hm.keySet()) {
			String key = obj.toString();
			tmpVec.add(key);
		}
		for (Object obj : hm.keySet()) {
			String key = obj.toString();
			ReturnInterface tmpInter = (ReturnInterface) hm.get(key);
			try {
				tmpInter.updateOnlineList(tmpVec);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	

	public synchronized void newUnregisterWithServer(String user)
			throws RemoteException {
		hm.remove(user);
		createMessage("ALL", "Logged off!", user);
		updateOnlineList();
	}

	public synchronized void newRegisterWithServer(ReturnInterface clientObj,
			String user) throws RemoteException {
		hm.put(user, clientObj);
		createMessage("ALL", "Logged on!", user);
		updateOnlineList();
	}

	

}
