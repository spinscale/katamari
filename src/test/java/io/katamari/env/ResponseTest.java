package io.katamari.env;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.MessageBuf;

import io.katamari.env.Response;
import io.katamari.env.Response.HeadersAlreadySentException;

@RunWith(MockitoJUnitRunner.class)
public class ResponseTest {
  @Mock private ChannelHandlerContext ctx;
  @Mock private MessageBuf<Object> buf;

  private Response response;

  @Before
  public void initialize() {
    MockitoAnnotations.initMocks(this);
    when(ctx.nextOutboundMessageBuffer()).thenReturn(buf);
    when(buf.add(anyObject())).thenReturn(true);
    response = new Response(ctx);
  }

  @Test(expected=HeadersAlreadySentException.class)
  public void cantSetStatusCodeAfterHeadersHasBeenSent() throws HeadersAlreadySentException {
    response.writeHead(200);
    response.setStatusCode(204);
  }

  @Test(expected=HeadersAlreadySentException.class)
  public void cantSetHeaderAfterHeadersHasBeenSent() throws HeadersAlreadySentException {
    response.writeHead(200);
    response.setHeader("Foo", "bar");
  }

  @Test(expected=HeadersAlreadySentException.class)
  public void cantRemoveHeaderAfterHeadersHasBeenSent() throws HeadersAlreadySentException {
    response.writeHead(200);
    response.removeHeader("Foo");
  }

  @Test
  public void setHeadersWhileWritingHead() throws HeadersAlreadySentException {
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Foo", "bar");
    response.writeHead(200, headers);
    assertEquals("bar", response.getHeader("Foo"));
  }
}