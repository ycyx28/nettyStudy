package com.ycyx.gateway.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import com.ycyx.gateway.netty.constant.Channels;
import com.ycyx.gateway.netty.model.Session;

/**
 * 处理TextWebSocketFrame
 * 
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception { // (1)
		Channel incoming = ctx.channel();
		String remoteAddress = incoming.remoteAddress().toString().replace("/", "");
		Session session = Channels.getSession(remoteAddress);
		for (Channel channel : Channels.getChannelGroup()) {
			if (channel != incoming) {
				channel.writeAndFlush(new TextWebSocketFrame("["+session.getUserName()+" ("+remoteAddress+") ] - " + msg.text()));
			} else {
				incoming.writeAndFlush(new TextWebSocketFrame("[ you ("+remoteAddress+") ] - " + msg.text()));
			}
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception { // (2)
		Channel incoming = ctx.channel();
		String remoteAddress = incoming.remoteAddress().toString().replace("/", "");
//		Channels.addChannel(incoming);
		System.out.println("Client:" + remoteAddress + "加入");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
		Channel incoming = ctx.channel();
		String remoteAddress = incoming.remoteAddress().toString().replace("/", "");
		Session session = Channels.getSession(remoteAddress);
		Channels.getChannelGroup().writeAndFlush(new TextWebSocketFrame("["+session.getUserName()+" ("+remoteAddress+") ] - 离开"));
		System.out.println("Client:" + incoming.remoteAddress() + "离开");

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
		Channel incoming = ctx.channel();
		System.out.println("Client:" + incoming.remoteAddress() + "在线");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
		Channel incoming = ctx.channel();
		System.out.println("Client:" + incoming.remoteAddress() + "掉线");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println("Client:" + incoming.remoteAddress() + "异常");
		// 当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}
}
