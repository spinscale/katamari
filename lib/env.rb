class Katamari::Env
  autoload :Response, 'env/response'
  autoload :Request, 'env/request'
  
  attr_reader :request, :response

  def initialize(event)
    @request = Katamari::Env::Request.new(event.message)
    @response = Katamari::Env::Response.new(event)
  end
end