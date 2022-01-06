package org.jjche.system.modules.security.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.util.PageUtil;
import org.jjche.common.util.RsaUtils;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.core.wrapper.response.ResultWrapper;
import org.jjche.log.biz.starter.annotation.LogRecordAnnotation;
import org.jjche.mybatis.param.MyPage;
import org.jjche.mybatis.param.PageParam;
import org.jjche.security.dto.OnlineUserDto;
import org.jjche.security.property.SecurityProperties;
import org.jjche.security.service.OnlineUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <p>OnlineController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 */
@RequiredArgsConstructor
@Api(tags = "系统：在线用户管理")
@SysRestController("/auth/online")
public class OnlineController extends BaseController {

    private final OnlineUserService onlineUserService;
    private final SecurityProperties securityProperties;

    /**
     * <p>query.</p>
     *
     * @param filter   a {@link java.lang.String} object.
     * @param pageable /
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("查询在线用户")
    @GetMapping
    @PreAuthorize("@el.check('online:list')")
    public ResultWrapper query(String filter, PageParam pageable) {
        List<OnlineUserDto> onlineUserDtos = onlineUserService.getAll(filter);
        List<OnlineUserDto> list = PageUtil.startPage(onlineUserDtos, (int) pageable.getPageIndex(), (int) pageable.getPageSize());
        MyPage myPage = new MyPage();
        myPage.setRecords(list);
        myPage.setTotal(onlineUserDtos.size());
        return ResultWrapper.ok(myPage);
    }

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param filter   a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    @LogRecordAnnotation(
            value = "导出", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "在线用户"
    )
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('online:list')")
    public void download(HttpServletResponse response, String filter) throws IOException {
        onlineUserService.download(onlineUserService.getAll(filter), response);
    }

    /**
     * <p>delete.</p>
     *
     * @param keys a {@link java.util.Set} object.
     * @return a {@link ResultWrapper} object.
     * @throws java.lang.Exception if any.
     */
    @ApiOperation("踢出用户")
    @DeleteMapping
    @PreAuthorize("@el.check('online:del')")
    public ResultWrapper delete(@RequestBody Set<String> keys) throws Exception {
        for (String key : keys) {
            String privateKey = securityProperties.getRsa().getPrivateKey();
            // 解密Key
            key = RsaUtils.decryptByPrivateKey(privateKey, key);
            onlineUserService.kickOut(key);
        }
        return ResultWrapper.ok();
    }
}
