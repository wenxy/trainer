package io.netty.example.telnet;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
public class TelnetClientInitializer extends ChannelInitializer<SocketChannel>{
	 private static final StringDecoder DECODER = new StringDecoder();
	       private static final StringEncoder ENCODER = new StringEncoder();
	   
	       private static final TelnetClientHandler CLIENT_HANDLER = new TelnetClientHandler();
	   
	       private final SslContext sslCtx;
	   
	       public TelnetClientInitializer(SslContext sslCtx) {
	           this.sslCtx = sslCtx;
	       }
	   
	       @Override
	       public void initChannel(SocketChannel ch) {
	           ChannelPipeline pipeline = ch.pipeline();
	   
	           if (sslCtx != null) {
	               pipeline.addLast(sslCtx.newHandler(ch.alloc(), TelnetClient.HOST, TelnetClient.PORT));
	           }
	   
	           // Add the text line codec combination first,最大消息长度是8192
	           pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
	           pipeline.addLast(DECODER);
	           pipeline.addLast(ENCODER);
	   
	           // and then business logic.
	           pipeline.addLast(CLIENT_HANDLER);
	       }
}
