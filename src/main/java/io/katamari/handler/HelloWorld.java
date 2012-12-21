package io.katamari.handler;

import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import io.katamari.Env;

public class HelloWorld extends SimpleChannelUpstreamHandler {
  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    Env env = (Env)e.getMessage();
    env.response().end("Hello World");
  }
}