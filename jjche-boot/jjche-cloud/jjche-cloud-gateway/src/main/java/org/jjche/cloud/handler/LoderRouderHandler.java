package org.jjche.cloud.handler;

import lombok.extern.slf4j.Slf4j;
import org.jjche.cloud.loader.DynamicRouteLoader;
import org.jjche.common.base.BaseMap;
import org.jjche.common.listener.JjcheRedisListerer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 路由刷新监听
 */
@Slf4j
@Component
public class LoderRouderHandler implements JjcheRedisListerer {

    @Resource
    private DynamicRouteLoader dynamicRouteLoader;


    @Override
    public void onMessage(BaseMap message) {
        dynamicRouteLoader.refresh(message);
    }

}