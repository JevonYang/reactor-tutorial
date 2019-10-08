package com.yang.tutorial.netty;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.*;
import io.netty.handler.codec.http.HttpObject;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author yangzijing
 */
@Slf4j
@ChannelHandler.Sharable
public class MiddlewareHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        log.info("MiddlewareHandler接收到消息");
        ctx.fireChannelRead(msg);
        ctx.fireChannelActive();
    }
}
