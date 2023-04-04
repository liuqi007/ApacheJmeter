package org.apache.jmeter.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

public class WebSocketServer {
    public void startSever() throws Exception {
        System.out.println("startSever");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)).option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 1024 *  1024 *10)
                    .childHandler(new ChannelInitializer(){

                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeLine = channel.pipeline();
                            pipeLine.addLast("http-codec", new HttpServerCodec());
                            pipeLine.addLast("http-chunked", new ChunkedWriteHandler());
                            pipeLine.addLast("aggregator", new HttpObjectAggregator(1024*1024*1024));
                            pipeLine.addLast(new WebSocketServerProtocolHandler("/logs", null, true, 65535));
                            pipeLine.addLast(new WebSocketHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(8899)).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
