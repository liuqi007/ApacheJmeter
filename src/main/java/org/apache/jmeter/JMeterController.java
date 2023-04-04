package org.apache.jmeter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class JMeterController extends SimpleChannelInboundHandler<Object> {

    NewDynamicClassloader loader = null;


    public static Object jmeter = null;

    public JMeterController(NewDynamicClassloader loader) {
        this.loader = loader;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        ProtocolBean protocolBean = (ProtocolBean) msg;

        String cmd = protocolBean.getContent();
        System.out.println("cmd:"+cmd);

        if(cmd.startsWith("start:") && jmeter == null) {
            StartJmeter sj = new StartJmeter(loader, cmd.substring("start:".length()));
            jmeter = sj.getInstance();
            sj.start();
        }

        if(cmd.startsWith("downloadjars:")) {
            new DownLoadPlugins(cmd.substring("downloadjars:".length()));
        }
    }
}
