package io.katamari.handler;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import static org.jboss.netty.handler.codec.http.HttpVersion.*;
import static org.jboss.netty.handler.codec.http.HttpMethod.*;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import io.katamari.Env;
import io.katamari.env.Request;
import io.katamari.handler.UriDecoder;

public class UriDecoderTest {
  private final Request request = new Request(HTTP_1_1, GET, "/path?id=1"); // TODO: mock
  private final MessageEvent event = mock(MessageEvent.class);
  private final ChannelHandlerContext context = mock(ChannelHandlerContext.class);
  private final MessageEvent alteredEvent = mock(MessageEvent.class);
  private final UriDecoder handler = new UriDecoder();

  private Env env;

  @Before
  public void initialize() {
    when(event.getMessage()).thenReturn(request);
    this.env = new Env(event); // TODO: mock
    when(alteredEvent.getMessage()).thenReturn(env);
  }

  @Test
  public void exposesPathInRequest() throws Exception {
    assertEquals(null, ((Env)alteredEvent.getMessage()).getRequest().path());
    handler.messageReceived(context, alteredEvent);
    assertEquals("/path", ((Env)alteredEvent.getMessage()).getRequest().path());
  }

  @Test
  public void exposesQueryStringParamsInRequest() throws Exception {
    HashMap<String,String> params = new HashMap<String,String>();
    params.put("id", "1");

    assertEquals(new HashMap<String,String>(), ((Env)alteredEvent.getMessage()).getRequest().params());
    handler.messageReceived(context, alteredEvent);
    assertEquals(params, ((Env)alteredEvent.getMessage()).getRequest().params());
  }
}