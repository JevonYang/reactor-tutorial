package com.yang.tutorial.netty;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.concurrent.ThreadFactory;

/**
 * @author yangzijing
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

//    private final ThreadFactory threadFactory = new ThreadFactoryBuilder()
//            .setNameFormat("netty-context-business-%d")
//            .setDaemon(false)
//            .build();
//
//    private NioEventLoopGroup business = new NioEventLoopGroup(200, threadFactory);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast("httpServerCodec", new HttpServerCodec());
        channelPipeline.addLast("MiddlewareHandler", new MiddlewareHandler());
        channelPipeline.addLast("business", new HttpServerHandler());
    }
}
