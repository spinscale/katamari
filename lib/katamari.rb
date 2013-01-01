$:.unshift File.expand_path('../', __FILE__)
require 'java'
require File.expand_path('../../javalib/netty-3.5.11.Final.jar', __FILE__)
require File.expand_path('../../target/katamari-0.1.1-SNAPSHOT.jar', __FILE__)

module Katamari
  module Handler
    autoload :URIDecoder, 'handler/uri_decoder'
    autoload :BodyDecoder, 'handler/body_decoder'
  end

  java_import 'io.katamari.Server'
end