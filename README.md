# Katamari (塊) Asynchronous Middleware Framework

Rack unified web development in Ruby. It has helped us well but its simplistic API is limiting features such as streaming. [Rails 4.0 has worked around these limits but it's not perfect](http://blog.plataformatec.com.br/2012/06/why-your-web-framework-should-not-adopt-rack-api). Katamari aims to create a new middleware framework with asynchrony in its core, using [Netty](http://netty.io) to achieve great scalability.

WSGI in Python and Ring in Clojure share more or less the same specification than Rack. Since Katamari is based on the JVM, it could be used in Jython or Clojure, but there still need some work.

# Run (JRuby 1.7.x required)

Run the examples:

    git clone git://github.com/nmerouze/katamari.git
    cd katamari
    curl http://cloud.github.com/downloads/nmerouze/katamari/netty-3.5.11.Final.jar > java_lib/netty-3.5.11.Final.jar
    ruby examples/hello_world.rb

And in another terminal:

    curl http://localhost:8080/

# Roadmap

Java branch:

+ Fluent interface for #add and #listen
+ Refactoring of BodyDecoder and URIDecoder
* Add tests (+ use Travis CI)
* Remove HttpChunkAggregator and enable Chunked Transfer Encoding
* Update to Netty 4
* Better API

Agnostic branch:

* Extract the Ruby code to katamari-ruby
* Create a katamari-clj

General:

* Add more middlewares