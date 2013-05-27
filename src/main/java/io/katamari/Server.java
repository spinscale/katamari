package io.katamari;

import io.katamari.handler.*;
import io.katamari.settings.Settings;
import io.katamari.settings.types.ByteSizeUnit;
import io.katamari.settings.types.ByteSizeValue;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
  private final ServerBootstrap bootstrap;
  private final Settings settings;
  private Thread thread;
  private NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
  private Boolean isStarted = Boolean.FALSE;
  private Logger logger = LoggerFactory.getLogger(getClass());

  public Server(final Settings settings, final ServerPipeline sp) throws Exception {
    this.bootstrap = new ServerBootstrap();
    this.settings = settings;

    bootstrap.group(nioEventLoopGroup)
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
  }

  public void shutdown() {
    if (thread != null) {
      thread.interrupt();
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
    Server server = new Server(settings, new ServerPipeline() {
      public void populate(ChannelPipeline pipeline) {
        pipeline.addLast("uri_decoder", new UriDecoder());
        pipeline.addLast("body_decoder", new BodyDecoder());
        pipeline.addLast("auth_decoder", new AuthDecoder("/auth.*", finalSettings.componentSettings("auth")));
        pipeline.addLast("hello_world", new HelloWorld());
      }
    });
    server.start();
  }

  public void start() {
    thread = new Thread(new Runnable() {
      public void run() {
        try {
          bootstrap.bind(settings.getAsInt("http.port", 8080)).sync().addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
              System.out.println("### COMPLETE");
              isStarted = Boolean.TRUE;
            }
          }).channel().closeFuture().sync();
        } catch (InterruptedException e) {
          isStarted = false;
        } finally {
          nioEventLoopGroup.shutdownGracefully();
        }
      }
    });
    thread.setDaemon(false);
    thread.start();
  }

  public void waitForStart() {
    while (!isStarted) {
      try {
        Thread.sleep(20);
      } catch (InterruptedException e) {}
    }
  }

}
