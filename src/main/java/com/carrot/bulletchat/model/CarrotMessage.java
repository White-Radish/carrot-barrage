package com.carrot.bulletchat.model;

/**
 * @author carrot
 */
public class CarrotMessage {

    private int msgType;
    private String userName;
    private long roomId;
    private String msgBody;

    public CarrotMessage(int msgType, String userName) {
        this.msgType = msgType;
        this.userName = userName;

    }

    public CarrotMessage(int msgType, String userName, String msgBody, long roomId) {
        this.msgType = msgType;
        this.userName = userName;
        this.msgBody=msgBody;
        this.roomId=roomId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

}
