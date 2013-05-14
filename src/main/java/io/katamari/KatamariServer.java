package io.katamari;

import io.katamari.handler.EnvInitializer;
import io.katamari.handler.RequestDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.LinkedList;
import java.util.List;

public class KatamariServer {

    private int port;
    private final List<ChannelHandler> channelHandlers = new LinkedList<ChannelHandler>();

    public KatamariServer(int port) {
        this.port = port;
    }

    public KatamariServer start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup group = null;

        try {
            group = new NioEventLoopGroup();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast("katamari:decoder", new RequestDecoder());
                            pipeline.addLast("netty:aggregator", new HttpObjectAggregator(1048576));
                            pipeline.addLast("netty:encoder", new HttpResponseEncoder());
                            pipeline.addLast("katamari:env_initializer", new EnvInitializer());

                            // TODO: Maybe check for dups here?
                            for (ChannelHandler channelHandler : channelHandlers) {
                                pipeline.addLast(channelHandler);
                            }
                        }
                    });

            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

        return this;
    }

    public KatamariServer add(ChannelHandler channelHandler) {
        channelHandlers.add(channelHandler);
        return this;
    }
}
