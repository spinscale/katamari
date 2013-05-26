package io.katamari;

import io.katamari.handler.*;
import io.katamari.settings.Settings;
import io.katamari.settings.SettingsException;
import io.katamari.settings.types.ByteSizeUnit;
import io.katamari.settings.types.ByteSizeValue;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
  private final ServerBootstrap bootstrap;
  private Logger logger = LoggerFactory.getLogger(getClass());


  public Server(final Settings settings, final ServerPipeline sp) throws Exception {
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

            pipeline.addLast("katamari:decoder", new RequestDecoder());
            ByteSizeValue defaultMaxLength = new ByteSizeValue(10, ByteSizeUnit.MB);
            int maxSize = settings.getAsBytesSize("http.max.length", defaultMaxLength).bytesAsInt();
            pipeline.addLast("netty:aggregator", new HttpObjectAggregator(maxSize));
            pipeline.addLast("netty:encoder", new HttpResponseEncoder());

            pipeline.addLast("katamari:env_initializer", new EnvInitializer());

            sp.populate(pipeline);

            logger.info("Configured pipeline: {}", pipeline.names());
          }
        });

      bootstrap.bind(settings.getAsInt("http.port", 8080)).sync().channel().closeFuture().sync();
    } finally {
      bootstrap.shutdown();
    }
  }

  public static void main(String [] args) throws Exception {
    Settings settings = null;
    try {
      Settings.load(Server.class.getResourceAsStream("/config.yml"));
    } catch (Exception e) {
      settings = new Settings.SettingsBuilder().build();
    }

    final Settings finalSettings = settings;
    new Server(settings, new ServerPipeline() {
      public void populate(ChannelPipeline pipeline) {
        pipeline.addLast("uri_decoder", new UriDecoder());
        pipeline.addLast("body_decoder", new BodyDecoder());
        pipeline.addLast("auth_decoder", new AuthDecoder("/auth.*", finalSettings.componentSettings("auth")));
        pipeline.addLast("hello_world", new HelloWorld());
      }
    });
  }
}