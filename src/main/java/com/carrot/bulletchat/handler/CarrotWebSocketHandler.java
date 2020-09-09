package com.carrot.bulletchat.handler;

import com.carrot.bulletchat.common.CarrotConstant;
import com.carrot.bulletchat.common.SpringBeanUtil;
import com.carrot.bulletchat.config.CarrotApplicationContext;
import com.carrot.bulletchat.domain.User;
import com.carrot.bulletchat.model.CarrotMessage;
import com.carrot.bulletchat.model.MsgType;
import com.carrot.bulletchat.service.IUserService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author carrot
 */
public class CarrotWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>  {

	private IUserService iUserService;
	//正常的属性注入无法生效，需要用上下文获取
	public CarrotWebSocketHandler() {
		this.iUserService = SpringBeanUtil.getBean(IUserService.class);
	}

	/**
	 * 有连接进来调用
	 * @param channelHandlerContext
	 * @param msg
	 * @throws Exception
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) {

	}


	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) {
			FullHttpRequest request = (FullHttpRequest) msg;
			String uri = request.uri();
			//获取ws的url后面带的参数
			Map paramMap=getUrlParams(uri);
//			System.out.println("接收到的参数是："+ CarrotConstant.gson.toJson(paramMap));
			//如果url包含参数，需要处理
			if(uri.contains("?")){
				String newUri=uri.substring(0,uri.indexOf("?"));
				System.out.println(newUri);
				if("666".equals(paramMap.get("uid"))){
					User user = iUserService.getUser(Long.parseLong((String) paramMap.get("uid")));
					if(user!=null){
						System.out.println("用户存在可以发送弹幕");
						request.setUri(newUri);
					}
				}
				else {
					System.out.println("验证不通过，通道关闭");
					ctx.close();
				}

			}
		}else if(msg instanceof TextWebSocketFrame){
			dealMessage(ctx,(TextWebSocketFrame) msg);
		}
		super.channelRead(ctx, msg);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx)  {

		CarrotApplicationContext.group.add(ctx.channel());
	}

	// onclose
	// Invoked when a Channel leaves active state and is no longer connected to its remote peer.
	// 当连接要关闭时
	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		System.out.println("客户端关闭");
		String channelId = ctx.channel().id().asLongText();
		CarrotMessage message = CarrotApplicationContext.userChannelMap.get(channelId);
		if(message==null){
			return;
		}
		message.setMsgType(MsgType.LEAVE_RESPONSE.getValue());
		removeGroupText( message);
		CarrotApplicationContext.group.remove(ctx.channel());
		CarrotApplicationContext.userChannelMap.keySet().removeIf(key->key.equals(channelId));
		CarrotApplicationContext.roomChannelMap.get(message.getRoomId()).keySet().removeIf(key->key.equals(message.getUserName()));
	}


	// 发生异常时
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	private void dealMessage(ChannelHandlerContext ctx, WebSocketFrame msg) {

			TextWebSocketFrame message = (TextWebSocketFrame) msg;
			// 文本消息
			CarrotMessage carrotMessage = CarrotConstant.gson.fromJson(message.text(), CarrotMessage.class);
			//房间号
			long roomId=carrotMessage.getRoomId();
			if(!CarrotApplicationContext.roomChannelMap.containsKey(roomId)){
				Map<String, Channel> userChannelMap = new HashMap<>();
				userChannelMap.put(carrotMessage.getUserName(),ctx.channel());
				CarrotApplicationContext.roomChannelMap.put(roomId,userChannelMap);
			}else{
				Map<String, Channel> stringChannelMap = CarrotApplicationContext.roomChannelMap.get(roomId);
				if(!stringChannelMap.containsKey(carrotMessage.getUserName())){
					stringChannelMap.put(carrotMessage.getUserName(),ctx.channel());
				}
			}

			String channelId=ctx.channel().id().asLongText();
			boolean hasChannel = CarrotApplicationContext.userChannelMap.containsKey(channelId);

			if(!hasChannel){
				CarrotApplicationContext.userChannelMap.put(channelId,carrotMessage);
			}
			switch (carrotMessage.getMsgType()){
				// 进入房间
				case 2:

					ctx.channel().writeAndFlush( new TextWebSocketFrame( CarrotConstant.gson.toJson(new CarrotMessage(MsgType.COME_RESPONSE.getValue(), carrotMessage.getUserName()))));
					broadCastTextButSelf( new CarrotMessage(MsgType.BROADCAST_COME_RESPONSE.getValue(), carrotMessage.getUserName(), carrotMessage.getMsgBody(),carrotMessage.getRoomId()));
					break;

				// 发送消息
				case 3:
					//向所有人发送消息
					System.out.println("发送消息");
					broadCastText( new CarrotMessage(MsgType.RCV_MSG_RESPONSE.getValue(), carrotMessage.getUserName(), carrotMessage.getMsgBody(),carrotMessage.getRoomId()) );
					break;

				// 离开房间
				case 1:
					System.out.println("离开房间");
					removeGroupText(  new CarrotMessage(MsgType.LEAVE_RESPONSE.getValue(), carrotMessage.getUserName(), carrotMessage.getMsgBody(),carrotMessage.getRoomId()) );
					break;

				default:
					break;
			}
	}


	//房间满了只给自己发消息
	private void sendSelfText(Channel channel, CarrotMessage msg) {
		channel.writeAndFlush( new TextWebSocketFrame( CarrotConstant.gson.toJson( msg )));
	}

	/**
	 * 发送消息，除了自己
	 * @param msg
	 */
	private void broadCastTextButSelf(CarrotMessage msg) {
		CarrotApplicationContext.roomChannelMap.get(msg.getRoomId()).entrySet().stream()
				.filter(x->!x.getKey().equals(msg.getUserName()))
				.forEach(h->{
					h.getValue().writeAndFlush( new TextWebSocketFrame( CarrotConstant.gson.toJson( msg )));
				});
	}

	/**
	 * 往当前房间发送消息
	 * @param msg
	 */
	private void broadCastText(CarrotMessage msg) {
		CarrotApplicationContext.roomChannelMap.get(msg.getRoomId()).forEach((key, value) -> value.writeAndFlush(new TextWebSocketFrame(CarrotConstant.gson.toJson(msg))));
	}

	/**
	 * 离开房间发送消息
	 * @param msg
	 */
	private void removeGroupText(CarrotMessage msg) {
		broadCastTextButSelf(msg);
		CarrotApplicationContext.roomChannelMap.get(msg.getRoomId()).keySet().removeIf(key->key.equals(msg.getUserName()));
	}

	private static Map getUrlParams(String url){
		Map<String,String> map = new HashMap<>();
		url = url.replace("?",";");
		if (!url.contains(";")){
			return map;
		}
		if (url.split(";").length > 0){
			String[] arr = url.split(";")[1].split("&");
			for (String s : arr){
				String key = s.split("=")[0];
				String value = s.split("=")[1];
				map.put(key,value);
			}
			return  map;

		}else{
			return map;
		}
	}
}
