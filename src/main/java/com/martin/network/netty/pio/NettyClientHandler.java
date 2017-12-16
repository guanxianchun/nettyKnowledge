package com.martin.network.netty.pio;

import java.net.SocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;

/*
 * @author 管贤春
 * @date 2017年12月15日 下午4:06:32
 * @email psyche19830113@163.com
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
	
	private String message = "Hello World!";
	
	public NettyClientHandler() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel关闭时调用..");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println("发生异常："+cause.getMessage());
		ctx.close();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] readByte = new byte[buf.readableBytes()];
		buf.readBytes(readByte);
		buf.release();
		System.out.println("client receive message:"+new String(readByte, "UTF-8"));
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("channel active ..........");
		ctx.writeAndFlush(Unpooled.copiedBuffer("Hello World".getBytes()));
		//下面是添加一个监听器，当ChannelFuture完成时关闭channel
//		.addListener(ChannelFutureListener.CLOSE);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("channel read complete....");
	}
	
}