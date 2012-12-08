class Katamari::Handler::ResponseEncoder < Katamari::Handler::Downstream
  import org.jboss.netty.channel.Channels
  
  def writeRequested(ctx, e)
    Channels.write(ctx, e.future, e.message.response.encode)
  end
end