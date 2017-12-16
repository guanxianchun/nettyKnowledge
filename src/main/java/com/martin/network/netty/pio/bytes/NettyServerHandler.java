package com.martin.network.netty.pio.bytes;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/*
 * 服务器消息处理类
 * @author 管贤春
 * @date 2017年12月14日 下午2:45:30
 * @email psyche19830113@163.com
 */
public class NettyServerHandler extends ChannelHandlerAdapter {
	/**
	 * 处理TCP粘包和拆包
	 */
	private void processPackageSplice(ChannelHandlerContext ctx,Object msg) {
		System.out.println("server receive message :"+msg);
	}
	
	private void processStream(ChannelHandlerContext ctx,Object msg) throws Exception {
		//服务器从缓存中读取客户端发送过来的数据
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		buf.release();
		String body = new String(req, "UTF-8");
		System.out.println("server receive ："+body);
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("-->"+Thread.currentThread().getName());
		//TCP粘包和拆包数据接收处理
		this.processPackageSplice(ctx,msg);
		//处理流数据
//		this.processStream(ctx,msg);
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
