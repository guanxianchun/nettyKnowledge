package com.martin.network.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.msgpack.MessagePack;
/**
 * 
 * @author 管贤春
 * @时间 2017年12月16日 下午7:49:05
 * @Email psyche19830113@163.com
 * @Description
 *   使用MessagePack首先要引入包
 *   在使用时，为防止粘包/拆包情况，首先要使用LengthFieldPrepender
 *   pip.addLast(new LengthFieldPrepender(2))
			.addLast(new MessagePackEncoder())
 *     
 */
public class MessagePackEncoder extends MessageToByteEncoder<Object>{
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		//创建MessagePack对象
		MessagePack msgPack = new MessagePack();
		//将对象转化字节数组
		byte[] raw = msgPack.write(msg);
		//将字节写入到ByteBuff中
		out.writeBytes(raw);
		
	}
}
