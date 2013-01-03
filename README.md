# Katamari (塊) Polyglot Asynchronous Middleware Framework

Katamari is a Middleware Framework inspired by Rack (Ruby), Connect.js (Node.js), WSGI (Python) and Ring (Clojure). The asynchronous part is handled by [Netty](http://netty.io). And it is polyglot: written in Java with bindings for other languages (currently only Ruby).

It is a work in progress and many things will change. There will be more middlewares, enough to easily create a JSON API with authentication and the basic features needed for it. Middlewares will be optionally be bound to routes. There will be bindings in Javascript, Clojure, Python, Scala and more.

# Run (Ruby bindings, JRuby 1.7.x required)

Run the examples:

    git clone git://github.com/nmerouze/katamari.git
    cd katamari
    curl http://cloud.github.com/downloads/nmerouze/katamari/netty-3.5.11.Final.jar > javalib/netty-3.5.11.Final.jar
    mvn package
    ruby server.rb

And in another terminal:

    curl http://localhost:8082/

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
* Add a router