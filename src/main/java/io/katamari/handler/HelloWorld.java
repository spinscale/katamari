package io.katamari.handler;

import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import io.katamari.Env;

public class HelloWorld extends ChannelInboundMessageHandlerAdapter<Env> {
  @Override
  public void messageReceived(ChannelHandlerContext ctx, Env env) throws Exception {
    env.getResponse().end("Hello World");
  }

  @Override
  public void endMessageReceived(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }
}