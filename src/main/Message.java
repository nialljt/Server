package main;

import java.io.Serializable;

import interfaces.MessageInterface;

public class Message implements MessageInterface, Serializable{

	String destination;
	String data;
	String sender;
	
	public Message(){
		super();
	}
	public void createMessage(String destination, String data, String sender){
		this.destination = destination;
		this.data = data;
		this.sender = sender;
	}
	public String getDestination() {
		return destination;
	}
	public String getData() {
		return data;
	}
	public String getSender() {
		return sender;
	}
	
}
