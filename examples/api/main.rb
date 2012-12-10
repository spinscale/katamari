require File.expand_path('../../../lib/katamari', __FILE__)
require 'bundler'
Bundler.setup
require 'sequel'
require 'json'

DB = Sequel.connect('jdbc:h2:mem:')

DB.create_table :posts do
  primary_key :id
  String :title
end

class BounceFaviconHandler < Katamari::Handler::Upstream
  def messageReceived(ctx, e)
    env = e.message

    if env.request.path == '/favicon.ico'
      env.response.status = 404
      env.response.headers['Content-Type'] = 'text/html'
      env.response.end
    else
      ctx.send_upstream(e)
    end
  end
end

class GetHandler < Katamari::Handler::Upstream
  def messageReceived(ctx, e)
    env = e.message

    if env.request.path == '/posts' && env.request.method == :get
      posts = DB.from(:posts).all
      env.response.headers['Content-Type'] = 'application/json'
      env.response.body = JSON(posts)
      env.response.end
    else
      ctx.send_upstream(e)
    end
  end
end

class PostHandler < Katamari::Handler::Upstream
  def messageReceived(ctx, e)
    env = e.message

    if env.request.path == '/posts' && env.request.method == :post
      id = DB.from(:posts).insert(env.request.params)
      post = DB.from(:posts).filter(id: id).first
      env.response.headers['Content-Type'] = 'application/json'
      env.response.body = JSON(post)
      env.response.end
    else
      ctx.send_upstream(e)
    end
  end
end

class RootHandler < Katamari::Handler::Upstream
  def messageReceived(ctx, e)
    env = e.message
    env.response.headers['Content-Type'] = 'text/html'
    env.response.body = 'Home page!'
    env.response.end
  end
end

# curl --data-urlencode "title=Bar" http://localhost:8080/posts
# curl http://localhost:8080/posts

Katamari.use(Katamari::Handler::URIDecoder).use(Katamari::Handler::BodyDecoder).use(BounceFaviconHandler).use(GetHandler).use(PostHandler).use(RootHandler).listen(8080)