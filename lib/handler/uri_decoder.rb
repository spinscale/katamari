class Katamari::Handler::URIDecoder < Katamari::Handler::Upstream
  import org.jboss.netty.handler.codec.http.QueryStringDecoder

  def messageReceived(ctx, e)
    env = e.message
    uri = QueryStringDecoder.new(env.request.uri)
    params = {}
    uri.parameters.each { |k,v| params[k] = v.get(0) }
    
    env.request.path = uri.path
    env.request.params = params

    ctx.send_upstream(e)
  end
end