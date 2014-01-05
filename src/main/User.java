package main;

public class User {

	String userName;
	String password;
	boolean online;
	int location;
	
	public User(String userName, String password){
		this.userName = userName;
		this.password = password;
		this.online = false;
		location = -1;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	public void setLocation(int i){
		this.location = i;
	}
	public int getLocation(int i){
		return location;
	}
}
