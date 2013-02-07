package io.katamari;

import org.jboss.netty.channel.ChannelPipeline;

public interface ServerPipeline {
  void populate(ChannelPipeline pipeline);
}