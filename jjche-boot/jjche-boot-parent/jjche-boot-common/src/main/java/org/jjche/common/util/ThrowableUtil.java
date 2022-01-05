package org.jjche.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具 2019-01-06
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 */
public class ThrowableUtil {

    /**
     * 获取堆栈信息
     *
     * @param throwable a {@link java.lang.Throwable} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
