package com.ycyx.gateway.netty.model;

import io.netty.channel.Channel;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Session implements Serializable{

	private static final long serialVersionUID = 7061163955442713307L;
	
	/**
	 * session Id
	 */
	private String sessionId;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * ip
	 */
	private String ipAddres;
	
	/**
	 * 端口
	 */
	private String port;
	
	/**
	 * 连接
	 */
	private Channel channel;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIpAddres() {
		return ipAddres;
	}

	public void setIpAddres(String ipAddres) {
		this.ipAddres = ipAddres;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
