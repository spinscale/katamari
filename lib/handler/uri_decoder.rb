class Katamari::Handler::URIDecoder < org.jboss.netty.channel.SimpleChannelUpstreamHandler
  import org.jboss.netty.handler.codec.http.QueryStringDecoder

  def messageReceived(ctx, e)
    env = e.message

    uri = QueryStringDecoder.new(env.request.uri)
    #env.request.path = uri.path
    uri.parameters.each { |k,v| env.request.params[k] = v.get(0) }
    
    ctx.send_upstream(e)
  end
end