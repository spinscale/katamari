class Katamari::Handler::BodyDecoder < Katamari::Handler::Upstream
  import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder

  def messageReceived(ctx, e)
    env = e.message

    if [:post, :put, :patch].include?(env.request.method)
      post = HttpPostRequestDecoder.new(env.request.origin)
      post.body_http_datas.each { |a| env.request.params[a.name] = a.value }
    end

    ctx.send_upstream(e)
  end
end