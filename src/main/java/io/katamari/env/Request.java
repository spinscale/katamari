package io.katamari.env;

import java.util.Map;
import java.util.HashMap;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.buffer.ByteBuf;

public class Request extends DefaultFullHttpRequest {
  private final Map<String,String> params = new HashMap<String,String>();
  private String path;

  public Request(HttpVersion httpVersion, HttpMethod method, String uri) {
    super(httpVersion, method, uri);
  }

  public Request(HttpVersion httpVersion, HttpMethod method, String uri, ByteBuf content) {
    super(httpVersion, method, uri, content);
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