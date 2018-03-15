package com.ycyx.gateway.netty.listener;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

import com.ycyx.gateway.netty.handler.WebsocketChatServer;

public class NettyContextLoaderListener extends ContextLoaderListener{

	@Override
	public void contextInitialized(ServletContextEvent event) {
		initWebApplicationContext(event.getServletContext());
		try {
			new WebsocketChatServer(8088).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
