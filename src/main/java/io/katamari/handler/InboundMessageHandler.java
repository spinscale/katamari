package io.katamari.handler;

import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import io.katamari.Env;

public class InboundMessageHandler extends ChannelInboundMessageHandlerAdapter<Env> {
  @Override
  public void messageReceived(ChannelHandlerContext ctx, Env env) throws Exception {
    ctx.nextInboundMessageBuffer();
  }

  @Override
  public void endMessageReceived(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }
}