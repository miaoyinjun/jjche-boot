package com.boot.admin.system.modules.mnt.websocket;

/**
 * <p>MsgType class.</p>
 *
 * @author ZhangHouYing
 * @since 2019-08-10 9:56
 * @version 1.0.8-SNAPSHOT
 */
public enum MsgType {
	/** 连接 */
	CONNECT,
	/** 关闭 */
	CLOSE,
	/** 信息 */
	INFO,
	/** 错误 */
	ERROR
}
