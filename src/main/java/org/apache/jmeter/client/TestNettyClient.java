package org.apache.jmeter.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.jmeter.ProtocolBean;
import org.apache.jmeter.ProtocolDecoder;
import org.apache.jmeter.ProtocolEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class TestNettyClient extends Thread {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress("127.0.0.1", 15625))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ProtocolEncoder());
                            socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                                String cmd = "version:";

                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

                                }

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    byte[] bytes = cmd.getBytes(StandardCharsets.UTF_8);
                                    ProtocolBean protocolBean = new ProtocolBean((byte) 0xA, (byte) 0xC, bytes.length, cmd);
                                    ctx.writeAndFlush(protocolBean);
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    cause.printStackTrace();;
                                    ctx.close();
                                }
                            });

                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {

        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
