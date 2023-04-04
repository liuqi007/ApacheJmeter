package org.apache.jmeter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

public class ProtocolDecoder extends LengthFieldBasedFrameDecoder {

    private static final int HEADER_SIZE = 6;
    public ProtocolDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    public ProtocolDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        in = (ByteBuf) super.decode(ctx, in);
        if (in == null) {
            return null;
        }

        if (in.readableBytes() < HEADER_SIZE) {
            throw new Exception("字节数不足");
        }

        byte type = in.readByte();
        byte flag = in.readByte();
        int length = in.readInt();

        if (in.readableBytes()!=length) {
            throw new Exception("标记的长度不符合实际长度");
        }

        byte []bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);

        return new ProtocolBean(type, flag, length, new String(bytes, "UTF-8"));
    }
}
