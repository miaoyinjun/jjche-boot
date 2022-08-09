package org.jjche.system.modules.app.api.conf;

import cn.hutool.log.StaticLog;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.constant.CacheKey;
import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.jjche.system.modules.app.api.dto.SecurityAppKeyQueryCriteriaDTO;
import org.jjche.system.modules.app.api.vo.SecurityAppKeyVO;
import org.jjche.system.modules.app.mapstruct.SecurityAppKeyMapStruct;
import org.jjche.system.modules.app.service.SecurityAppKeyService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 应用密钥初始化到缓存
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-05
 */
@Component
@RequiredArgsConstructor
public class AppKeyRunner implements ApplicationRunner {

    public final SecurityAppKeyService appKeyService;
    public final RedisService redisService;
    public final SecurityAppKeyMapStruct securityAppKeyMapStruct;

    /**
     * {@inheritDoc}
     * <p>
     * 项目启动时 应用密钥初始化到缓存
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        StaticLog.info("--------------------应用密钥初始化到缓存---------------------");
        //清空缓存
        redisService.delByKeyPrefix(CacheKey.SECURITY_APP_ID);
        //所有启用状态
        SecurityAppKeyQueryCriteriaDTO query = new SecurityAppKeyQueryCriteriaDTO();
        query.setEnabled(true);
        List<SecurityAppKeyVO> list = appKeyService.list(query);
        List<SecurityAppKeyBasicVO> basicVOList = securityAppKeyMapStruct.toBasicVO(list);
        for (SecurityAppKeyBasicVO basicVO : basicVOList) {
            String appId = basicVO.getAppId();
            redisService.objectSetObject(CacheKey.SECURITY_APP_ID + appId, basicVO);
        }
        StaticLog.info("--------------------应用密钥初始化到缓存完成---------------------");
    }
}
