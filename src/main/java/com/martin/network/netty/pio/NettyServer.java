package com.martin.network.netty.pio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/*
 * @author 管贤春
 * @date 2017年12月14日 下午2:35:13
 * @email psyche19830113@163.com
 */
public class NettyServer {
	
	public void initServer(int port) throws Exception {
		//创建二个线程组，分别用于接受客户端的连接和网络读写
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			//创建NIO服务端的辅助启动类
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workGroup)
						.channel(NioServerSocketChannel.class)
						.option(ChannelOption.SO_BACKLOG, 1024)
						.childHandler(new ChildChannelHandler());
			//绑定端口 同步等待成功
			ChannelFuture future = bootstrap.bind(port).sync();
			//异步等待服务器监听端口关闭
			future.channel().closeFuture().sync();
		}finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
		@Override
		protected void initChannel(SocketChannel channel) throws Exception {
			channel.pipeline().addLast(new NettyServerHandler());
		}
	}
}
