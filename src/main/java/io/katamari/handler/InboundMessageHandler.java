package io.katamari.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerUtil;

import io.katamari.Env;

@ChannelHandler.Sharable
public abstract class InboundMessageHandler extends ChannelInboundMessageHandlerAdapter<Env> {

  public abstract String getName();

  public void messageReceived(ChannelHandlerContext ctx, Env env) throws Exception {
    super.endMessageReceived(ctx);
    ctx.fireInboundBufferUpdated();
  }

  public boolean nextHandler(ChannelHandlerContext ctx, Env env) throws Exception {
    return ChannelHandlerUtil.addToNextInboundBuffer(ctx, env);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}