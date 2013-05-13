package io.katamari.handler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static io.netty.handler.codec.http.HttpVersion.*;
import static io.netty.handler.codec.http.HttpMethod.*;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerUtil;
import io.netty.util.CharsetUtil;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.buffer.MessageBuf;

import io.katamari.Env;
import io.katamari.env.Request;
import io.katamari.handler.BodyDecoder;

@RunWith(MockitoJUnitRunner.class)
public class BodyDecoderTest {
  @Mock private ChannelHandlerContext context;
  @Mock private MessageBuf<Object> buf;

  private BodyDecoder handler = new BodyDecoder();
  private DefaultFullHttpRequest request = new DefaultFullHttpRequest(HTTP_1_1, POST, "/path?id=1", Unpooled.copiedBuffer("foo=bar", CharsetUtil.UTF_8));
  private Env env = new Env(context, request);

  @Before
  public void initialize() throws Exception {
    MockitoAnnotations.initMocks(this);
    when(context.nextInboundMessageBuffer()).thenReturn(buf);
    when(buf.add(anyObject())).thenReturn(true);
  }

  @Test
  public void exposesBodyParamsInRequest() throws Exception {
    HashMap<String,String> params = new HashMap<String,String>();
    params.put("foo", "bar");

    assertEquals(new HashMap<String,String>(), env.getRequest().getParams());
    handler.messageReceived(context, env);
    assertEquals(params, env.getRequest().getParams());
  }

  @Test
  public void notExposesBodyParamsInRequestonGet() throws Exception {
    env.getRequest().setMethod(GET);
    assertEquals(new HashMap<String,String>(), env.getRequest().getParams());
    handler.messageReceived(context, env);
    assertEquals(new HashMap<String,String>(), env.getRequest().getParams());
  }
}