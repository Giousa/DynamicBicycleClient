package com.km1930.dynamicbicycleclient.client;

import android.util.Log;

import com.km1930.dynamicbicycleclient.code.MsgPackDecode;
import com.km1930.dynamicbicycleclient.code.MsgPackEncode;
import com.km1930.dynamicbicycleclient.handler.ClientHandler;
import com.km1930.dynamicbicycleclient.model.DeviceValue;
import com.km1930.dynamicbicycleclient.utils.SharedPreferencesUtil;
import com.km1930.dynamicbicycleclient.utils.UIUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Description:
 * Author:Giousa
 * Date:2017/2/9
 * Email:65489469@qq.com
 */
public class Client {

    private NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
    private Channel channel;
    private Bootstrap bootstrap;
    private String hostIp = "127.0.0.1";
    private final static String SP_NAME = "CONFIG_IP";

    public Client() {
    }

    public void sendData(DeviceValue deviceValue) throws Exception {
        if (channel != null && channel.isActive()) {

            channel.writeAndFlush(deviceValue);
        }
    }

    public void start() {
        try {
            bootstrap = new Bootstrap();
            bootstrap
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new IdleStateHandler(0, 0, 5));
                            p.addLast(new MsgPackDecode());
                            p.addLast(new MsgPackEncode());
                            p.addLast(new ClientHandler(Client.this));
                        }
                    });
            doConnect();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }

        String string = SharedPreferencesUtil.getString(UIUtils.getContext(), SP_NAME, "");
        if(string != null){
            hostIp = string;
        }

        ChannelFuture future = bootstrap.connect(hostIp, 12345);

        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture futureListener) throws Exception {
                if (futureListener.isSuccess()) {
                    channel = futureListener.channel();
                    System.out.println("Connect to server successfully!");
                } else {
                    System.out.println("Failed to connect to server, try connect after 10s");
                    achieveHostIP();
                    futureListener.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    }, 10, TimeUnit.SECONDS);
                }
            }
        });
    }

    private void achieveHostIP() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int port = 9999;
                DatagramSocket ds = null;
                DatagramPacket dp = null;
                byte[] buf = new byte[1024];
                StringBuffer sbuf = new StringBuffer();
                try {
                    ds = new DatagramSocket(port);
                    dp = new DatagramPacket(buf, buf.length);
                    System.out.println("监听广播端口打开：");
                    ds.receive(dp);
                    ds.close();
                    int i;
                    for(i=0;i<1024;i++){
                        if(buf[i] == 0){
                            break;
                        }
                        sbuf.append((char) buf[i]);
                    }

                    if(sbuf != null){
                        String mConfigServerIP = sbuf.toString();
                        System.out.println("收到广播: "+mConfigServerIP);
                        if(!mConfigServerIP.equals(hostIp) && !mConfigServerIP.isEmpty()){
                            SharedPreferencesUtil.saveString(UIUtils.getContext(),SP_NAME,mConfigServerIP);
                        }
                        hostIp = mConfigServerIP;
                    }

                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
