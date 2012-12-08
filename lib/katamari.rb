$:.unshift File.expand_path('../', __FILE__)
require 'java'
require File.expand_path('../../java_lib/netty-3.5.11.Final.jar', __FILE__)

module Katamari
  autoload :Handler, 'handler'
  autoload :Server, 'server'
  autoload :Env, 'env'

  module_function

  def use(handler)
    @@handlers ||= []
    @@handlers << handler
    self
  end

  def listen(port)
    Katamari::Server.new(port, @@handlers)
  end
end