package com.novery.stack;

import java.util.Stack;

public class NovUserInfo {

	public NovUserInfo() {
		//stackObject = new  Stack<NovRestObjectInfo>();
	}
	public NovRestLoginInfo getLogin() {
		return login;
	}
	public void setLogin(NovRestLoginInfo login) {
		this.login = login;
	}
	
	//private Stack<NovRestObjectInfo> stackObject ;
	private  NovRestLoginInfo login;
	private static NovUserInfo _instance;
	public static NovUserInfo getInstance(){
		if( null == _instance ){
			_instance = new NovUserInfo();
			_instance.login = new NovRestLoginInfo();
			
		}
		return _instance;
	}
	//public Stack<NovRestObjectInfo> getStackObject() {
	//	return stackObject;
	//}

}
