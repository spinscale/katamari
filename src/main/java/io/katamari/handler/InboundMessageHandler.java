package io.katamari.handler;

import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import io.katamari.Env;

public class InboundMessageHandler extends ChannelInboundMessageHandlerAdapter<Env> {
  @Override
  public void messageReceived(ChannelHandlerContext ctx, Env env) throws Exception {
    ctx.fireInboundBufferUpdated();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}