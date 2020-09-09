package com.carrot.bulletchat.config;

import com.carrot.bulletchat.model.CarrotMessage;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author carrot
 */

public class CarrotApplicationContext {
	/**
	 * 存储所有连接的 channel
	 *
	 */
	public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	/**
	 * String 是用户名 Long 是房间号，Channel 指房间号对应的通道
	 */
	public static Map<Long, Map<String, Channel>> roomChannelMap = new ConcurrentHashMap<>();


	/**
	 * 存储channelId和用户之间的关系,便于离开时方便清除相应通道
	 */
	public static Map<String, CarrotMessage> userChannelMap = new ConcurrentHashMap<String, CarrotMessage>();

	public static String host="127.0.0.1";

	public static int port=9999;


}
