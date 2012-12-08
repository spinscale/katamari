class Katamari::Handler::RequestDecoder < Katamari::Handler::Upstream
  import org.jboss.netty.channel.Channels
  
  def messageReceived(ctx, e)
    Channels.fireMessageReceived(ctx, Katamari::Env.new(e))
  end
end