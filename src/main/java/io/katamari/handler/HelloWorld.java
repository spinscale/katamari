package io.katamari.handler;

import io.netty.channel.ChannelHandlerContext;

import io.katamari.Env;
import io.katamari.handler.InboundMessageHandler;

public class HelloWorld extends InboundMessageHandler {
  @Override
  public void messageReceived(ChannelHandlerContext ctx, Env env) throws Exception {
    env.getResponse().end("Hello World");
  }

  @Override
  public void endMessageReceived(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }
}