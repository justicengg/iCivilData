package com.novery.model;

public class NovWarnPoint {
	//warnClientID, warnObjectID,warnObjectName,warnCnt,warnUpdated
	
	private String warnObjectID;
	private String warnClientID;
	private String warnObjectName;
	private int warnCnt;
	private String warnUpdated;
	
	public void setWarnObjectID(String warnObjectID) {
		this.warnObjectID = warnObjectID;
	}
	public String getWarnObjectID() {
		return warnObjectID;
	}
	public String getWarnClientID() {
		return warnClientID;
	}
	public void setWarnClientID(String warnClientID) {
		this.warnClientID = warnClientID;
	}
	public String getWarnObjectName() {
		return warnObjectName;
	}
	public void setWarnObjectName(String warnObjectName) {
		this.warnObjectName = warnObjectName;
	}
	public int getWarnCnt() {
		return warnCnt;
	}
	public void setWarnCnt(int warnCnt) {
		this.warnCnt = warnCnt;
	}
	public String getWarnUpdated() {
		return warnUpdated;
	}
	public void setWarnUpdated(String warnUpdated) {
		this.warnUpdated = warnUpdated;
	}

}
