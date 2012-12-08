# Katamari (塊)

Asynchronous Middleware Framework with JRuby and Netty

# Why?

We just have one middleware framework in Ruby, Rack. But it's not mean to be used with asynchronous librairies, so it has limits (see http://blog.plataformatec.com.br/2012/06/why-your-web-framework-should-not-adopt-rack-api).

Katamari is a new middleware framework with asynchrony in mind. It is based on the awesome Netty library (http://netty.io) but could be ported to Celluloid or EventMachine some time in the future.

# Run (with JRuby 1.7.x)

Run the examples:

git clone git://github.com/nmerouze/katamari.git
cd katamari
curl http://cloud.github.com/downloads/nmerouze/katamari/netty-3.5.11.Final.jar > java_lib/netty-3.5.11.Final.jar
ruby examples/hello_world.rb

And in another terminal:

curl http://localhost:8080/

# Roadmap

* Update & cleanup specs
* Add more middlewares
* Better API
* Rewrite core in Java (or Mirah) to make it workable with other JVM languages (Clojure, Javascript, Python, Scala, etc)
* A lot of other things