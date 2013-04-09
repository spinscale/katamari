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

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;

import io.katamari.Env;
import io.katamari.env.Request;
import io.katamari.handler.UriDecoder;

@RunWith(MockitoJUnitRunner.class)
public class UriDecoderTest {
  @Mock private ChannelHandlerContext context;
  
  private UriDecoder handler = new UriDecoder();
  private DefaultFullHttpRequest request = new DefaultFullHttpRequest(HTTP_1_1, GET, "/path?id=1");
  private Env env = new Env(context, request);

  @Test
  public void exposesPathInRequest() throws Exception {
    assertEquals(null, env.getRequest().getPath());
    handler.messageReceived(context, env);
    assertEquals("/path", env.getRequest().getPath());
  }

  @Test
  public void exposesQueryStringParamsInRequest() throws Exception {
    HashMap<String,String> params = new HashMap<String,String>();
    params.put("id", "1");

    assertEquals(new HashMap<String,String>(), env.getRequest().getParams());
    handler.messageReceived(context, env);
    assertEquals(params, env.getRequest().getParams());
  }
}