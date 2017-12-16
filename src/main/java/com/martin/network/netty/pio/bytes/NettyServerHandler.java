package com.martin.network.netty.pio.bytes;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/*
 * 服务器消息处理类
 * @author 管贤春
 * @date 2017年12月14日 下午2:45:30
 * @email psyche19830113@163.com
 */
public class NettyServerHandler extends ChannelHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("-->"+Thread.currentThread().getName());
		//服务器从缓存中读取客户端发送过来的数据
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		buf.release();
		String body = new String(req, "UTF-8");
		System.out.println("server receive ："+body);
		//将收到的消息发送给客户端
		body="server echo message :"+body;
		/**
		 * 从性能考虑，为防止频繁的唤醒Selector进行消息发送，Netty的write方法并不直接将消息写入到SocketChannel中
		 * 它只是把消息放到发送缓冲区中，再通过调用flush方法，将缓冲区中的消息全部写到SocketChannel中
		 */
		ctx.write(Unpooled.copiedBuffer(body.getBytes()));
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//将消息发送队列中的消息写入到SocketChannel中发送给对方
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		/**
		 * 发生了异常，关闭ChannelHandlerContext，释放和ChannelHandlerContext相关联的句柄等资源
		 */
		System.err.println("发生异常时，该方法被调用："+cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("新的客户端接入.......");
		super.channelActive(ctx);
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端连接关闭......");
		super.channelUnregistered(ctx);
	}
	
}
