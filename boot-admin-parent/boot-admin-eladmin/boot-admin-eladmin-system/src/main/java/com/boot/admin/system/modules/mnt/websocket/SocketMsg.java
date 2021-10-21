package com.boot.admin.system.modules.mnt.websocket;

import lombok.Data;

/**
 * <p>SocketMsg class.</p>
 *
 * @author ZhangHouYing
 * @since 2019-08-10 9:55
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class SocketMsg {
	private String msg;
	private MsgType msgType;

	/**
	 * <p>Constructor for SocketMsg.</p>
	 *
	 * @param msg a {@link java.lang.String} object.
	 * @param msgType a {@link com.boot.admin.system.modules.mnt.websocket.MsgType} object.
	 */
	public SocketMsg(String msg, MsgType msgType) {
		this.msg = msg;
		this.msgType = msgType;
	}
}
