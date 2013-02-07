package io.katamari.handler;

import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.ExceptionEvent;

import io.katamari.Env;
import io.katamari.env.Request;

public class EnvInitializer extends SimpleChannelUpstreamHandler {
  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    if (e.getMessage() instanceof Request) {
      Channels.fireMessageReceived(ctx, new Env(e));
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
    e.getCause().printStackTrace();
    e.getChannel().close();
  }
}