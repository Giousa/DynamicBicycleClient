package com.km1930.dynamicbicycleclient.common;

import com.km1930.dynamicbicycleclient.model.DeviceValue;
import com.km1930.dynamicbicycleclient.model.TypeData;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Description:
 * Author:Giousa
 * Date:2017/2/9
 * Email:65489469@qq.com
 */
public abstract class CustomHeartbeatHandler extends ChannelInboundHandlerAdapter {

    protected String name;
    private int heartbeatCount = 0;

    public CustomHeartbeatHandler(String name) {
        this.name = name;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        List<DeviceValue> s = (List<DeviceValue>) msg;
        System.out.println("type="+s.get(0));
        if ("1".equals(s.get(0))) {
            sendPongMsg(ctx);
        } else if ("2".equals(s.get(0))){
            System.out.println(name + " get pong msg from " + ctx.channel().remoteAddress());
        } else {
            handleData(ctx, msg);
        }
    }


    protected void sendPingMsg(ChannelHandlerContext context) {
        DeviceValue s = new DeviceValue();
        s.setType(TypeData.PING);
        s.setSpeed(0);
        s.setAngle(15);
        s.setDeviceName("ping");
        context.channel().writeAndFlush(s);
        heartbeatCount++;
        System.out.println(name + " sent ping msg to " + context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

    private void sendPongMsg(ChannelHandlerContext context) {
        DeviceValue s = new DeviceValue();
        s.setType(TypeData.PONG);
        s.setSpeed(0);
        s.setAngle(15);
        s.setDeviceName("pong");
        context.channel().writeAndFlush(s);
        heartbeatCount++;
        System.out.println(name + " sent pong msg to " + context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

    protected abstract void handleData(ChannelHandlerContext channelHandlerContext, Object msg);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("---" + ctx.channel().remoteAddress() + " is active---");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("---" + ctx.channel().remoteAddress() + " is inactive---");
    }

    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        System.err.println("---READER_IDLE---");
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        System.err.println("---WRITER_IDLE---");
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        System.err.println("---ALL_IDLE---");
    }
}