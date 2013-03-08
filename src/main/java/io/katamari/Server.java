package io.katamari;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.channel.ChannelOption;

import io.katamari.ServerPipeline;
import io.katamari.handler.EnvInitializer;
import io.katamari.handler.HelloWorld;

public class Server {
  private final ServerBootstrap bootstrap;

  public Server(int port, final ServerPipeline sp) throws Exception {
    this.bootstrap = new ServerBootstrap();

    try {
      bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
        .channel(NioServerSocketChannel.class)
        .childOption(ChannelOption.TCP_NODELAY, true)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();

            pipeline.addLast("netty:decoder", new HttpRequestDecoder());
            pipeline.addLast("netty:aggregator", new HttpObjectAggregator(1048576));
            pipeline.addLast("netty:encoder", new HttpResponseEncoder());

            pipeline.addLast("katamari:env_initializer", new EnvInitializer());

            sp.populate(pipeline);
          }
        });

      bootstrap.bind(port).sync().channel().closeFuture().sync();
    } finally {
      bootstrap.shutdown();
    }
  }

  public static void main(String [] args) throws Exception {
    new Server(8080, new ServerPipeline() {
      public void populate(ChannelPipeline pipeline) {
        pipeline.addLast("hello_world", new HelloWorld());
      }
    });
  }
}