package io.katamari.handler;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import static org.jboss.netty.handler.codec.http.HttpVersion.*;
import static org.jboss.netty.handler.codec.http.HttpMethod.*;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.util.CharsetUtil;

import io.katamari.Env;
import io.katamari.handler.UriDecoder;

public class UriDecoderTest {
  private final HttpRequest request = new DefaultHttpRequest(HTTP_1_1, GET, "/path?id=1"); // TODO: mock
  private final MessageEvent event = mock(MessageEvent.class);
  private final ChannelHandlerContext context = mock(ChannelHandlerContext.class);
  private final MessageEvent alteredEvent = mock(MessageEvent.class);
  private final UriDecoder handler = new UriDecoder();

  private Env env;

  @Before
  public void initialize() {
    //request.setContent(ChannelBuffers.copiedBuffer("{}", CharsetUtil.UTF_8));
    when(event.getMessage()).thenReturn(request);
    this.env = new Env(event); // TODO: mock
    when(alteredEvent.getMessage()).thenReturn(env);
  }

  @Test
  public void exposesPathInRequest() throws Exception {
    assertEquals(((Env)alteredEvent.getMessage()).request().path(), null);
    handler.messageReceived(context, alteredEvent);
    assertEquals(((Env)alteredEvent.getMessage()).request().path(), "/path");
  }

  @Test
  public void exposesQueryStringParamsInRequest() throws Exception {
    HashMap<String,String> params = new HashMap<String,String>();
    params.put("id", "1");

    assertEquals(((Env)alteredEvent.getMessage()).request().params(), new HashMap<String,String>());
    handler.messageReceived(context, alteredEvent);
    assertEquals(((Env)alteredEvent.getMessage()).request().params(), params);
  }
}