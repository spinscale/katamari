# https://github.com/ngocdaothanh/xitrum/commit/31c727f9c125239ef3e25cf1edf2eb3ae24b6515
class Katamari::Handler::NoPipelining < Katamari::Handler::Upstream
  def messageReceived(ctx, e)
    ctx.channel.set_readable(false)
    ctx.send_upstream(e)
  end
end