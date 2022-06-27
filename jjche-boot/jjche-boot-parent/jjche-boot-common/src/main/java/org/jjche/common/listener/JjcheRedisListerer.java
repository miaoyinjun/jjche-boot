package org.jjche.common.listener;


import org.jjche.common.base.BaseMap;

/**
 * <p>
 * 自定义消息监听
 * </p>
 *
 * @author miaoyj
 * @since 2022-02-11
 */
public interface JjcheRedisListerer {

    void onMessage(BaseMap message);

}
