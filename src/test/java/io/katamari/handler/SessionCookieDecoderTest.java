package io.katamari.handler;

import com.ning.http.client.Cookie;
import com.ning.http.client.Response;
import io.katamari.Env;
import io.katamari.settings.Settings;
import io.katamari.test.KatamariTest;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SessionCookieDecoderTest extends KatamariTest {

  private final String SECRET = "FOO BAR BAZ";
  private final String SESSION_ID = "customSessionId";

  @Override
  public void configure() {
    Settings settings = new Settings.SettingsBuilder()
        .put("secret", SECRET)
        .put("cookie", SESSION_ID)
        .build();
    addHandler("session", new SessionCookieDecoder(settings));
    addHandler("sessionAdder", new InboundMessageHandler(){
      @Override
      public void messageReceived(ChannelHandlerContext ctx, Env env) throws Exception {
        if (env.getSession().contains("foo")) {
          env.getSession().put("foo", "bar-updated");
        } else {
          env.getSession().put("foo", "bar");
        }
        nextHandler(ctx, env);
      }
    });
    addHandler("helloworld", new HelloWorld());
  }

  @Test
  public void testThatSessionCookieIsReturned() throws Exception {
    Response response = sendRequest(GET().build());
    Cookie cookie = getCookieByName(SESSION_ID, response);
    assertThat(cookie, is(notNullValue()));
    assertThat(cookie.getValue(), containsString("foo=bar"));

    // send the cookie and get back foo=bar-updated
    response = sendRequest(GET().addCookie(cookie).build());
    cookie = getCookieByName(SESSION_ID, response);
    assertThat(cookie, is(notNullValue()));
    assertThat(cookie.getValue(), containsString("foo=bar-updated"));
  }

  private Cookie getCookieByName(String cookieName, Response response) {
    for (Cookie cookie : response.getCookies()) {
      if (cookie.getName().equals(cookieName)) {
        return cookie;
      }
    }

    return null;
  }

}
