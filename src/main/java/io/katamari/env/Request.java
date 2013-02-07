package io.katamari.env;

import java.util.Map;
import java.util.HashMap;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpVersion;

public class Request extends DefaultHttpRequest {
  private final Map<String,String> params = new HashMap<String,String>();
  private String path;

  public Request(HttpVersion httpVersion, HttpMethod method, String uri) {
    super(httpVersion, method, uri);
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
}