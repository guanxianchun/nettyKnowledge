package com.martin.network.netty.pio.bytes;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/*
 * @author 管贤春
 * @date 2017年12月15日 下午3:59:36
 * @email psyche19830113@163.com
 */
public class NettyClient {
	/**
	 * 连接到Netty服务器端
	 * @param host
	 * @param port
	 * @throws Exception 
	 */
	public void connect(String host,int port) throws Exception {
		//创建NIO线程组，用于处理网络的读写
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			//创建Netty客户端启动辅助类Bootstrap
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new NettyClientHandler());
						
					}
				});
			//发起异步连接操作
			ChannelFuture future = bootstrap.connect(host, port).sync();
			//异步等待客户端的链路关闭
			
			future.channel().closeFuture().sync();
			
		}finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		NettyClient client = new NettyClient();
		client.connect("127.0.0.1", 9000);
	}
}
