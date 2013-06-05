package io.katamari.handler;

import io.katamari.Env;
import io.katamari.env.Request;
import io.katamari.env.Response;
import io.katamari.settings.Settings;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.ServerCookieEncoder;

import java.util.Set;

public class SessionCookieDecoder extends InboundMessageHandler {

  private final String secret;
  private final String cookieName;

  public SessionCookieDecoder(Settings settings) {
    secret = settings.getAsString("secret");
    cookieName = settings.getAsString("cookie", "sessionId");
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, final Env env) throws Exception {
    Cookie cookie = getSessionCookie(env.getRequest());
    if (cookie != null) {
      env.getSession().deserialize(secret, cookie.getValue());
    }

    // when sending out the response, serialize the session
    env.getResponse().beforeSend(new Response.ResponseSendListener() {
      public void execute() {
        try {
          if (!env.getSession().isEmpty()) {
            env.getResponse().setHeader("Set-Cookie", ServerCookieEncoder.encode(cookieName, env.getSession().serialize(secret)));
          }
        } catch (Response.HeadersAlreadySentException e) {
        }
      }
    });

    nextHandler(ctx, env);
  }

  private Cookie getSessionCookie(Request request) {
    if (request.headers().isEmpty() || request.headers().get("Cookie") == null) {
      return null;
    }

    Set<Cookie> cookies = CookieDecoder.decode(request.headers().get("Cookie"));
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals(cookieName)) {
        return cookie;
      }
    }

    return null;
  }

}