package com.boot.admin.tool.modules.tool.service;

import cn.hutool.core.lang.Assert;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.boot.admin.common.constant.CacheKey;
import com.boot.admin.tool.modules.tool.domain.AlipayConfigDO;
import com.boot.admin.tool.modules.tool.mapper.AliPayMapper;
import com.boot.admin.tool.modules.tool.vo.TradeVO;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>AliPayService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-31
 */
@Service
@RequiredArgsConstructor
public class AliPayService extends MyServiceImpl<AliPayMapper, AlipayConfigDO> {

    /**
     * 查询配置
     *
     * @return AlipayConfigDO
     */
    @Cached(name = CacheKey.ALI_PAY)
    public AlipayConfigDO find() {
        return this.getById(1L);
    }

    /**
     * 更新配置
     *
     * @param alipayConfig 支付宝配置
     * @return AlipayConfigDO
     */
    @CacheUpdate(name = CacheKey.ALI_PAY, value = "#result")
    @Transactional(rollbackFor = Exception.class)
    public AlipayConfigDO config(AlipayConfigDO alipayConfig) {
        alipayConfig.setId(1L);
        this.saveOrUpdate(alipayConfig);
        return alipayConfig;
    }

    /**
     * <p>
     * 处理来自PC的交易请求
     * </p>
     *
     * @param alipay 支付宝配置
     * @param trade  t
     * @return 交易详情
     * @throws java.lang.Exception if any.
     */
    public String toPayAsPc(AlipayConfigDO alipay, TradeVO trade) throws Exception {

        Assert.notNull(alipay.getId(), "请先添加相应配置，再操作");
        AlipayClient alipayClient = new DefaultAlipayClient(alipay.getGatewayUrl(), alipay.getAppId(), alipay.getPrivateKey(), alipay.getFormat(), alipay.getCharset(), alipay.getPublicKey(), alipay.getSignType());

        // 创建API对应的request(电脑网页版)
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        // 订单完成后返回的页面和异步通知地址
        request.setReturnUrl(alipay.getReturnUrl());
        request.setNotifyUrl(alipay.getNotifyUrl());
        // 填充订单参数
        request.setBizContent("{" +
                "    \"out_trade_no\":\"" + trade.getOutTradeNo() + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + trade.getTotalAmount() + "," +
                "    \"subject\":\"" + trade.getSubject() + "\"," +
                "    \"body\":\"" + trade.getBody() + "\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"" + alipay.getSysServiceProviderId() + "\"" +
                "    }" +
                "  }");//填充业务参数
        // 调用SDK生成表单, 通过GET方式，口可以获取url
        return alipayClient.pageExecute(request, "GET").getBody();

    }

    /**
     * 处理来自手机网页的交易请求
     *
     * @param alipay 支付宝配置
     * @param trade  交易详情
     * @return String
     * @throws java.lang.Exception if any.
     */
    public String toPayAsWeb(AlipayConfigDO alipay, TradeVO trade) throws Exception {
        Assert.notNull(alipay.getId(), "请先添加相应配置，再操作");
        AlipayClient alipayClient = new DefaultAlipayClient(alipay.getGatewayUrl(), alipay.getAppId(), alipay.getPrivateKey(), alipay.getFormat(), alipay.getCharset(), alipay.getPublicKey(), alipay.getSignType());

        double money = Double.parseDouble(trade.getTotalAmount());
        double maxMoney = 5000;
        Assert.isFalse(money <= 0 || money >= maxMoney, "测试金额过大");
        // 创建API对应的request(手机网页版)
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setReturnUrl(alipay.getReturnUrl());
        request.setNotifyUrl(alipay.getNotifyUrl());
        request.setBizContent("{" +
                "    \"out_trade_no\":\"" + trade.getOutTradeNo() + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + trade.getTotalAmount() + "," +
                "    \"subject\":\"" + trade.getSubject() + "\"," +
                "    \"body\":\"" + trade.getBody() + "\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"" + alipay.getSysServiceProviderId() + "\"" +
                "    }" +
                "  }");
        return alipayClient.pageExecute(request, "GET").getBody();
    }
}
