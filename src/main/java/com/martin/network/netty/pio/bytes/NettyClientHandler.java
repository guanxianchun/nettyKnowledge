package com.martin.network.netty.pio.bytes;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import com.martin.network.netty.bean.User;

/*
 * @author 管贤春
 * @date 2017年12月15日 下午4:06:32
 * @email psyche19830113@163.com
 */
public class NettyClientHandler extends ChannelHandlerAdapter {
	
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
	
	private void sendJsonData(ChannelHandlerContext ctx) {
		int count = 50;
		String message = "";
		while(count>=0){
			message = new User("martion-"+count, count, 1)+System.getProperty("line.separator");
			ctx.write(Unpooled.copiedBuffer(message.getBytes()));
			count-=1;
		}
		ctx.flush();
	}
	
	private void sendByteData(ChannelHandlerContext ctx) {
		String message ="Hello World..";
		ctx.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("channel active ..........");
		//下面的while是模拟拆包、粘包情况
		this.sendJsonData(ctx);
		//发送流数据
//		this.sendByteData(ctx);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("channel read complete....");
	}
	
}