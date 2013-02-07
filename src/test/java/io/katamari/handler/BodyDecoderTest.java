package io.katamari.handler;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import static org.jboss.netty.handler.codec.http.HttpVersion.*;
import static org.jboss.netty.handler.codec.http.HttpMethod.*;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.util.CharsetUtil;

import io.katamari.Env;
import io.katamari.env.Request;
import io.katamari.handler.BodyDecoder;

public class BodyDecoderTest {
  private final Request request = new Request(HTTP_1_1, POST, "/path?id=1"); // TODO: mock
  private final MessageEvent event = mock(MessageEvent.class);
  private final ChannelHandlerContext context = mock(ChannelHandlerContext.class);
  private final MessageEvent alteredEvent = mock(MessageEvent.class);
  private final BodyDecoder handler = new BodyDecoder();

  private Env env;

  @Before
  public void initialize() {
    request.setContent(ChannelBuffers.copiedBuffer("foo=bar", CharsetUtil.UTF_8));
    when(event.getMessage()).thenReturn(request);
    this.env = new Env(event); // TODO: mock
    when(alteredEvent.getMessage()).thenReturn(env);
  }

  @Test
  public void exposesBodyParamsInRequest() throws Exception {
    HashMap<String,String> params = new HashMap<String,String>();
    params.put("foo", "bar");

    assertEquals(new HashMap<String,String>(), ((Env)alteredEvent.getMessage()).request().params());
    handler.messageReceived(context, alteredEvent);
    assertEquals(params, ((Env)alteredEvent.getMessage()).request().params());
  }

  @Test
  public void notExposesBodyParamsInRequestonGet() throws Exception {
    request.setMethod(GET);
    assertEquals(new HashMap<String,String>(), ((Env)alteredEvent.getMessage()).request().params());
    handler.messageReceived(context, alteredEvent);
    assertEquals(new HashMap<String,String>(), ((Env)alteredEvent.getMessage()).request().params());
  }
}