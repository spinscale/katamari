package io.katamari.env;

import static org.jboss.netty.handler.codec.http.HttpVersion.*;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.jboss.netty.handler.codec.http.DefaultHttpChunk;
import org.jboss.netty.handler.codec.http.DefaultHttpChunkTrailer;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunkTrailer;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.CharsetUtil;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

public class Response {
  private Channel channel;
  private DefaultHttpResponse headResponse;
  private Map<String,String> trailers = new HashMap<String,String>();
  private boolean headersSent = false;
  public  boolean sendDate = true;

  public Response(Channel channel) {
    this.channel = channel;
    this.headResponse = new DefaultHttpResponse(HTTP_1_1, OK);
    if (sendDate) { headResponse.setHeader("Date", getUtcDateTime()); }
    headResponse.setHeader("Connection", "keep-alive");
    headResponse.setHeader("Transfer-Encoding", "chunked");
  }

  public int getStatusCode() {
    return headResponse.getStatus().getCode();
  }

  public void setStatusCode(int statusCode) throws HeadersAlreadySentException {
    if (headersSent) { throw new HeadersAlreadySentException(); }
    headResponse.setStatus(HttpResponseStatus.valueOf(statusCode));
  }

  public void writeContinue() {
    channel.write(new DefaultHttpResponse(HTTP_1_1, CONTINUE));
  }

  public void setHeader(String name, String value) throws HeadersAlreadySentException {
    if (headersSent) { throw new HeadersAlreadySentException(); }
    headResponse.setHeader(name, value);
  }

  public String getHeader(String name) {
    return headResponse.getHeader(name);
  }

  public void removeHeader(String name) throws HeadersAlreadySentException {
    if (headersSent) { throw new HeadersAlreadySentException(); }
    headResponse.removeHeader(name);
  }

  public void writeHead(int statusCode) throws HeadersAlreadySentException {
    setStatusCode(statusCode);
    if (!sendDate) { removeHeader("Date"); }
    channel.write(headResponse);
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
    channel.write(new DefaultHttpChunk(ChannelBuffers.copiedBuffer(data, encoding)));
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

    HttpChunkTrailer chunkTrailer = new DefaultHttpChunkTrailer();
    for (Map.Entry<String,String> trailer: trailers.entrySet()) {
      chunkTrailer.addHeader((String)trailer.getKey(), (String)trailer.getValue());
    }
    
    channel.write(chunkTrailer);
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