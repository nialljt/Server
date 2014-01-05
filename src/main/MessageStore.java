package main;

import java.io.Serializable;
import java.util.Vector;

public class MessageStore implements Serializable{

	Vector<Message> que, undelivered;
	
	public MessageStore(){
		this.que = new Vector<Message>(5);
		this.undelivered = new Vector<Message>(5);
	}
	
	public void addToQue(Message msg){
		que.add(msg);
	}
	public Message getMessageFromQue(){
		Message tmpmsg = que.elementAt(0);
		que.remove(0);
		return tmpmsg;
	}
	
	public void addToUndelivered(Message msg){
		undelivered.add(msg);
	}
}
