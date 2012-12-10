require 'spec_helper'

describe Katamari::Handler::BodyDecoder do
  it 'should not fail on GET' do
    make_request(org.jboss.netty.handler.codec.http.HttpMethod::GET)
    expect { @handler.messageReceived(@context, @event) }.to_not raise_error
  end

  it 'should set request#params' do
    make_request(org.jboss.netty.handler.codec.http.HttpMethod::POST)
    expect(@event.message.request.params).to eq({})
    @handler.messageReceived(@context, @event)
    expect(@event.message.request.params).to eq('foo' => 'bar')
  end

  def make_request(method)
    _request = org.jboss.netty.handler.codec.http.DefaultHttpRequest.new(org.jboss.netty.handler.codec.http.HttpVersion::HTTP_1_1, method, '/path?id=1')
    _request.content = org.jboss.netty.buffer.ChannelBuffers.copied_buffer('foo=bar', org.jboss.netty.util.CharsetUtil::UTF_8)
    _event = mock(:event, :message => _request)
    _event.stub(:channel)
    @context = mock(:context)
    @event = mock(:event, :message => Katamari::Env.new(_event))
    @handler = Katamari::Handler::BodyDecoder.new
    @context.stub(:send_upstream)
  end
end