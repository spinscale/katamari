require File.expand_path('../../lib/katamari', __FILE__)

class TestHandler < Katamari::Handler::Upstream
  def messageReceived(ctx, e)
    env = e.message

    if env.request.path == '/test'
      env.response.body = 'Test!'
      env.response.end
    else
      ctx.send_upstream(e)
    end
  end
end

class HelloWorldHandler < Katamari::Handler::Upstream
  def messageReceived(ctx, e)
    env = e.message
    env.response.body = 'Hello World!'
    env.response.end
  end
end

Katamari.use(Katamari::Handler::URIDecoder).use(TestHandler).use(HelloWorldHandler).listen(8080)