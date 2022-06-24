package org.jjche.common.util;


import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import org.jjche.common.dto.LogUpdateDetailDTO;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

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
        try {
            Map<String, String> newObjectMap = Convert.convert(Map.class, newObject);
            Class oldObjectClazz = oldObject.getClass();
            //获取object的所有属性
            PropertyDescriptor[] oldObjectPds = Introspector.getBeanInfo(oldObjectClazz, Object.class).getPropertyDescriptors();
            for (PropertyDescriptor oldObjectPd : oldObjectPds) {
                String name = oldObjectPd.getName();
                if (newObjectMap.containsKey(name)) {
                    Method readMethod = oldObjectPd.getReadMethod();
                    String oldValue = Convert.toStr(readMethod.invoke(oldObject));
                    String newValue = MapUtil.getStr(newObjectMap, name);
                    if (!StrUtil.equalsIgnoreCase(oldValue, newValue)) {
                        LogUpdateDetailDTO logUpdateDetailDTO = new LogUpdateDetailDTO();
                        logUpdateDetailDTO.setName(name);
                        logUpdateDetailDTO.setOldVal(oldValue);
                        logUpdateDetailDTO.setNewVal(newValue);
                        resultList.add(logUpdateDetailDTO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
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
