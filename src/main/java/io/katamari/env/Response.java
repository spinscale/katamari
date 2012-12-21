package io.katamari.env;

import static org.jboss.netty.handler.codec.http.HttpHeaders.*;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.*;

import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.CharsetUtil;
import org.jboss.netty.channel.MessageEvent;

public class Response extends DefaultHttpResponse {
  private HttpRequest request;
  private Channel channel;

  public Response(MessageEvent e) {
    super(HTTP_1_1, OK);
    this.request = (HttpRequest)e.getMessage();
    this.channel = (Channel)e.getChannel();
  }

  public void end(String content) {
    setContent(ChannelBuffers.copiedBuffer(content, CharsetUtil.UTF_8));
    setContentLength(this, getContent().readableBytes());
    ChannelFuture future = this.channel.write(this);
    keepAliveOrClose(future);
  }

  private void keepAliveOrClose(ChannelFuture future) {
    //boolean keepAlive = HttpHeaders.isKeepAlive(request);

    if (true) {
      future.addListener(new ChannelFutureListener() {
        public void operationComplete(ChannelFuture future) {
          future.getChannel().setReadable(true);
        } 
      });
    } else {
      future.addListener(ChannelFutureListener.CLOSE);
    }
  }
}