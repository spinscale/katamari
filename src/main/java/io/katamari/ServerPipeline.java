package io.katamari;

import io.netty.channel.ChannelPipeline;

public interface ServerPipeline {
  void populate(ChannelPipeline pipeline);
}