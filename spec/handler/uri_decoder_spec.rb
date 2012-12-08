require 'spec_helper'

describe Katamari::Handler::URIDecoder do
  before do
    _request = org.jboss.netty.handler.codec.http.DefaultHttpRequest.new(org.jboss.netty.handler.codec.http.HttpVersion::HTTP_1_1, org.jboss.netty.handler.codec.http.HttpMethod::GET, '/path?id=1')
    _request.content = org.jboss.netty.buffer.ChannelBuffers.copied_buffer('{}', org.jboss.netty.util.CharsetUtil::UTF_8)
    _event = mock(:event, :message => _request)
    _event.stub(:channel)
    @context = mock(:context)
    @event = mock(:event, :message => Katamari::Env.new(_event))
    @handler = Katamari::Handler::URIDecoder.new
    @context.stub(:send_upstream)
  end

  it 'should set request#path' do
    expect(@event.message.request.path).to eq(nil)
    @handler.messageReceived(@context, @event)
    expect(@event.message.request.path).to eq('/path')
  end

  it 'should set request#params' do
    expect(@event.message.request.params).to eq(nil)
    @handler.messageReceived(@context, @event)
    expect(@event.message.request.params).to eq('id' => '1')
  end
end