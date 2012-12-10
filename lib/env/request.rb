class Katamari::Env::Request
  import org.jboss.netty.util.CharsetUtil
  
  attr_accessor :headers, :uri, :body, :method, :path, :params

  def initialize(request)
    @_request, @headers, @params, @uri, @method, @body = request, {}, {}, request.uri, request.method.name.downcase.to_sym, request.content.to_string(0, request.content.readable_bytes, CharsetUtil::UTF_8)
    @_request.headers.each { |h| @headers[h.key] = h.value }
  end

  def origin
    @_request
  end
end