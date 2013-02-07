package io.katamari;

import org.jboss.netty.channel.ChannelPipeline;

public interface ServerChannelPipelineFactory {
  void getPipeline(ChannelPipeline pipeline);
}