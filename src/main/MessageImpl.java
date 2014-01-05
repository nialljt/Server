package main;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import interfaces.MessagingInterface;
import interfaces.ReturnInterface;

public class MessageImpl extends UnicastRemoteObject implements
		MessagingInterface {

	private Vector<ReturnInterface> tmpRegisteredClients;
	private Vector<ReturnInterface> registeredClients;

	public MessageImpl() throws RemoteException {
		super();
		registeredClients = new Vector<ReturnInterface>();
		tmpRegisteredClients = new Vector<ReturnInterface>();
	}

	@Override
	public void createMessage(String destination, String data, String sender)
			throws RemoteException {
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
	public boolean login(String userName, String password)
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
		System.out.println("Here");
		return false;
	}

	@Override
	public synchronized void registerWithServer(ReturnInterface clientObj,
			boolean login, String user) throws RemoteException {
		if (!login) {
			if (!tmpRegisteredClients.contains(clientObj)) {
				// Add the client to the list so they can get callback messages
				tmpRegisteredClients.add(clientObj);
				System.out.println("temp Registered client ");
			} else {
				System.out
						.println("tmp Register failed - client was already registered.");
			}
		} else {
			if (!registeredClients.contains(clientObj)) {
				// Add the client to the list so they can get callback messages
				registeredClients.add(clientObj);
				System.out.println("Registered client ");
			} else {
				System.out
						.println("Register failed - client was already registered.");
			}
			if (login = true) {
				for (int i = 0; i < MessagingServer.clients.allClients.size(); i++) {
					if (MessagingServer.clients.allClients.get(i).getUserName()
							.equals(user)) {
						MessagingServer.clients.allClients.get(i).setOnline(
								true);
						MessagingServer.clients.allClients.get(i).setLocation(
								registeredClients.size());
						break;
					}
				}
				updateOnlineList();
			}
		}

	}

	@Override
	public synchronized void unRegisterWithServerAndLogin(
			ReturnInterface clientObj, boolean logout, String user)
			throws RemoteException {

		if (!logout) {
			if (tmpRegisteredClients.contains(clientObj)) {

				// Remove the client from the list so they don't get any more
				// callback messages

				tmpRegisteredClients.remove(clientObj);
				System.out.println("Unregistered tmp client ");

			} else {
				System.out.println("tmp unregister: clientwasn't registered.");
			}
		} else {
			for (int i = 0; i < registeredClients.size(); i++) {

				if (MessagingServer.clients.allClients.get(i).getUserName()
						.equals(user)) {
					MessagingServer.clients.allClients.get(i).setOnline(false);
					MessagingServer.clients.allClients.get(i).setLocation(-1);
					registeredClients.removeElementAt(i);
					System.out.println("Unregistered client!!");
					break;
				}
			}
			updateOnlineList();
			
		}

	}

	public synchronized void sendMessage() {
		if (MessagingServer.msgStore.que.size() > 0) {
			Message tmpmsg = MessagingServer.msgStore.getMessageFromQue();
			if (tmpmsg.destination.equalsIgnoreCase("ALL")) {
				//for (int i = 0; i < MessagingServer.clients.allClients.size(); i++) {
				for(int i = 0; i < registeredClients.size();i++){
					if (i == 1
							&& MessagingServer.clients.allClients.size() == 1) {

					} else {
						ReturnInterface tmpClientObj = (ReturnInterface) registeredClients
								.get(i);
						try {
							tmpClientObj.recieveMessage(tmpmsg.data,
									tmpmsg.sender + "~~ALL~~");
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} else {
				for (int i = 0; i < MessagingServer.clients.allClients.size(); i++) {

					if (MessagingServer.clients.allClients.get(i).getUserName()
							.equals(tmpmsg.destination)) {
						ReturnInterface tmpClientObj = (ReturnInterface) registeredClients
								.get(i);
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
	
	private void updateOnlineList(){
		for(int i = 0; i < registeredClients.size();i++){
			ReturnInterface tmpClientObj = (ReturnInterface) registeredClients
					.get(i);
			try {
				tmpClientObj.updateOnlineList(getOnlineList());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Vector<String> getOnlineList(){
		Vector<String>whosOnline = new Vector<String>();
		for (int i = 0; i < MessagingServer.clients.allClients.size(); i++) {
			if(MessagingServer.clients.allClients.get(i).isOnline()){
				whosOnline.add(MessagingServer.clients.allClients.get(i).getUserName());
				System.out.println(MessagingServer.clients.allClients.get(i).getUserName());
			}
		}
		return whosOnline;
	}
}
