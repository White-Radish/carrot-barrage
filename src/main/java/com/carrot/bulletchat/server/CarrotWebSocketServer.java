package com.carrot.bulletchat.server;

import com.carrot.bulletchat.config.CarrotApplicationContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author carrot
 */
@Component
public class CarrotWebSocketServer {

	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private final EventLoopGroup workGroup = new NioEventLoopGroup();
	private Channel channel;

	public ChannelFuture start(InetSocketAddress address) throws InterruptedException {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new CarrotServerInitializer())
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);

		ChannelFuture future = bootstrap.bind(address).sync();
		channel = future.channel();
		System.out.println("netty 服务已启动");
		return future;
	}

	public void destroy() {
		if(channel != null) {
			channel.close();
		}
		CarrotApplicationContext.group.close();
		workGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}
}
