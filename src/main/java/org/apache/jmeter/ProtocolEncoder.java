package org.apache.jmeter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class ProtocolEncoder extends MessageToByteEncoder<ProtocolBean> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtocolBean msg, ByteBuf out) throws Exception {
        if (msg == null) {
            throw new Exception("msg is null");
        }

        out.writeByte(msg.getType());
        out.writeByte(msg.getFlag());
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getContent().getBytes(StandardCharsets.UTF_8));
    }
}
