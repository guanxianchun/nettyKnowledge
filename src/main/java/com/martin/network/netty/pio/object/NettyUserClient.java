package com.martin.network.netty.pio.object;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import com.martin.network.netty.bean.User;
import com.martin.network.netty.common.MarshallingCodeFactory;

public class NettyUserClient {
	/**
	 * 连接到Netty服务器端
	 * 
	 * @param host
	 * @param port
	 * @throws Exception
	 */
	public void connect(String host, int port) throws Exception {
		// 创建NIO线程组，用于处理网络的读写
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			// 创建Netty客户端启动辅助类Bootstrap
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(MarshallingCodeFactory.buildDecoder())
								.addLast(MarshallingCodeFactory.buildEncoder())
								.addLast(new ReadTimeoutHandler(30))
								.addLast(new NettyUserClientHandler());	

						}
					});
			// 发起异步连接操作
			ChannelFuture future = bootstrap.connect(host, port).sync();
			// 异步等待客户端的链路关闭
			future.channel().closeFuture().sync();

		} finally {
			group.shutdownGracefully();
		}
	}

	class NettyUserClientHandler extends ChannelHandlerAdapter {

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("Channel关闭时调用..");
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			System.err.println("发生异常：" + cause.getMessage());
			ctx.close();
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			System.out.println("client receive message:" + msg.toString());
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("channel active ..........");
			//通道连接成功后发送一个消息数据
			//模拟粘包和拆包情况
			int count = 5;
			while (count>=0) {
				ctx.write(new User("gxc_"+count, 35, 1));
				count-=1;
			}
			ctx.flush();
			count = 10000;
			while (count>=0) {
				ctx.write(new User("gxc_"+count, 35, 1));
				count-=1;
			}
			ctx.flush();
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("channel read complete....");
		}

	}

	public static void main(String[] args) throws Exception {
		NettyUserClient client = new NettyUserClient();
		client.connect("127.0.0.1", 9000);
	}
}
