package org.jjche.common.util;


import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import org.jjche.common.constant.EnumConstant;
import org.jjche.common.dto.LogUpdateDetailDTO;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 类比较工具
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-20
 */
public class ClassCompareUtil {

    private final static String OBJECT_STR = "java.lang.object";

    /**
     * <p>
     * 获取注解ApiModelProperty内容
     * </p>
     *
     * @param objectClass 含有ApiModelProperty注解的对象类
     * @return 字段注释值
     */
    public static Map<String, String> getApiModelPropertyValue(Class objectClass) {
        List<Field> fieldList = getAllFields(objectClass);
        Map<String, String> fieldMap = new LinkedHashMap<>();
        for (Field field : fieldList) {
            field.setAccessible(true);
            String annotationName = AnnotationUtil.getAnnotationValue(field, ApiModelProperty.class, "value");
            if (StrUtil.isNotBlank(annotationName)) {
                fieldMap.put(field.getName(), annotationName);
            }
        }
        return fieldMap;
    }

    /**
     * <p>
     * 比较2个对象相差
     * </p>
     *
     * @param oldObject 老对象
     * @param newObject 新对象
     * @return 2个对象相差信息
     */
    public static List<LogUpdateDetailDTO> compareFieldsObject(Object oldObject, Object newObject) {
        List<LogUpdateDetailDTO> compareFieldList = null;
        if (ObjectUtil.isNotNull(oldObject) && ObjectUtil.isNotNull(newObject)) {
            compareFieldList = compareFields(oldObject, newObject);
            if (CollUtil.isNotEmpty(compareFieldList)) {
                Map<String, String> fieldMap = getApiModelPropertyValue(newObject.getClass());
                //转换为注释里的中文
                for (LogUpdateDetailDTO detailDTO : compareFieldList) {
                    String fieldKey = detailDTO.getName();
                    String annotationName = fieldMap.get(fieldKey);
                    detailDTO.setName(annotationName);
                }
            }
        }
        //删除字段名为空的内容
        Predicate condition = (str) -> str != null;
        compareFieldList = compareFieldList.stream().filter((p) -> (condition.test(p.getName()))).collect(Collectors.toList());
        return compareFieldList;
    }

    /**
     * 比较两个实体属性值，返回一个map以有差异的属性名为key，value为一个Map分别存oldObject,newObject此属性名的值
     *
     * @param oldObject 进行属性比较的对象1
     * @param newObject 进行属性比较的对象2
     * @return 属性差异比较结果map
     */
    @SuppressWarnings("rawtypes")
    private static List<LogUpdateDetailDTO> compareFields(Object oldObject, Object newObject) {
        List<LogUpdateDetailDTO> resultList = new ArrayList<>();
        Map<String, Object> oldObjectMap = Convert.convert(Map.class, oldObject);
        Map<String, Object> newObjectMap = Convert.convert(Map.class, newObject);
        for (Map.Entry<String, Object> entry : oldObjectMap.entrySet()) {
            String name = entry.getKey();
            //旧值
            Object oldValueObj = entry.getValue();
            String oldValue = getEnumDesc(oldValueObj);
            //新值
            Object newValueObj = MapUtil.get(newObjectMap, name, Object.class);
            String newValue = getEnumDesc(newValueObj);

            if (!StrUtil.equals(oldValue, newValue)) {
                LogUpdateDetailDTO logUpdateDetailDTO = new LogUpdateDetailDTO();
                logUpdateDetailDTO.setName(name);
                logUpdateDetailDTO.setOldVal(oldValue);
                logUpdateDetailDTO.setNewVal(newValue);
                resultList.add(logUpdateDetailDTO);
            }
        }
        return resultList;
    }

    /**
     * <p>
     * 获取枚举描述
     * </p>
     *
     * @param enumObject /
     * @return /
     */
    private static String getEnumDesc(Object enumObject) {
        String value = "";
        if (enumObject != null) {
            //枚举
            if (Enum.class.isInstance(enumObject)) {
                Field descField = ReflectionUtils.findField(enumObject.getClass(), EnumConstant.DESC);
                if (descField != null) {
                    ReflectionUtils.makeAccessible(descField);
                    enumObject = ReflectionUtils.getField(descField, enumObject);
                }
            }
            value = Convert.toStr(enumObject);
        }
        return value;
    }

    /**
     * <p>
     * 获取对象所有的属性
     * </p>
     *
     * @param clazz 对象类型
     * @return 属性
     */
    public static List<Field> getAllFields(Class clazz) {
        List<Field> fieldList = CollUtil.newArrayList();
        while (clazz != null && !OBJECT_STR.equals(clazz.getName().toLowerCase())) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            //得到父类,然后赋给自己
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }
}
