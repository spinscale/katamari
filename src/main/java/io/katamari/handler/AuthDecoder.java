package io.katamari.handler;

import static io.netty.handler.codec.http.HttpHeaders.Names.AUTHORIZATION;
import static io.netty.handler.codec.http.HttpHeaders.Names.WWW_AUTHENTICATE;

import io.katamari.Env;
import io.katamari.env.Request;
import io.katamari.env.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import sun.misc.BASE64Decoder;

import java.util.regex.Pattern;

public class AuthDecoder extends InboundMessageHandler {

  private final Pattern pattern;
  private final String username;
  private final String password;
  private final BASE64Decoder decoder;

  public AuthDecoder(String pathRegex, String username, String password) {
    this.pattern = Pattern.compile(pathRegex);
    this.username = username;
    this.password = password;
    this.decoder = new BASE64Decoder();
  }

    @Override
    public String getName() {
        return "basic-auth";
    }

    @Override
  public void messageReceived(ChannelHandlerContext ctx, Env env) throws Exception {
    Request request = env.getRequest();
    if (pattern.matcher(request.getPath()).matches()) {
      HttpHeaders headers = request.headers();
      if (!headers.contains(AUTHORIZATION)) {
        requireAuth(ctx, env);
        return;
      }

      String auth = headers.get(AUTHORIZATION);
      if (!auth.startsWith("Basic ")) {
        requireAuth(ctx, env);
        return;
      }

      String decodedAuth = new String(decoder.decodeBuffer(auth.substring(6)));
      if (!decodedAuth.contains(":")) {
        requireAuth(ctx, env);
        return;
      }

      String[] authStr = decodedAuth.split(":", 2);
      String sentUser = authStr[0];
      String sentPassword = authStr[1];

      if (!username.equals(sentUser) || !password.equals(sentPassword)) {
        requireAuth(ctx, env);
        return;
      }
    }

    nextHandler(ctx, env);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }

  private void requireAuth(ChannelHandlerContext ctx, Env env) throws Response.HeadersAlreadySentException {
    env.getResponse().setHeader(WWW_AUTHENTICATE, "Basic realm==\"auth\"");
    env.getResponse().setStatusCode(403);
    env.getResponse().end();
  }

}
