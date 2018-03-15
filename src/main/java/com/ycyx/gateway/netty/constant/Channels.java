package com.ycyx.gateway.netty.constant;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;

import com.ycyx.gateway.netty.model.Session;

public class Channels {
	
	private static HashMap<String, Session> sessionMap = new HashMap<String, Session>();
	
	private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	public static void setSession(Session session){
		sessionMap.put(session.getIpAddres()+":"+session.getPort(), session);
	}
	
	public static Session getSession(String remoteAddress){
		return sessionMap.get(remoteAddress);
	}
	
	public static HashMap<String, Session> getSessionMap(){
		return sessionMap;
	}
	
	public static ChannelGroup getChannelGroup(){
		return channels;
	}
	
	public static void addChannel(Channel channel){
		channels.add(channel);
	}

}
