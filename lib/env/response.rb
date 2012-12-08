class Katamari::Env::Response
  import org.jboss.netty.util.CharsetUtil
  import org.jboss.netty.buffer.ChannelBuffers
  import org.jboss.netty.handler.codec.http.DefaultHttpResponse
  import org.jboss.netty.handler.codec.http.HttpResponseStatus
  import org.jboss.netty.handler.codec.http.HttpVersion
  import org.jboss.netty.handler.codec.http.HttpHeaders

  attr_accessor :status, :headers, :body

  def initialize(event)
    @channel, @request = event.channel, event.message
    @status, @headers, @body = 200, { 'Content-Length' => 0 }, ''
  end

  def encode
    response = DefaultHttpResponse.new(HttpVersion::HTTP_1_1, HttpResponseStatus.value_of(@status))
    @headers.each { |k,v| response.add_header(k,v) }
    response.content = ChannelBuffers.copied_buffer(@body, CharsetUtil::UTF_8)
    HttpHeaders::set_content_length(response, response.content.readable_bytes)
    response
  end

  def end
    future = @channel.write(self.encode)
    keep_alive_or_close(future)
  end

  private

  def keep_alive_or_close(future)
    if HttpHeaders::keep_alive?(@request)
      future.add_listener { @channel.set_readable(true) }
    else
      future.add_listener(ChannelFutureListener::CLOSE)
    end
  end
end