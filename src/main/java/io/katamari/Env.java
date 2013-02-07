package io.katamari;

import org.jboss.netty.channel.MessageEvent;

import io.katamari.env.Request;
import io.katamari.env.Response;

public class Env {
  private final Request request;
  private final Response response;

  public Env(MessageEvent e) {
    this.request = (Request) e.getMessage();
    this.response = new Response(e);
  }

  public Request getRequest() {
    return request;
  }

  public Response getResponse() {
    return response;
  }
}