package io.katamari.handler;

import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;

import io.katamari.Env;

public class EnvInitializer extends ChannelInboundMessageHandlerAdapter<Object> {

  public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof HttpRequest) {
      if (ctx.nextInboundMessageBuffer().add(new Env(ctx, (DefaultFullHttpRequest) msg))) {
        ctx.fireInboundBufferUpdated();
      }
    }
  }

  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}