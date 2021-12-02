package com.boot.admin.system.modules.security.rest;

import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.common.util.PageUtil;
import com.boot.admin.common.util.RsaUtils;
import com.boot.admin.core.annotation.controller.SysRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.security.dto.OnlineUserDto;
import com.boot.admin.security.property.SecurityProperties;
import com.boot.admin.security.service.OnlineUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
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
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
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
