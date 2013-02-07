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

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Map<String,String> getParams() {
    return params;
  }

  public Object getParam(String key) {
    return params.get(key);
  }

  public void setParam(String key, String value) {
    params.put(key, value);
  }
}