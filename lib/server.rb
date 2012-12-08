class Katamari::Server
  import java.net.InetSocketAddress
  import java.util.concurrent.Executors
  import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory
  import org.jboss.netty.bootstrap.ServerBootstrap

  import org.jboss.netty.channel.Channels
  import org.jboss.netty.handler.codec.http.HttpChunkAggregator
  import org.jboss.netty.handler.codec.http.HttpRequestDecoder
  import org.jboss.netty.handler.codec.http.HttpResponseEncoder

  def initialize(port, handlers)
    channel_factory = NioServerSocketChannelFactory.new(Executors.newCachedThreadPool, Executors.newCachedThreadPool)
    
    bootstrap = ServerBootstrap.new(channel_factory)
    bootstrap.set_option 'child.tcpNoDelay', true
    bootstrap.set_option 'child.keepAlive', true
    
    bootstrap.set_pipeline_factory {
      pipeline = Channels.pipeline
      pipeline.add_last('netty:decoder', HttpRequestDecoder.new)
      pipeline.add_last('netty:aggregator', HttpChunkAggregator.new(65536))
      pipeline.add_last('netty:encoder', HttpResponseEncoder.new)

      pipeline.add_last('katamari:no_pipelining', Katamari::Handler::NoPipelining.new)
      pipeline.add_last('katamari:decoder', Katamari::Handler::RequestDecoder.new)
      #pipeline.add_last('katamari:encoder', Katamari::Handler::ResponseEncoder.new)

      handlers.each_with_index { |h,i| pipeline.add_last("handler#{i}", h.new) }

      pipeline
    }

    bootstrap.bind InetSocketAddress.new(port)
  end
end