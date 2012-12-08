require File.expand_path('../../lib/katamari', __FILE__)

class HelloWorldHandler < Katamari::Handler::Upstream
  def messageReceived(ctx, e)
    env = e.message
    env.response.body = 'Hello World!'
    env.response.end
  end
end

Katamari.use(HelloWorldHandler).listen(8080)