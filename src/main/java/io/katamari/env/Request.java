package io.katamari.env;

import java.util.Map;
import java.util.HashMap;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.channel.MessageEvent;

public class Request {
  private final HttpRequest request;
  private final Map<String, Object> params = new HashMap<String, Object>();

  public Request(MessageEvent e) {
    this.request = (HttpRequest)e.getMessage();
  }

  public HttpMethod method() {
    return request.getMethod();
  }

  public String uri() {
    return request.getUri();
  }

  public Map<String, Object> params() {
    return params;
  }

  public Object params(String key) {
    return params.get(key);
  }

  public void params(String key, Object value) {
    params.put(key, value);
  }
}