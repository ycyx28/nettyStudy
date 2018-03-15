package com.ycyx.gateway.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 1.扩展 ChannelInitializer
 */
public class WebsocketChatServerInitializer extends ChannelInitializer<SocketChannel> { // 1

	/**
	 * 2.添加 ChannelHandler　到 ChannelPipeline
	 */
	@Override
	public void initChannel(SocketChannel ch) throws Exception {// 2
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast("server-codec", new HttpServerCodec());
		pipeline.addLast("http-aggregator", new HttpObjectAggregator(64 * 1024));
		pipeline.addLast("cunk-writer", new ChunkedWriteHandler());
		pipeline.addLast("http-request",new HttpRequestHandler());
		pipeline.addLast("socket-frame",new TextWebSocketFrameHandler());

	}
}