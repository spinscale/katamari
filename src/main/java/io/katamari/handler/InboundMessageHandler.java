package io.katamari.handler;

import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerUtil;

import io.katamari.Env;

public class InboundMessageHandler extends ChannelInboundMessageHandlerAdapter<Env> {
  @Override
  public void messageReceived(ChannelHandlerContext ctx, Env env) throws Exception {
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