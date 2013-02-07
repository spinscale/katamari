package io.katamari.handler;

import org.jboss.netty.channel.LifeCycleAwareChannelHandler;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;

import io.katamari.env.Request;

public class RequestDecoder extends HttpRequestDecoder {
  @Override
  protected HttpMessage createMessage(String[] initialLine) throws Exception {
    return new Request(
      HttpVersion.valueOf(initialLine[2]), HttpMethod.valueOf(initialLine[0]), initialLine[1]);
  }
}