package com.martin.network.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import org.msgpack.MessagePack;
/**
 * 
 * @author 管贤春
 * @时间 2017年12月16日 下午8:04:47
 * @Email psyche19830113@163.com
 * @Description
 *   自定义MessagePack的解码器 MessagePackDecoder
 *   在使用MessagePackDecoder解码器，为防止粘包/拆包情况,
 *   首先要添加LengthFieldBasedFrameDecoder处理handler
 *   ChannelPipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
			.addLast("MessagePack Decoder",new MessagePackDecoder())
 */
public class MessagePackDecoder extends MessageToMessageDecoder<ByteBuf>{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		//从缓冲区中读取数据
		final byte[] readBytes;
		final int length = msg.readableBytes();
		readBytes = new byte[length];
		msg.readBytes(readBytes);
		//创建MessagePack对象
		MessagePack msgPack = new MessagePack();
		//将生成的对象放到集合中
		out.add(msgPack.read(readBytes));
		
	}
}
