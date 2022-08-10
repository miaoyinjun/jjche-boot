package org.jjche.filter.enc.api;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.jjche.common.annotation.HttpResDataEncrypt;
import org.jjche.common.api.CommonAPI;
import org.jjche.common.enums.FilterEncEnum;
import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.OutRestController;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 加密响应信息
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-04
 */
@RestControllerAdvice(annotations = OutRestController.class)
@RequiredArgsConstructor
public class EncOutResponseBody implements ResponseBodyAdvice<R> {
    private final CommonAPI commonAPI;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //加密注解
        boolean methodHasHttpResDataEncrypt = methodParameter.hasMethodAnnotation(HttpResDataEncrypt.class);
        if (methodHasHttpResDataEncrypt) {
            return true;
        }
        return false;
    }

    @Override
    public R beforeBodyWrite(R restResult, MethodParameter methodParameter,
                             MediaType mediaType,
                             Class<? extends HttpMessageConverter<?>> aClass,
                             ServerHttpRequest serverHttpRequest,
                             ServerHttpResponse serverHttpResponse) {
        // 转换对象
        HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        HttpServletResponse response = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();

        int resCode = response.getStatus();
        //200时才做加密处理
        if (resCode == HttpStatus.OK.value()) {
            if (restResult.success()) {
                String appIdValue = request.getHeader(FilterEncEnum.APP_ID.getKey());
                SecurityAppKeyBasicVO appSecretVO = commonAPI.getKeyByAppId(appIdValue);
                String encKey = appSecretVO.getEncKey();
                SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, encKey.getBytes());
                String dataStr = JSONUtil.toJsonStr(restResult.getData());
                dataStr = aes.encryptHex(dataStr);
                restResult.setData(dataStr);
            }
        }
        return restResult;
    }
}
