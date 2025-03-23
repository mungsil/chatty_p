package playground01;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.HashSet;
import java.util.Set;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Set<Channel> channels = new HashSet<>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        System.out.println("Received: "+ text);

        broadcastMessage(text);
    }

    private void broadcastMessage(String text) {
        for (Channel channel : channels) {
            channel.writeAndFlush(new TextWebSocketFrame(text));
        }
    }

}
