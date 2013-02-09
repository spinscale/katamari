package io.katamari.env;

import static org.mockito.Mockito.*;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import org.jboss.netty.channel.Channel;

import io.katamari.env.Response;
import io.katamari.env.Response.HeadersAlreadySentException;

@RunWith(MockitoJUnitRunner.class)
public class ResponseTest {
  @Mock private Channel channel;

  private Response response;

  @Before
  public void initialize() {
    MockitoAnnotations.initMocks(this);
    when(channel.write(anyObject())).thenReturn(null);
    response = new Response(channel);
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
}