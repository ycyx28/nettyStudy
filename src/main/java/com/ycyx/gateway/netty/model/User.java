package com.ycyx.gateway.netty.model;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 1819319333342834784L;
	
	private String userName;
	
	private String passWord;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	

}
