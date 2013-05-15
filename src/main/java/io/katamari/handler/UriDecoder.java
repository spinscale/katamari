package io.katamari.handler;

import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerUtil;
import io.netty.handler.codec.http.QueryStringDecoder;

import io.katamari.Env;
import io.katamari.handler.InboundMessageHandler;

public class UriDecoder extends InboundMessageHandler {

  @Override
  public String getName() {
    return "decode-uri";
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, Env env) throws Exception {
    QueryStringDecoder decoder = new QueryStringDecoder(env.getRequest().getUri());
    env.getRequest().setPath(decoder.path());

    for (Map.Entry<String, List<String>> entry: decoder.parameters().entrySet()) {
      env.getRequest().setParam(entry.getKey(), entry.getValue().get(0));
    }

    nextHandler(ctx, env);
  }
}