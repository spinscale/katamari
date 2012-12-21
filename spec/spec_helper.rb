require 'bundler'
Bundler.require(:default, :test)

require 'java'
require File.expand_path('../../javalib/netty-3.5.11.Final.jar', __FILE__)
require File.expand_path('../../target/katamari-0.1.1-SNAPSHOT.jar', __FILE__)
#require File.expand_path('../../lib/katamari', __FILE__)