package com.novery.stack;

public class NovRestLoginInfo {
	private String userID ="" ;
	private String userName ="";
	private String clientID ="";
	private String clientName = "";
	private Integer userGrade = 0;
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public Integer getUserGrade() {
		return userGrade;
	}
	public void setUserGrade(Integer userGrade) {
		this.userGrade = userGrade;
	}
}
