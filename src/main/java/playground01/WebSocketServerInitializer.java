package playground01;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

/**
 * take care of the WebSocket handshake and HTTP-to-WebSocket upgrade process
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private final String webSocketPath;

    public WebSocketServerInitializer(String webSocketPath) {
        this.webSocketPath = webSocketPath;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec()); //  decoding and encoding for incoming requests
        pipeline.addLast(new HttpObjectAggregator(65536)); // aggregates multiple HTTP messages into a single FullHttpRequest or FullHttpResponse
        pipeline.addLast(new WebSocketServerCompressionHandler()); // handles WebSocket per-message compression
        pipeline.addLast(new WebSocketServerProtocolHandler(webSocketPath, null, true)); // takes care of websocket handshaking as well as processing of control frames
        pipeline.addLast(new WebSocketFrameHandler());
    }

}
