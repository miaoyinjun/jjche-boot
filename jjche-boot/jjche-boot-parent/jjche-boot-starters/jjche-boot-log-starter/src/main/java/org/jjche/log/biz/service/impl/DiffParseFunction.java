package org.jjche.log.biz.service.impl;

import cn.hutool.log.StaticLog;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import org.jjche.common.context.LogRecordContext;
import org.jjche.log.biz.starter.diff.IDiffItemsToLogContentService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 对象比较
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-12
 */
public class DiffParseFunction {
    public static final String diffFunctionName = "_DIFF";
    public static final String OLD_OBJECT = "_oldObj";

    private static IDiffItemsToLogContentService diffItemsToLogContentService;

    public String functionName() {
        return diffFunctionName;
    }

    public String diff(Object source, Object target) {
        if (source == null && target == null) {
            return "";
        }
        if (source == null || target == null) {
            try {
                Class<?> clazz = source == null ? target.getClass() : source.getClass();
                source = source == null ? clazz.getDeclaredConstructor().newInstance() : source;
                target = target == null ? clazz.getDeclaredConstructor().newInstance() : target;
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        if (!Objects.equals(source.getClass(), target.getClass())) {
            StaticLog.error("diff的两个对象类型不同, source.class={}, target.class={}", source.getClass().toString(), target.getClass().toString());
            return "";
        }
        DiffNode diffNode = ObjectDifferBuilder.buildDefault().compare(target, source);
        return diffItemsToLogContentService.toLogContent(diffNode, source, target);
    }

    public String diff(Object newObj) {
        Object oldObj = LogRecordContext.getVariable(OLD_OBJECT);
        return diff(oldObj, newObj);
    }

    public String diff(Object newObj, int index) {
        Object oldObj = LogRecordContext.getVariable(OLD_OBJECT);
        if (oldObj instanceof List) {
            List<?> oldList = (List<?>) oldObj;
            if (oldList.size() <= index) {
                return diff(oldList.get(oldList.size() - 1), newObj);
            } else {
                return diff(oldList.get(index), newObj);
            }
        } else {
            return diff(oldObj, newObj);
        }
    }

    public void setDiffItemsToLogContentService(IDiffItemsToLogContentService diffItemsToLogContentService) {
        DiffParseFunction.diffItemsToLogContentService = diffItemsToLogContentService;
    }
}
