package io.katamari.handler;

import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Iterator;
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

    QueryStringDecoder decoder = new QueryStringDecoder(env.request().uri());
    env.request().path(decoder.getPath());
    Map<String,List<String>> parameters = decoder.getParameters();
    Iterator it = parameters.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry)it.next();
      List<String> values = (List)entry.getValue();
      if (!values.isEmpty()) {
        env.request().params((String)entry.getKey(), (String)values.get(0));
      }
    }

    ctx.sendUpstream(e);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
    e.getCause().printStackTrace();
    e.getChannel().close();
  }
}