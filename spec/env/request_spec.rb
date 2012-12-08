require 'spec_helper'

describe Katamari::Env::Request do
  before do
    _request = org.jboss.netty.handler.codec.http.DefaultHttpRequest.new(org.jboss.netty.handler.codec.http.HttpVersion::HTTP_1_1, org.jboss.netty.handler.codec.http.HttpMethod::GET, '/favicon.ico')
    _request.content = org.jboss.netty.buffer.ChannelBuffers.copied_buffer('{}', org.jboss.netty.util.CharsetUtil::UTF_8)
    @request = Katamari::Env::Request.new(_request)
  end

  it 'should have a #uri accessor' do
    expect(@request.uri).to eq('/favicon.ico')
    @request.uri = '/index.html'
    expect(@request.uri).to eq('/index.html')
  end

  it 'should have a #method accessor' do
    expect(@request.method).to eq(:get)
    @request.method = :post
    expect(@request.method).to eq(:post)
  end

  it 'should have a #headers accessor' do
    expect(@request.headers).to be_a(Hash)
    expect(@request.headers).to be_empty
    @request.headers['Accept'] = 'application/json'
    expect(@request.headers).to_not be_empty
  end

  it 'should have a #body accessor' do
    expect(@request.body).to eq('{}')
    @request.body = '{"foo":"bar"}'
    expect(@request.body).to eq('{"foo":"bar"}')
  end

  it 'should have a #path accessor' do
    expect(@request.path).to eq(nil)
    @request.path = '/'
    expect(@request.path).to eq('/')
  end

  it 'should have a #params accessor' do
    expect(@request.params).to eq(nil)
    @request.params = { 'foo' => 'bar' }
    expect(@request.params).to eq('foo' => 'bar')
  end
end