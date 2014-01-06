package main;

import interfaces.ReturnInterface;

import java.util.Vector;

public class Clients {

	public Vector<User> allClients;

	public Clients(){
		allClients = new Vector<User>();

	}
	public void addToClients(String userName, String password){
		allClients.add(new User(userName,password));
	}
				
}
