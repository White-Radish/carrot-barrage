package com.carrot.bulletchat.model;

/**
 * @author carrot
 */
public enum MsgType {
	/**
	 * 离开房间
	 */
	LEAVE(1),
	/**
	 * 离开房间消息响应
	 */
	LEAVE_RESPONSE(1001),
	/**
	 * 进入房间
	 */
	COME(2),
	/**
	 * 欢迎进入房间消息响应
	 */
	COME_RESPONSE(1002),
	/**
	 * 通知房间内其他人有人进入房间
	 */
	BROADCAST_COME_RESPONSE(2002),
	/**
	 * 收到消息
	 */
	RCV_MSG(3),
	/**
	 * 收到消息响应
	 */
	RCV_MSG_RESPONSE(1003),
	/**
	 * 广播消息
	 */
	BROADCAST(4);
	int value;
	MsgType(int i) {
		this.value=i;
	}

	public int getValue() {
		return value;
	}
}
