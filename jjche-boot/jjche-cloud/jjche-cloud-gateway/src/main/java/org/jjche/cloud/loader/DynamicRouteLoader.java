package org.jjche.cloud.loader;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jjche.cloud.config.GatewayRoutersConfiguration;
import org.jjche.cloud.config.RouterDataType;
import org.jjche.common.base.BaseMap;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 动态路由加载器
 *
 * @author : zyf
 * @date :2020-11-10
 */
@Slf4j
@Component
@DependsOn({"gatewayRoutersConfiguration"})
public class DynamicRouteLoader {
    private InMemoryRouteDefinitionRepository repository;
    private DynamicRouteService dynamicRouteService;
    private ConfigService configService;

    public DynamicRouteLoader(InMemoryRouteDefinitionRepository repository, DynamicRouteService dynamicRouteService) {
        this.repository = repository;
        this.dynamicRouteService = dynamicRouteService;
    }

    @PostConstruct
    public void init() {
        init(null);
    }

    public void init(BaseMap baseMap) {
        String dataType = GatewayRoutersConfiguration.DATA_TYPE;
        log.info("初始化路由，dataType：" + dataType);
        if (RouterDataType.nacos.toString().endsWith(dataType)) {
            loadRoutesByNacos();
        }
    }

    /**
     * 刷新路由
     *
     * @return
     */
    public Mono<Void> refresh(BaseMap baseMap) {
        String dataType = GatewayRoutersConfiguration.DATA_TYPE;
        if (!RouterDataType.yml.toString().endsWith(dataType)) {
            this.init(baseMap);
        }
        return Mono.empty();
    }

    /**
     * 从nacos中读取路由配置
     *
     * @return
     */
    private void loadRoutesByNacos() {
        List<RouteDefinition> routes = Lists.newArrayList();
        configService = createConfigService();
        if (configService == null) {
            log.warn("initConfigService fail");
        }
        try {
            String configInfo = configService.getConfig(GatewayRoutersConfiguration.DATA_ID, GatewayRoutersConfiguration.ROUTE_GROUP, GatewayRoutersConfiguration.DEFAULT_TIMEOUT);
            if (StrUtil.isNotBlank(configInfo)) {
                log.info("获取网关当前配置:\r\n{}", configInfo);
                routes = JSON.parseArray(configInfo, RouteDefinition.class);
            }
        } catch (NacosException e) {
            log.error("初始化网关路由时发生错误", e);
            e.printStackTrace();
        }
        for (RouteDefinition definition : routes) {
            log.info("update route : {}", definition.toString());
            dynamicRouteService.add(definition);
        }
        dynamicRouteByNacosListener(GatewayRoutersConfiguration.DATA_ID, GatewayRoutersConfiguration.ROUTE_GROUP);
    }

    /**
     * 监听Nacos下发的动态路由配置
     *
     * @param dataId
     * @param group
     */
    public void dynamicRouteByNacosListener(String dataId, String group) {
        try {
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("进行网关更新:\n\r{}", configInfo);
                    List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
                    log.info("update route : {}", definitionList.toString());
                    dynamicRouteService.updateList(definitionList);
                }

                @Override
                public Executor getExecutor() {
                    log.info("getExecutor\n\r");
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("从nacos接收动态路由配置出错!!!", e);
        }
    }

    /**
     * 创建ConfigService
     *
     * @return
     */
    private ConfigService createConfigService() {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", GatewayRoutersConfiguration.SERVER_ADDR);
            properties.setProperty("namespace", GatewayRoutersConfiguration.NAMESPACE);
            return configService = NacosFactory.createConfigService(properties);
        } catch (Exception e) {
            log.error("创建ConfigService异常", e);
            return null;
        }
    }
}
