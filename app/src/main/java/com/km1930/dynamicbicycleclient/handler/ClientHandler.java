package com.km1930.dynamicbicycleclient.handler;

import com.km1930.dynamicbicycleclient.client.Client;
import com.km1930.dynamicbicycleclient.common.CustomHeartbeatHandler;

import io.netty.channel.ChannelHandlerContext;

/**
 * Description:
 * Author:Giousa
 * Date:2017/2/9
 * Email:65489469@qq.com
 */
public class ClientHandler extends CustomHeartbeatHandler {
    private Client client;
    public ClientHandler(Client client) {
        super("client");
        this.client = client;
    }


    @Override
    protected void handleData(ChannelHandlerContext channelHandlerContext, Object msg) {
        System.out.println(name+"  handleData:"+msg);
    }

    @Override
    protected void handleAllIdle(ChannelHandlerContext ctx) {
        super.handleAllIdle(ctx);
        sendPingMsg(ctx);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        client.doConnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(name + " exception"+cause.toString());

    }
}