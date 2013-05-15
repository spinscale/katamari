package io.katamari.handler;

import static io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import static io.netty.handler.codec.http.HttpMethod.*;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.Attribute;

import io.katamari.Env;
import io.katamari.handler.InboundMessageHandler;

public class BodyDecoder extends InboundMessageHandler {

  @Override
  public String getName() {
    return "body";
  }

    @Override
  public void messageReceived(ChannelHandlerContext ctx, Env env) throws Exception {
    if (env.getRequest().getMethod() == POST || env.getRequest().getMethod() == PUT || env.getRequest().getMethod() == PATCH) {
      HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(env.getRequest());

      for (InterfaceHttpData entry: decoder.getBodyHttpDatas()) {
        if (entry.getHttpDataType() == HttpDataType.Attribute) {
          Attribute attribute = (Attribute)entry;
          env.getRequest().setParam((String)attribute.getName(), (String)attribute.getValue());
        }
      }
    }

    nextHandler(ctx, env);
  }
}