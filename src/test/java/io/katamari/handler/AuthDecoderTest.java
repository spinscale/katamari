package io.katamari.handler;

import static io.netty.handler.codec.http.HttpHeaders.Names.AUTHORIZATION;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import io.katamari.Env;
import io.netty.buffer.MessageBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import sun.misc.BASE64Encoder;

@RunWith(MockitoJUnitRunner.class)
public class AuthDecoderTest {

  @Mock private ChannelHandlerContext context;
  @Mock private MessageBuf<Object> buf;
  @Mock private MessageBuf<Object> outboundBuf;

  private Env env;

  private final AuthDecoder handler = new AuthDecoder(".*", "user", "pass");
  private final DefaultFullHttpRequest request = new DefaultFullHttpRequest(HTTP_1_1, GET, "/foo");
  private final BASE64Encoder base64Encoder = new BASE64Encoder();

  @Before
  public void initialize() {
    MockitoAnnotations.initMocks(this);
    when(context.nextInboundMessageBuffer()).thenReturn(buf);
    when(buf.unfoldAndAdd(anyObject())).thenReturn(true);
    when(context.fireInboundBufferUpdated()).thenReturn(context);
    when(context.nextOutboundMessageBuffer()).thenReturn(outboundBuf);
  }

  @Test
  public void testThatAuthIsDeniedWithWrongUser() throws Exception {
    receiveRequest(encodeBasicAuth("noUser", "pass"));
    assertThat(env.getResponse().getStatusCode(), is(403));
  }

  @Test
  public void testThatAuthIsDeniedWithWrongPass() throws Exception {
    receiveRequest(encodeBasicAuth("user", "wrongpass"));
    assertThat(env.getResponse().getStatusCode(), is(403));
  }

  @Test
  public void testThatAuthIsDeniedWithArbitraryString() throws Exception {
    receiveRequest("dafuq");
    assertThat(env.getResponse().getStatusCode(), is(403));
  }

  @Test
  public void testThatAuthIsAllowedWithValidUserAndPass() throws Exception {
    receiveRequest(encodeBasicAuth("user", "pass"));
    assertThat(env.getResponse().getStatusCode(), is(200));
  }

  private void receiveRequest(String basicAuthHeader) throws Exception {
    request.headers().add(AUTHORIZATION, basicAuthHeader);
    env = new Env(context, request);
    env.getRequest().setPath(request.getUri());
    handler.messageReceived(context, env);
  }

  private String encodeBasicAuth(String noUser, String pass) {
    return "Basic " + base64Encoder.encode(new String (noUser +  ":" + pass).getBytes());
  }

}
