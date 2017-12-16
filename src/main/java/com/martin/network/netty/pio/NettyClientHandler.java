package com.martin.network.netty.pio;

import java.net.SocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;

/*
 * @author 管贤春
 * @date 2017年12月15日 下午4:06:32
 * @email psyche19830113@163.com
 */
public class NettyClientHandler extends ChannelOutboundHandlerAdapter {
	
	private String message = "Hello World!";
	
	public NettyClientHandler() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
			ChannelPromise promise) throws Exception {
		System.err.println("connect server.....");
		ctx.write(Unpooled.copiedBuffer(message.getBytes()));
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println("发生异常："+cause.getMessage());
		ctx.close();
	}
	
	
	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		//服务器从缓存中读取客户端发送过来的数据
		System.err.println(ctx.read());
//		ByteBuf buf = (ByteBuf) msg;
//		byte[] req = new byte[buf.readableBytes()];
//		buf.readBytes(req);
//		String body = new String(req, "UTF-8");
//		System.out.println("服务器收到消息内容："+body);
	}

	
}
