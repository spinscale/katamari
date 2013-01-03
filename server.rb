require File.expand_path('../lib/katamari', __FILE__)

class HelloWorld < org.jboss.netty.channel.SimpleChannelUpstreamHandler
  def messageReceived(ctx, e)
    env = e.message
    env.response.end('Hello World!')
  end
end

s = Katamari::Server.new
s.add('uri_decoder', Katamari::Handler::UriDecoder.new).add('body_decoder', Katamari::Handler::BodyDecoder.new).add('hello_world', HelloWorld.new).listen(8082)