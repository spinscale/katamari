class Katamari::Handler < org.jboss.netty.channel.SimpleChannelHandler
  autoload :Upstream, 'handler/upstream'
  autoload :Downstream, 'handler/downstream'
  autoload :NoPipelining, 'handler/no_pipelining'
  autoload :RequestDecoder, 'handler/request_decoder'
  autoload :ResponseEncoder, 'handler/response_encoder'
  autoload :URIDecoder, 'handler/uri_decoder'
  autoload :BodyDecoder, 'handler/body_decoder'
end