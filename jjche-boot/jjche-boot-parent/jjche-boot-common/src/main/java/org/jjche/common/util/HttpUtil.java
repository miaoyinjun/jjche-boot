package org.jjche.common.util;

import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import org.lionsoul.ip2region.DbConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * http工具类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-25
 */
public class HttpUtil {
    /**
     * Constant <code>UNKNOWN="unknown"</code>
     */
    public static final String UNKNOWN = "unknown";
    private static final char SEPARATOR = '_';
    private static boolean ipLocal = false;
    private static File file = null;
    private static DbConfig config;

    /**
     * <p>printJson.</p>
     *
     * @param servletResponse a {@link javax.servlet.http.HttpServletResponse} object.
     * @param result          a {@link java.lang.Object} object.
     */
    @SneakyThrows
    public static void printJson(HttpServletResponse servletResponse, Object result) {
        PrintWriter writer = null;
        OutputStreamWriter osw = null;
        try {
            servletResponse.setContentType("application/json");
            osw = new OutputStreamWriter(servletResponse.getOutputStream(),
                    StandardCharsets.UTF_8);
            writer = new PrintWriter(osw, true);
            String jsonStr = JSONUtil.toJsonStr(result);
            writer.write(jsonStr);
            writer.flush();
            writer.close();
            osw.close();
        } catch (UnsupportedEncodingException ue) {
        } catch (IOException e) {

        } finally {
            if (null != writer) {
                writer.close();
            }
            if (null != osw) {
                try {
                    osw.close();
                } catch (IOException e) {

                }
            }
        }
        return;
    }

    /**
     * <p>
     * 获取浏览器信息
     * </p>
     *
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     * @return a {@link java.lang.String} object.
     * @author miaoyj
     * @since 2020-09-17
     */
    public static String getBrowser(HttpServletRequest request) {
        UserAgent userAgent = getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    /**
     * <p>
     * 获取用户代理
     * </p>
     *
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     * @return userAgent
     * @author miaoyj
     * @since 2020-11-05
     */
    public static UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgentUtil.parse(request.getHeader("User-Agent"));
    }

}
