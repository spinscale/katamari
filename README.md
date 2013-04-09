[![Build Status](https://secure.travis-ci.org/nmerouze/katamari.png)](http://travis-ci.org/nmerouze/katamari)

# Katamari (塊) Polyglot Asynchronous Web Server and Middleware Framework

Katamari is a Web Server and a Middleware Framework inspired by Rack (Ruby), Connect.js (Node.js), WSGI (Python) and Ring (Clojure). The asynchronous part is handled by [Netty](http://netty.io). And it is polyglot: written in Java with bindings for other languages (currently only Ruby).

It is a work in progress and many things will change. There will be more middlewares, enough to easily create a JSON API with authentication and the basic features needed for it. Middlewares will optionally be bound to routes. There will be bindings in Javascript, Clojure, Python, Scala and more.

# Bindings

* Ruby: http://github.com/nmerouze/katamari-ruby

# Run

    mvn package
    mvn exec:java