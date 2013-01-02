package io.katamari.env;

import java.util.Map;
import java.util.HashMap;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.channel.MessageEvent;

public class Request {
  private final HttpRequest request;
  private final Map<String,String> params = new HashMap<String,String>();
  private String path;

  public Request(MessageEvent e) {
    this.request = (HttpRequest)e.getMessage();
  }

  public HttpMethod method() {
    return request.getMethod();
  }

  public String uri() {
    return request.getUri();
  }

  public String path() {
    return path;
  }

  public void path(String path) {
    this.path = path;
  }

  public Map<String,String> params() {
    return params;
  }

  public Object params(String key) {
    return params.get(key);
  }

  public void params(String key, String value) {
    params.put(key, value);
  }

  public HttpRequest request() {
    return request;
  }
}