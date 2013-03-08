package io.katamari;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;

import io.katamari.env.Response;

public class Env {
  private final DefaultFullHttpRequest request;
  private final Response response;

  public Env(ChannelHandlerContext ctx, DefaultFullHttpRequest msg) {
    this.request = msg;
    this.response = new Response(ctx);
  }

  public DefaultFullHttpRequest getRequest() {
    return request;
  }

  public Response getResponse() {
    return response;
  }
}