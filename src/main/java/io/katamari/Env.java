package io.katamari;

import io.katamari.env.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;

import io.katamari.env.Request;
import io.katamari.env.Response;

public class Env {
  private final Request request;
  private final Response response;
  private Session session;

  public Env(ChannelHandlerContext ctx, DefaultFullHttpRequest msg) {
    this.request = new Request(msg.getProtocolVersion(), msg.getMethod(), msg.getUri(), msg.content());
    this.request.headers().add(msg.headers());
    this.response = new Response(ctx);
    this.session = new Session();
  }

  public Session getSession() {
    return session;
  }
  
  public Request getRequest() {
    return request;
  }

  public Response getResponse() {
    return response;
  }
}