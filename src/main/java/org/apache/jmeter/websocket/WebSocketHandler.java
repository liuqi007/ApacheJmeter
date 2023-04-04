package org.apache.jmeter.websocket;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.jmeter.NewDriver;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // text msg
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame req = (TextWebSocketFrame) msg;
            String[] params = req.text().split("&");
            if(params==null ||params.length<3){
                ctx.writeAndFlush(new TextWebSocketFrame("param is error"));
            }

            long size = Integer.parseInt(params[1]);
            long page = Integer.parseInt(params[2]);

            StringBuilder sb = new StringBuilder();
            try{
                Path p = Paths.get(NewDriver.JMETER_INSTALLATION_DIRECTORY + File.separator + "logs" + File.separator +"JOB_"+params[0]+".log");
                System.out.println(p.toAbsolutePath());
                Stream<String> lines = Files.lines(p);
                long count = lines.count();
                lines.close();

                System.out.println("-----"+count);
                long begin = count > size * (page +1) ? (count - size * (page +1)):0;
                Stream<String> newLines = Files.lines(p).skip(begin);

                System.out.println("-------" + begin);
                newLines.limit(size).forEach(str->sb.append(str).append("\n"));


            }catch (Exception e) {
                System.out.println("no such file");
            }

            ctx.writeAndFlush(new TextWebSocketFrame(sb.toString()));
        }

        //binary msg
        if (msg instanceof BinaryWebSocketFrame) {
            System.out.println("recive binary msg: " + ((BinaryWebSocketFrame) msg).content().readableBytes());
            BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(Unpooled.buffer().writeBytes("hello".getBytes(StandardCharsets.UTF_8)));

            // send msg to client
            ctx.channel().writeAndFlush(binaryWebSocketFrame);
        }

        //ping msg
        if (msg instanceof PongWebSocketFrame) {
            System.out.println("client ping success");
        }

        //close msg
        if (msg instanceof CloseWebSocketFrame) {
            System.out.println("client close, channel close");
            Channel channel = ctx.channel();
            channel.close();
        }
    }
}
