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
 * Netty服务器端的开发步骤：
 *  1. 创建二个NIO线程组，分别用于接受客户的连接和网络读写
 *  2. 创建Netty用于启动NIO服务器端的辅助类ServerBootstrap类
 *  3. 将二个NIO线程组当作参数传入到NIO服务器端的辅助类 ServerBootstrap中
 *  4. 指定创建Channel对象的类NioServerSocketChannel
 *  5. 配置NioServerSocketChannel的TCP参数  backlog
 *  6. 绑定I/O事件的处理类ChildChannelHandler
 *  7. 绑定端口
 *  8. 异步等待服务器监听端口的关闭
 */
public class NettyServer {
	
	public void initServer(int port) throws Exception {
		//创建二个线程组，分别用于接受客户端的连接和网络读写
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			//创建NIO服务端的辅助启动类
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workGroup)                  //将NIO两个线程组放入到bootstrap中
						.channel(NioServerSocketChannel.class)     //创建Channel为NioServerSocketChannel
						.option(ChannelOption.SO_BACKLOG, 1024)    //配置NioServerSocketChannel的TCP参数，此处将backlog设置1024
						.childHandler(new ChildChannelHandler());  //绑定I/O事件的处理类ChildChannelHandler
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
			//添加消息处理类
			channel.pipeline().addLast(new NettyServerHandler());
		}
	}
	
	public static void main(String[] args) throws Exception {
		NettyServer server = new NettyServer();
		server.initServer(9000);
	}
}
