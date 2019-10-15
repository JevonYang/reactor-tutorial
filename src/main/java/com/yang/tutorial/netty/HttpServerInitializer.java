package com.yang.tutorial.netty;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.concurrent.ThreadFactory;

/**
 * @author yangzijing
 */
@Slf4j
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
        channelPipeline.addLast("myInboundHandler", new MyInboundHandler());
        channelPipeline.addLast("myOutboundHandler", new MyOutboundHandler());
        channelPipeline.addLast("business", new HttpServerHandler());
    }

    private class MyInboundHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            log.info("Inbound channelActive ==========================>");
            super.channelActive(ctx);
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            log.info("Inbound channelRegistered ==========================>");
            super.channelRegistered(ctx);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            log.info("Inbound channelUnregistered ==========================>");
            super.channelUnregistered(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            log.info("Inbound channelInactive ==========================>");
            super.channelInactive(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            log.info("Inbound channelRead ==========================>");
            super.channelRead(ctx, msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            log.info("Inbound channelReadComplete ==========================>");
            super.channelReadComplete(ctx);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            log.info("Inbound userEventTriggered ==========================>");
            super.userEventTriggered(ctx, evt);
        }

        @Override
        public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
            log.info("Inbound channelWritabilityChanged ==========================>");
            super.channelWritabilityChanged(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            log.info("Inbound exceptionCaught ==========================>");
            super.exceptionCaught(ctx, cause);
        }
    }

    private class MyOutboundHandler extends ChannelOutboundHandlerAdapter {
        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            log.info("<========================== Outbound close");
            super.close(ctx, promise);
        }

        @Override
        public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
            log.info("<========================== Outbound bind");
            super.bind(ctx, localAddress, promise);
        }

        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
            log.info("<========================== Outbound connect");
            super.connect(ctx, remoteAddress, localAddress, promise);
        }

        @Override
        public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            log.info("<========================== Outbound disconnect");
            super.disconnect(ctx, promise);
        }

        @Override
        public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            log.info("<========================== Outbound deregister");
            super.deregister(ctx, promise);
        }

        @Override
        public void read(ChannelHandlerContext ctx) throws Exception {
            log.info("<========================== Outbound read");
            super.read(ctx);
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            log.info("<========================== Outbound write");
            super.write(ctx, msg, promise);
        }

        @Override
        public void flush(ChannelHandlerContext ctx) throws Exception {
            log.info("<========================== Outbound flush");
            super.flush(ctx);
        }
    }

}
