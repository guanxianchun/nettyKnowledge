package com.martin.network.netty.nio;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
/**
 * 
 * @Description 
 * @author 管贤春
 * @date 2017年12月15日 下午3:32:01
 * @email psyche19830113@163.com
 * 
 * NIO服务器端的开发步骤：
 * 	1. 创建ServerSocketChannel对象，配置为非阻塞模式
 *  2. 绑定监听端口 配置TCP参数
 *  3. 创建Selector，将之前创建的ServerSocketChannel注册到Selector上，监听SelectionKey.ACCEPT
 *  4. 执行selector.select()方法，轮询就绪的Channel
 *  5. 当轮询到就绪的Channel时，对其进行判断，如果是OP_ACCEPT状态，说明有新客户端接入，则调用ServerSocketChannel的
 *     accept()方法接受新的客户端
 *  6. 设置新接入的客户端链路为非阻塞模式，可配置其他一些TCP参数
 *  7. 将SocketChannel注册到Selector上，监听OP_READ操作位
 *  8. 如果轮询到Channel为OP_READ，则说明SocketChannel中有的新就绪数据包需要读取，则构造ByteBuffere对象,读取数据包
 *  9. 如果轮询到Channel为OP_WRITE,则说明还有数据没有发送完成，需要继续发送
 */
public class NioServer {

	// 通道管理器
	private Selector selector;
	private boolean stop = false;
	public void initServer(int port) throws IOException {
		// 获取一个ServerSocket通道
		ServerSocketChannel channel = ServerSocketChannel.open();
		// 设置为非阻塞模式
		channel.configureBlocking(false);
		// 绑定服务监听端口,并设置TCP参数 backlog=1024
		channel.socket().bind(new InetSocketAddress(port),1024);
		// 获取一个通道管理器
		this.selector = Selector.open();
		// 将通道管理器与通道绑定,并为该通道注册SelectionKey.OP_ACCEPT事件
		// 注册事件后，当事到达时，Selector.select()就会返回，如果没有事件到达会一直阻塞
		channel.register(this.selector, SelectionKey.OP_ACCEPT);
	}

	public void listen() throws IOException {
		System.out.println("服务器端启动成功！");
		// 轮询Selector
		while (true && ! this.stop) {
			// 当注册事件到达时，访问返回，否则该方法会一直阻塞
			this.selector.select();
			// 获得Selector中选中项的迭代器，为选中的项注册事件
			Iterator<?> iterator = this.selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = (SelectionKey) iterator.next();
				iterator.remove();
				handler(key);
				
			}
		}
		if (this.selector !=null) {
			try {
				this.selector.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void handler(SelectionKey key) throws IOException {
		if (key.isAcceptable()) {
			handlerAccept(key);
		} else if (key.isReadable()) {
			handlerRead(key);
		}
	}

	/**
	 * 处理读操作
	 * 
	 * @param key
	 * @throws IOException
	 */
	public void handlerRead(SelectionKey key) throws IOException {
		// 服务器可读取消息，获取事件发生的SocketChannel
		SocketChannel channel = (SocketChannel) key.channel();
		// 创建一个缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int readBytes = channel.read(buffer);
		if (readBytes>0) {
			buffer.flip();
			byte[] bytes = new byte[buffer.remaining()];
			buffer.get(bytes);
			String message = new String(bytes, "UTF-8");
			System.out.println("服务器收到消息：" + message);
			message="<=="+message+"\r";
			ByteBuffer out = ByteBuffer.wrap(message.getBytes());
			// 将消息返回给客户端
			channel.write(out);
		}else if (readBytes<0) {
			//对端链路关闭  客户端连接关闭了
			System.out.println("一个客户端连接关闭了...");
			key.cancel();
			key.channel().close();
		}
		
	}

	public void handlerAccept(SelectionKey key) throws IOException {
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		// 获得和客户端连接的通道
		SocketChannel channel = server.accept();
		// 设置成非阻塞
		channel.configureBlocking(false);
		// 在这里可以给客户端发送信息哟
		System.out.println("新的客户端连接");
		channel.register(selector, SelectionKey.OP_READ);
	}

	public static void main(String[] args) throws IOException {
		NioServer server = new NioServer();
		server.initServer(9000);
		server.listen();
	}
}
