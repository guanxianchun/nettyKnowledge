package com.martin.network.netty.pio.object;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import com.martin.network.netty.bean.User;
import com.martin.network.netty.codec.MarshallingCodeFactory;

/**
 * 
 * @author 管贤春
 * @时间 2017年12月16日 下午3:30:40
 * @Email psyche19830113@163.com
 * @Description 使用Jobss的marshalling进行编解码处理 
 *     当然自己也可以写相应的编解码Handler
 *     使用marshalling首先要在pom中引入marshalling的依赖包
 */
public class NettyUserServer {
	
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
			channel.pipeline()
				.addLast(MarshallingCodeFactory.buildDecoder())  //解码handler
				.addLast(MarshallingCodeFactory.buildEncoder())  //编码handler
				.addLast(new ReadTimeoutHandler(30))     //添加超时handler 
				.addLast(new NettyUserServerHandler());  //数据接收handler
				
		}
	}
	/**
	 * 数据处理类
	 * @author 管贤春
	 * @时间 2017年12月16日 下午4:34:21
	 * @Email psyche19830113@163.com
	 * @Description
	 */
	class NettyUserServerHandler extends ChannelHandlerAdapter{
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			//直接转换成对象
			User user = (User) msg;
			System.out.println("server receive :"+user.toString());
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		NettyUserServer server = new NettyUserServer();
		server.initServer(9000);
	}
}
