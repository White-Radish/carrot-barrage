package com.carrot.bulletchat;

import com.carrot.bulletchat.config.CarrotApplicationContext;
import com.carrot.bulletchat.server.CarrotWebSocketServer;
import com.carrot.bulletchat.service.IUserService;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

import java.net.InetSocketAddress;

@SpringBootApplication
public class BulletChatApplication implements CommandLineRunner {

	@Autowired
	private CarrotWebSocketServer ws;


	public static void main(String[] args) {
		SpringApplication.run(BulletChatApplication.class, args);
	}
	@Override
	public void run(String... args) throws InterruptedException {
		InetSocketAddress address = new InetSocketAddress(CarrotApplicationContext.host, CarrotApplicationContext.port);
		ChannelFuture future = ws.start(address);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> ws.destroy()));
		future.channel().closeFuture().sync();

	}
}
