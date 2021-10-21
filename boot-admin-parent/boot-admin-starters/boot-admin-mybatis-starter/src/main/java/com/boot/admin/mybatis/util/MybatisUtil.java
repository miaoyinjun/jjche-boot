package com.boot.admin.mybatis.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.*;
import cn.hutool.log.StaticLog;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.mybatis.param.BetweenParam;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * 工具类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-10-10
 */
public class MybatisUtil {

    /**
     * <p>
     * 组装queryWrapper
     * </p>
     *
     * @param query 查询参数
     * @return queryWrapper
     * @author miaoyj
     * @since 2020-10-10
     */
    public static QueryWrapper assemblyQueryWrapper(Object query) {
        QueryWrapper queryWrapper = new QueryWrapper();
        List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
        for (Field field : fields) {
            boolean accessible = field.isAccessible();
            // 设置对象的访问权限，保证对private的属性的访
            field.setAccessible(true);
            QueryCriteria q = field.getAnnotation(QueryCriteria.class);
            if (q != null) {
                String propName = q.propName();
                if (StrUtil.isBlank(propName)) {
                    propName = StrUtil.toUnderlineCase(field.getName());
                }
                Object val = null;
                try {
                    val = field.get(query);
                    if (Enum.class.isInstance(val)) {
                        Enum sEnum = (Enum) val;
                        Class<? extends Enum<?>> clazz = (Class<? extends Enum<?>>) sEnum.getClass();
                        Map<String, Object> enumMap = EnumUtil.getNameFieldMap(clazz, "value");
                        val = enumMap.get(sEnum.name()).toString();
                    }
                } catch (Exception e) {
                    StaticLog.error(e.getMessage(), e);
                }
                if (ObjectUtil.isNull(val) || "".equals(val)) {
                    continue;
                }
                switch (q.type()) {
                    case EQUAL:
                        queryWrapper.eq(propName, val);
                        break;
                    case GREATER_THAN:
                        queryWrapper.ge(propName, val);
                        break;
                    case LESS_THAN:
                        queryWrapper.le(propName, val);
                        break;
                    case INNER_LIKE:
                        queryWrapper.like(propName, val);
                        break;
                    case BETWEEN:
                        if (ObjectUtil.isNotNull(val)) {
                            List<String> betweenParamList = (List) val;
                            BetweenParam betweenParam = getBetweenParam(betweenParamList, true);
                            queryWrapper.between(propName, betweenParam.getStart(), betweenParam.getEnd());
                        }
                        break;
                    case IN:
                        if (ObjectUtil.isNotNull(val)) {
                            Set<String> inParamList = (Set<String>) val;
                            if (CollUtil.isNotEmpty(inParamList)) {
                                queryWrapper.in(propName, inParamList);
                            }
                        }
                        break;
                    case IS_NULL:
                        queryWrapper.isNull(propName);
                        break;
                    case NOT_NULL:
                        queryWrapper.isNotNull(propName);
                        break;
                    default:
                        break;
                }
            }
            field.setAccessible(accessible);
        }
        return queryWrapper;
    }

    /**
     * <p>
     * 获取class 所有字段
     * </p>
     *
     * @param clazz
     * @param fields
     * @return
     * @author miaoyj
     * @since 2020-10-10
     */
    private static List<Field> getAllFields(Class clazz, List<Field> fields) {
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }

    /**
     * <p>
     * List参数转换区间对象
     * </p>
     *
     * @param betweenParamList a {@link java.util.List} object.
     * @param isDateTime       a boolean.
     * @return a {@link com.boot.admin.mybatis.param.BetweenParam} object.
     * @author miaoyj
     * @since 2020-10-15
     */
    public static BetweenParam getBetweenParam(List<String> betweenParamList, boolean isDateTime) {
        boolean isBetween = CollUtil.isNotEmpty(betweenParamList) && betweenParamList.size() == 2;
        Assert.isTrue(isBetween, "区间参数格式不正确");
        BetweenParam betweenParam = new BetweenParam(betweenParamList.get(0), betweenParamList.get(1));
        if (isDateTime) {
            String dateTimeRegex = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
            boolean isMatchStart = ReUtil.isMatch(dateTimeRegex, betweenParam.getStart());
            boolean isMatchEnd = ReUtil.isMatch(dateTimeRegex, betweenParam.getEnd());
            Assert.isTrue(isMatchStart && isMatchEnd, "区间参数日期时间格式不正确");
        }
        return betweenParam;
    }
}
