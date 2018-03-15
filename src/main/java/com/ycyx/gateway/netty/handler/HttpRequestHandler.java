package com.ycyx.gateway.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import java.net.URLDecoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ycyx.gateway.netty.constant.Channels;
import com.ycyx.gateway.netty.constant.SessionConstant;
import com.ycyx.gateway.netty.model.Session;

/**
 * 1.扩展 SimpleChannelInboundHandler 用于处理 FullHttpRequest信息
 */
@SuppressWarnings("deprecation")
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> { // 1

	private static Log log = LogFactory.getLog(HttpRequestHandler.class);

	private WebSocketServerHandshaker handshaker;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		Channel incoming = ctx.channel();
		// 如果HTTP解码失败，返回HHTP异常
		if (!request.getDecoderResult().isSuccess() || (!"websocket".equals(request.headers().get("Upgrade")))) {
			sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}
		
		// 正常WebSocket的Http连接请求，构造握手响应返回,
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://netty.uri" + request.headers().get(HttpHeaders.Names.HOST), null, false);
		handshaker = wsFactory.newHandshaker(request);
		if (handshaker == null) { // 无法处理的websocket版本
			WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
		} else { // 向客户端发送websocket握手,完成握手
			handshaker.handshake(ctx.channel(), request);
			String uri = URLDecoder.decode(request.getUri().replace("/", ""), "UTF-8");
			Session session = SessionConstant.sessionMap.get(uri);
			System.out.println("uri===>" + uri + " === session ====>" + session);
			if(null == session){
				ctx.close();
				return;
			}
			String remoteAddress = incoming.remoteAddress().toString().replace("/", "");
			String  ip = remoteAddress.split(":")[0];
			String  port = remoteAddress.split(":")[1];
			session.setPort(port);
			session.setIpAddres(ip);
			session.setChannel(incoming);
			Channels.setSession(session);
			Channels.addChannel(incoming);
			for(Channel channel :Channels.getChannelGroup() ){
				if(incoming != channel){
					channel.writeAndFlush(new TextWebSocketFrame("["+session.getUserName()+" ("+remoteAddress+") ] - 加入"));
				}else {
					channel.writeAndFlush(new TextWebSocketFrame("[you ("+remoteAddress+") ] - 加入"));
				}
			}
			
		}

	}

	/**
	 * Http返回
	 * 
	 * @param ctx
	 * @param request
	 * @param response
	 */
	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
		// 返回应答给客户端
		if (response.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(response.getStatus().toString(), CharsetUtil.UTF_8);
			response.content().writeBytes(buf);
			buf.release();
			HttpHeaders.setContentLength(response, response.content().readableBytes());
		}

		// 如果是非Keep-Alive，关闭连接
		ChannelFuture f = ctx.channel().writeAndFlush(response);
		if (!HttpHeaders.isKeepAlive(request) || response.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("WebSocket异常", cause);
		ctx.close();
	}

}
