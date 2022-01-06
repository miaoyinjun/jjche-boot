package org.jjche.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.jjche.common.constant.IpConstant;
import org.jjche.common.constant.UrlConstant;
import org.jjche.common.dto.LogUpdateDetailDTO;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * <p>StringUtils class.</p>
 *
 * @author Zheng Jie
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * @version 1.0.8-SNAPSHOT
 */
public class StrUtil extends cn.hutool.core.util.StrUtil {

    private static final char SEPARATOR = '_';
    private static final String UNKNOWN = "unknown";
    private static final Supplier<Stream<String>> BROWSER = () -> Stream.of(
            "Chrome", "Firefox", "Microsoft Edge", "Safari", "Opera"
    );
    private static final Supplier<Stream<String>> OPERATING_SYSTEM = () -> Stream.of(
            "Android", "Linux", "Mac OS X", "Ubuntu", "Windows 10", "Windows 8", "Windows 7", "Windows XP", "Windows Vista"
    );
    private static boolean ipLocal = true;
    private static File file = null;
    private static DbConfig config;

    static {
        if (ipLocal) {
            /*
             * 此文件为独享 ，不必关闭
             */
            String name = "ip2region.db";
            String path = "ip2region/" + name;
            try {
                config = new DbConfig();
                file = FileUtil.inputStreamToFile(new ClassPathResource(path).getInputStream(), name, true);
            } catch (Exception e) {
                StaticLog.error(e.getMessage(), e);
            }
        }
    }

    private static String simplifyOperatingSystem(String operatingSystem) {
        return OPERATING_SYSTEM.get().parallel().filter(b -> cn.hutool.core.util.StrUtil.containsIgnoreCase(operatingSystem, b)).findAny().orElse(operatingSystem);
    }

    private static String simplifyBrowser(String browser) {
        return BROWSER.get().parallel().filter(b -> cn.hutool.core.util.StrUtil.containsIgnoreCase(browser, b)).findAny().orElse(browser);
    }

    /**
     * 驼峰命名法工具
     *
     * @param s a {@link java.lang.String} object.
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @param s a {@link java.lang.String} object.
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 获取ip地址
     *
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (cn.hutool.core.util.StrUtil.contains(ip, comma)) {
            ip = ip.split(",")[0];
        }
        if (cn.hutool.core.util.StrUtil.equals(localhost, ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                StaticLog.error(e.getMessage(), e);
            }
        }
        return ip == null ? UNKNOWN : ip;
    }

    /**
     * 根据ip获取详细地址
     *
     * @param ip a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getCityInfo(String ip) {
        if (ipLocal) {
            return getLocalCityInfo(ip);
        } else {
            return getHttpCityInfo(ip);
        }
    }

    /**
     * 根据ip获取详细地址
     *
     * @param ip a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getHttpCityInfo(String ip) {
        String api = String.format(UrlConstant.IP_JSON_URL, ip);
        JSONObject object = JSONUtil.parseObj(HttpUtil.get(api));
        return object.get("addr", String.class);
    }

    /**
     * 根据ip获取详细地址
     *
     * @param ip a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getLocalCityInfo(String ip) {
        try {
            DbSearcher dbSearcher = new DbSearcher(config, file.getPath());
            DataBlock dataBlock = dbSearcher
                    .binarySearch(ip);
            String region = dataBlock.getRegion();
            String address = region.replace("0|", "");
            char symbol = '|';
            if (address.charAt(address.length() - 1) == symbol) {
                address = address.substring(0, address.length() - 1);
            }
            return address.equals(IpConstant.REGION) ? "内网IP" : address;
        } catch (Exception e) {
            StaticLog.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * <p>getBrowser.</p>
     * <p>
     * userAgent 请求
     *
     * @param userAgent a {@link eu.bitwalker.useragentutils.UserAgent} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getBrowser(UserAgent userAgent) {
        Browser browser = userAgent.getBrowser();
        if (browser != null) {
            return simplifyBrowser(browser.getName());
        } else {
            return UNKNOWN;
        }
    }

    /**
     * 获取当前机器的IP
     *
     * @return /
     */
    public static String getLocalIp() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements(); ) {
                NetworkInterface anInterface = interfaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration<InetAddress> inetAddresses = anInterface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddresses.nextElement();
                    // 排除loopback类型地址
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                return "unknown";
            }
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * <p>
     * 获取系统版本
     * </p>
     *
     * @param userAgent 请求
     * @return 系统版本
     */
    public static String getOs(UserAgent userAgent) {
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        if (operatingSystem != null) {
            return simplifyOperatingSystem(operatingSystem.getName());
        } else {
            return UNKNOWN;
        }
    }

    /**
     * <p>
     * 单数转换复数
     * </p>
     *
     * @param word a {@link java.lang.String} object.
     * @return 复数
     */
    public static String pluralizeWord(String word) {
        return InflectorUtil.getInstance().pluralize(word);
    }

    /**
     * <p>
     * 比较对象修改日志
     * </p>
     *
     * @param eDO  修改前
     * @param eDTO 修改后
     * @return /
     */
    public static String updateDiffByDoDto(Serializable eDO, Serializable eDTO) {
        StringBuilder detailSb = cn.hutool.core.util.StrUtil.builder();
        //保存修改前后区别字段
        List<LogUpdateDetailDTO> logUpdateDetailDTOList = ClassCompareUtil.compareFieldsObject(eDO, eDTO);
        if (CollUtil.isNotEmpty(logUpdateDetailDTOList)) {
            for (LogUpdateDetailDTO updateDetailDTO : logUpdateDetailDTOList) {
                detailSb.append("[");
                detailSb.append("(");
                detailSb.append(updateDetailDTO.getName());
                detailSb.append(")");
                detailSb.append("，");
                detailSb.append("旧值：");
                detailSb.append("'");
                detailSb.append(updateDetailDTO.getOldVal());
                detailSb.append("'");
                detailSb.append("，");
                detailSb.append("新值：");
                detailSb.append("'");
                detailSb.append(updateDetailDTO.getNewVal());
                detailSb.append("'");
                detailSb.append("]，");
            }
        }
        return StrUtil.removeSuffix(detailSb.toString(), "，");
    }
}
