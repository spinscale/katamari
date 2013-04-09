package io.katamari.env;

import static io.netty.handler.codec.http.HttpVersion.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

public class Response {
  private ChannelHandlerContext context;
  private DefaultHttpResponse headResponse;
  private Map<String,String> trailers = new HashMap<String,String>();
  private boolean headersSent = false;
  public  boolean sendDate = true;

  public Response(ChannelHandlerContext ctx) {
    this.context = ctx;
    this.headResponse = new DefaultHttpResponse(HTTP_1_1, OK);
    if (sendDate) { headResponse.headers().set("Date", getUtcDateTime()); }
    headResponse.headers().set("Connection", "keep-alive");
    headResponse.headers().set("Transfer-Encoding", "chunked");
  }

  public int getStatusCode() {
    return headResponse.getStatus().code();
  }

  public void setStatusCode(int statusCode) throws HeadersAlreadySentException {
    if (headersSent) { throw new HeadersAlreadySentException(); }
    headResponse.setStatus(HttpResponseStatus.valueOf(statusCode));
  }

  public void writeContinue() {
    context.nextOutboundMessageBuffer().add(new DefaultHttpResponse(HTTP_1_1, CONTINUE));
  }

  public void setHeader(String name, String value) throws HeadersAlreadySentException {
    if (headersSent) { throw new HeadersAlreadySentException(); }
    headResponse.headers().set(name, value);
  }

  public String getHeader(String name) {
    return headResponse.headers().get(name);
  }

  public void removeHeader(String name) throws HeadersAlreadySentException {
    if (headersSent) { throw new HeadersAlreadySentException(); }
    headResponse.headers().remove(name);
  }

  public void writeHead(int statusCode) throws HeadersAlreadySentException {
    setStatusCode(statusCode);
    if (!sendDate) { removeHeader("Date"); }
    context.nextOutboundMessageBuffer().add(headResponse);
    headersSent = true;
  }

  public void writeHead(int statusCode, Map<String,String> headers) throws HeadersAlreadySentException {
    for (Map.Entry<String,String> header: headers.entrySet()) {
      setHeader((String)header.getKey(), (String)header.getValue());
    }

    writeHead(statusCode);
  }

  public void write(String data) throws HeadersAlreadySentException {
    write(data, CharsetUtil.UTF_8);
  }

  public void write(String data, Charset encoding) throws HeadersAlreadySentException {
    if (!headersSent) { writeHead(getStatusCode()); }
    context.nextOutboundMessageBuffer().add(new DefaultHttpContent(Unpooled.copiedBuffer(data, encoding)));
  }

  public void end() throws HeadersAlreadySentException {
    end(null, CharsetUtil.UTF_8);
  }

  public void end(String data) throws HeadersAlreadySentException {
    end(data, CharsetUtil.UTF_8);
  }

  public void end(String data, Charset encoding) throws HeadersAlreadySentException {
    if (!headersSent) { writeHead(getStatusCode()); }
    if (data != null) { write(data, encoding); }

    LastHttpContent lastContent = new DefaultLastHttpContent();
    for (Map.Entry<String,String> trailer: trailers.entrySet()) {
      lastContent.trailingHeaders().set((String)trailer.getKey(), (String)trailer.getValue());
    }
    
    context.nextOutboundMessageBuffer().add(lastContent);
  }

  public void addTrailers(Map<String,String> headers) {
    for (Map.Entry<String,String> header: headers.entrySet()) {
      trailers.put((String)header.getKey(), (String)header.getValue());
    }
  }

  private String getUtcDateTime() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return dateFormat.format(new Date());
  }

  public static class HeadersAlreadySentException extends Exception {
    private static final long serialVersionUID = 3944337479091193286L;

    public HeadersAlreadySentException() {
    }

    public HeadersAlreadySentException(String msg) {
        super(msg);
    }

    public HeadersAlreadySentException(Throwable cause) {
        super(cause);
    }

    public HeadersAlreadySentException(String msg, Throwable cause) {
        super(msg, cause);
    }
  }
}