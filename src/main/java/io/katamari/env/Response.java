package io.katamari.env;

import static org.jboss.netty.handler.codec.http.HttpVersion.*;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;

import org.jboss.netty.handler.codec.http.DefaultHttpChunk;
import org.jboss.netty.handler.codec.http.DefaultHttpChunkTrailer;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.CharsetUtil;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

public class Response {
  private Channel channel;
  private DefaultHttpResponse headResponse;
  private boolean headersSent = false;

  public Response(Channel channel) {
    this.channel = channel;
    this.headResponse = new DefaultHttpResponse(HTTP_1_1, OK);
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
    channel.write(headResponse);
    headersSent = true;
  }

  public void write(String data) throws HeadersAlreadySentException {
    if (!headersSent) { writeHead(getStatusCode()); }
    channel.write(new DefaultHttpChunk(ChannelBuffers.copiedBuffer(data, CharsetUtil.UTF_8)));
  }

  public void end() throws HeadersAlreadySentException {
    end("");
  }

  public void end(String data) throws HeadersAlreadySentException {
    if (!headersSent) { writeHead(getStatusCode()); }
    if (data != "") { write(data); }
    channel.write(new DefaultHttpChunkTrailer());
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