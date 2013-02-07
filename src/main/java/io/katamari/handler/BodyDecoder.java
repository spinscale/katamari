package io.katamari.handler;

import static org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import static org.jboss.netty.handler.codec.http.HttpMethod.*;

import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.jboss.netty.handler.codec.http.multipart.Attribute;

import io.katamari.Env;

public class BodyDecoder extends SimpleChannelUpstreamHandler {
  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    Env env = (Env)e.getMessage();

    if (env.getRequest().getMethod() == POST || env.getRequest().getMethod() == PUT || env.getRequest().getMethod() == PATCH) {
      HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(env.getRequest());

      for (InterfaceHttpData entry: decoder.getBodyHttpDatas()) {
        if (entry.getHttpDataType() == HttpDataType.Attribute) {
          Attribute attribute = (Attribute)entry;
          env.getRequest().setParam((String)attribute.getName(), (String)attribute.getValue());
        }
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