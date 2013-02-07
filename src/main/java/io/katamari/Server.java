package io.katamari;

import static org.jboss.netty.channel.Channels.*;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import io.katamari.ServerChannelPipelineFactory;
import io.katamari.handler.NoPipelining;
import io.katamari.handler.RequestDecoder;
import io.katamari.handler.EnvInitializer;
import io.katamari.handler.HelloWorld;

public class Server {
  private final ServerBootstrap bootstrap;

  public Server(int port, final ServerPipeline sp) {
    this.bootstrap = new ServerBootstrap(
      new NioServerSocketChannelFactory(
              Executors.newCachedThreadPool(),
              Executors.newCachedThreadPool()));

    bootstrap.setOption("child.tcpNoDelay", true);
    bootstrap.setOption("child.keepAlive", true);

    bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
      public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline();

        pipeline.addLast("katamari:decoder", new RequestDecoder());
        pipeline.addLast("netty:aggregator", new HttpChunkAggregator(65536));
        pipeline.addLast("netty:encoder", new HttpResponseEncoder());

        pipeline.addLast("katamari:no_pipelining", new NoPipelining());
        pipeline.addLast("katamari:env_initializer", new EnvInitializer());
        
        sp.populate(pipeline);

        return pipeline;
      }
    });

    bootstrap.bind(new InetSocketAddress(port));
  }

  public static void main(String [] args) {
    new Server(8080, new ServerPipeline() {
      public void populate(ChannelPipeline pipeline) {
        pipeline.addLast("hello_world", new HelloWorld());
      }
    });
  }
}