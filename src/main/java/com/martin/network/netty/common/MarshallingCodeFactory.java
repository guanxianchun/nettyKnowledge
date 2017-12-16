package com.martin.network.netty.common;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

public class MarshallingCodeFactory {
	/**
	 * 获取MarshallerFactory工厂对象
	 * @return
	 */
	public static MarshallerFactory getMarshallerFactory() {
		/**
		 * 通过Marshalling工具类获取Marshalling实例对象
		 * 参数serial标识创建的是Java序列化工厂对象
		 */
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		return marshallerFactory;
	}
	/**
	 * 获取MarshallingConfiguration对象
	 * @param version
	 * @return
	 */
	public static MarshallingConfiguration getMarshallingConfiguration(int version) {
		final MarshallingConfiguration config = new MarshallingConfiguration();
		config.setVersion(version);
		return config;
	}
	
	/**
	 * 生成一个解码器对象
	 * @return
	 */
	public static MarshallingDecoder buildDecoder() {
		//构建Netty的MarshallingDecoder对象
		UnmarshallerProvider provider = new DefaultUnmarshallerProvider(getMarshallerFactory(), getMarshallingConfiguration(5));
		return new MarshallingDecoder(provider,10240);
	}
	
	public static MarshallingEncoder buildEncoder() {
		return new MarshallingEncoder(new DefaultMarshallerProvider(getMarshallerFactory(),getMarshallingConfiguration(5)));
	}
}
