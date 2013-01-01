$:.unshift File.expand_path('../', __FILE__)
require 'java'
require File.expand_path('../../javalib/netty-3.5.11.Final.jar', __FILE__)
require File.expand_path('../../target/katamari-0.1.1-SNAPSHOT.jar', __FILE__)

module Katamari
  module Handler
    java_import 'io.katamari.handler.UriDecoder'
  end

  java_import 'io.katamari.Server'
end