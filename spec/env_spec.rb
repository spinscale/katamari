require 'spec_helper'

import org.jboss.netty.handler.codec.http.HttpRequest;
#import io.katamari.env.Response;

describe Java::IoKatamari::Env do
  before do
    _request = org.jboss.netty.handler.codec.http.DefaultHttpRequest.new(org.jboss.netty.handler.codec.http.HttpVersion::HTTP_1_1, org.jboss.netty.handler.codec.http.HttpMethod::GET, '/favicon.ico')
    _request.content = org.jboss.netty.buffer.ChannelBuffers.copied_buffer('{}', org.jboss.netty.util.CharsetUtil::UTF_8)
    @event = mock(:event, message: _request)
    @event.stub(:channel)
    @env = Java::IoKatamari::Env.new(@event)
  end

  it 'should have a default response' do
    expect(@env.response).to be_a(Java::IoKatamariEnv::Response)
  end

  it 'should have a request' do
    expect(@env.request).to be_a(HttpRequest)
  end
end