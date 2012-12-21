require 'spec_helper'

describe Java::IoKatamariEnv::Response do
  before do
    _request = org.jboss.netty.handler.codec.http.DefaultHttpRequest.new(org.jboss.netty.handler.codec.http.HttpVersion::HTTP_1_1, org.jboss.netty.handler.codec.http.HttpMethod::GET, '/favicon.ico')
    _request.content = org.jboss.netty.buffer.ChannelBuffers.copied_buffer('{}', org.jboss.netty.util.CharsetUtil::UTF_8)
    @channel = mock(:channel)
    @event = mock(:event, :channel => @channel, :message => _request)
    
    Java::IoKatamariEnv::Response.__persistent__ = true
    @response = Java::IoKatamariEnv::Response.new(@event)
  end

  describe '#initialize' do
    it 'should set HTTP version 1.1 and status code 200 to the response' do
      expect(@response.status).to eq(org.jboss.netty.handler.codec.http.HttpResponseStatus::OK)
      expect(@response.protocol_version).to eq(org.jboss.netty.handler.codec.http.HttpVersion::HTTP_1_1)
    end

    # it 'should set instance variable @request' do
      
    # end

    # it 'should set instance variable @channel' do

    # end
  end

  describe '#end' do
    it 'should set content' do
      @channel.stub(:write)
      @response.end('Foo')
      expect(@response.content.to_string(org.jboss.netty.util.CharsetUtil::UTF_8)).to eq('Foo')
    end

    it 'should set Content-Length header' do
      @channel.stub(:write)
      @response.end('Foo')
      expect(@response.get_header('Content-Length')).to eq('3')
    end

    it 'should write the response into the channel' do
      @future = mock(:future)
      @channel.should_receive(:write).with(@response).and_return(@future)
      @response.end('Foo')
    end
  end
end