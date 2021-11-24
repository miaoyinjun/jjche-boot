package com.boot.admin.tool.modules.tool.rest;

import cn.hutool.core.lang.Assert;
import com.boot.admin.tool.modules.tool.domain.AlipayConfigDO;
import com.boot.admin.tool.modules.tool.service.AliPayService;
import com.boot.admin.tool.modules.tool.utils.AliPayStatusEnum;
import com.boot.admin.tool.modules.tool.utils.AlipayUtils;
import com.boot.admin.tool.modules.tool.vo.TradeVO;
import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.SysRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * <p>AliPayController class.</p>
 *
 * @author Zheng Jie
 * @since 2018-12-31
 * @version 1.0.8-SNAPSHOT
 */
@Slf4j
@SysRestController("aliPay")
@RequiredArgsConstructor
@Api(tags = "工具：支付宝管理")
public class AliPayController extends BaseController {

    private final AlipayUtils alipayUtils;
    private final AliPayService alipayService;

    /**
     * <p>queryConfig.</p>
     *
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping
    public ResultWrapper<AlipayConfigDO> queryConfig() {
        return ResultWrapper.ok(alipayService.find());
    }

    /**
     * <p>updateConfig.</p>
     *
     * @param alipayConfig a {@link com.boot.admin.tool.modules.tool.domain.AlipayConfigDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "配置", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "支付宝"
    )
    @ApiOperation("配置支付宝")
    @PutMapping
    public ResultWrapper updateConfig(@Validated @RequestBody AlipayConfigDO alipayConfig) {
        alipayService.config(alipayConfig);
        return ResultWrapper.ok();
    }

    /**
     * <p>toPayAsPc.</p>
     *
     * @param trade a {@link com.boot.admin.tool.modules.tool.vo.TradeVO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecordAnnotation(
            value = "PC网页支付", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "支付宝"
    )
    @ApiOperation("PC网页支付")
    @PostMapping(value = "/toPayAsPC")
    public ResultWrapper<String> toPayAsPc(@Validated @RequestBody TradeVO trade) throws Exception {
        AlipayConfigDO aliPay = alipayService.find();
        trade.setOutTradeNo(alipayUtils.getOrderCode());
        String payUrl = alipayService.toPayAsPc(aliPay, trade);
        return ResultWrapper.ok(payUrl);
    }

    /**
     * <p>toPayAsWeb.</p>
     *
     * @param trade a {@link com.boot.admin.tool.modules.tool.vo.TradeVO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecordAnnotation(
            value = "手机网页支付", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "支付宝"
    )
    @ApiOperation("手机网页支付")
    @PostMapping(value = "/toPayAsWeb")
    public ResultWrapper<String> toPayAsWeb(@Validated @RequestBody TradeVO trade) throws Exception {
        AlipayConfigDO alipay = alipayService.find();
        trade.setOutTradeNo(alipayUtils.getOrderCode());
        String payUrl = alipayService.toPayAsWeb(alipay, trade);
        return ResultWrapper.ok(payUrl);
    }

    /**
     * <p>returnPage.</p>
     *
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiIgnore
    @ApiOperation("支付之后跳转的链接")
    public ResultWrapper<String> returnPage(HttpServletRequest request, HttpServletResponse response) {
        AlipayConfigDO alipay = alipayService.find();
        response.setContentType("text/html;charset=" + alipay.getCharset());
        //内容验签，防止黑客篡改参数
        Assert.isTrue(alipayUtils.rsaCheck(request, alipay), "rsaCheck failed");
        //商户订单号
        String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        //支付宝交易号
        String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        // 根据业务需要返回数据，这里统一返回OK
        return ResultWrapper.ok();
    }

    /**
     * <p>notify.</p>
     *
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiIgnore
    @RequestMapping("/notify")
    @ApiOperation("支付异步通知(要公网访问)，接收异步通知，检查通知内容app_id、out_trade_no、total_amount是否与请求中的一致，根据trade_status进行后续业务处理")
    public ResultWrapper notify(HttpServletRequest request) {
        AlipayConfigDO alipay = alipayService.find();
        Map<String, String[]> parameterMap = request.getParameterMap();
        //内容验签，防止黑客篡改参数
        if (alipayUtils.rsaCheck(request, alipay)) {
            //交易状态
            String tradeStatus = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //付款金额
            String totalAmount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //验证
            if (tradeStatus.equals(AliPayStatusEnum.SUCCESS.getValue()) || tradeStatus.equals(AliPayStatusEnum.FINISHED.getValue())) {
                // 验证通过后应该根据业务需要处理订单
            }
            return ResultWrapper.ok();
        }
        return ResultWrapper.ok();
    }
}
