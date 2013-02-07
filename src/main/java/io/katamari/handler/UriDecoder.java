package io.katamari.handler;

import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import io.katamari.Env;

public class UriDecoder extends SimpleChannelUpstreamHandler {
  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    Env env = (Env)e.getMessage();

    QueryStringDecoder decoder = new QueryStringDecoder(env.request().getUri());
    env.request().path(decoder.getPath());

    for (Map.Entry<String, List<String>> entry: decoder.getParameters().entrySet()) {
      env.request().params((String)entry.getKey(), (String)entry.getValue().get(0));
    }

    ctx.sendUpstream(e);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
    e.getCause().printStackTrace();
    e.getChannel().close();
  }
}