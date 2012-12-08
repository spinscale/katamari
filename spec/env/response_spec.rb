require 'spec_helper'

describe Katamari::Env::Response do
  before do
    @event = mock('event')
    @event.stub(:channel)
    @event.stub(:message)

    @response = Katamari::Env::Response.new(@event)
  end

  it 'should #initialize with default values' do
    expect(@response.status).to eq(200)
    expect(@response.headers).to eq('Content-Length' => 0)
    expect(@response.body).to be_empty
  end

  it 'should have accessors for status, headers, body' do
    expect(@response.status).to eq(200)
    @response.status = 302
    expect(@response.status).to eq(302)

    expect(@response.headers).to eq('Content-Length' => 0)
    @response.headers['Content-Type'] = 'text/html'
    expect(@response.headers).to eq('Content-Length' => 0, 'Content-Type' => 'text/html')

    expect(@response.body).to be_empty
    @response.body = 'foobar'
    expect(@response.body).to eq('foobar')
  end

  # it 'should write the content into a Netty HttpResponse' do
  #   channel = mock('channel')
  #   @ctx.stub(:channel).and_return(channel)
  #   channel.should_receive(:write).and_return(env)
  #   @response.write('Hello World!')
  #   expect(@response.body).to eq('Hello World!')
  # end
end