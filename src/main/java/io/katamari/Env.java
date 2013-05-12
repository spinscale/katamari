package io.katamari;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;

import io.katamari.env.Request;
import io.katamari.env.Response;

public class Env {
  private final Request request;
  private final Response response;

  public Env(ChannelHandlerContext ctx, DefaultFullHttpRequest msg) {
    this.request = new Request(msg.getProtocolVersion(), msg.getMethod(), msg.getUri(), msg.data());
    this.request.headers().add(msg.headers());
    this.response = new Response(ctx);
  }

  public Request getRequest() {
    return request;
  }

  public Response getResponse() {
    return response;
  }
}