package com.boot.admin.security.permission.field;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.boot.admin.core.permission.DataPermissionFieldFilterable;
import com.boot.admin.core.permission.DataPermissionFieldMetaSetter;
import com.boot.admin.core.permission.DataPermissionFieldResultVO;
import com.boot.admin.core.permission.IDataPermissionFieldUserAuthorityHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据权限拦截器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-11
 */
@Component
@Aspect
public class DataPermissionFieldResourceAuthorityInterceptor {

    @Autowired(required = false)
    private IDataPermissionFieldUserAuthorityHelper userAuthorityHelper;

    /**
     * 切入点设置，拦截所有具有{@link com.boot.admin.security.permission.field.DataPermissionFieldMethod}注解的方法
     */
    @Pointcut("@annotation(com.boot.admin.security.permission.field.DataPermissionFieldMethod)")
    public void queryMethodPointcut() {
    }

    /**
     * 环绕通知
     *
     * @param joinPoint ProceedingJoinPoint
     * @return 方法返回的对象
     * @throws java.lang.Throwable if any.
     */
    @Around(value = "queryMethodPointcut()")
    public Object doInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        Object object = joinPoint.proceed();
        Class myPageClass = ClassUtil.loadClass("com.boot.admin.mybatis.param.MyPage");
        //只处理返回结果为DataPermissionFieldResult类型
        if (ObjectUtil.isNotNull(object)) {
//            boolean isMyPage = ClassUtil.isAssignable(myPageClass, object.getClass());
//            boolean isCollection = ClassUtil.isAssignable(CollUtil.class, object.getClass());
//            if (isMyPage || isCollection) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            DataPermissionFieldMethod aopPermission = method.getAnnotation(DataPermissionFieldMethod.class);
            String permission = aopPermission.value();
            String methodName = this.getMethodName(joinPoint);
            if (object != null) {
                if (object instanceof DataPermissionFieldFilterable) {
                    this.doFilter((DataPermissionFieldFilterable) object, permission);
                }
                if (object instanceof DataPermissionFieldMetaSetter) {
                    this.metaHandler((DataPermissionFieldMetaSetter) object, methodName, permission);
                }
            }
//            }
        }
        return object;
    }

    /**
     * 执行过滤操作
     *
     * @param filterable 方法返回的对象
     * @param permission 权限标识
     */
    private void doFilter(DataPermissionFieldFilterable<?> filterable, String permission) {
        List<DataPermissionFieldResultVO> resources = this.getDataResources(permission);
        // 如果
        if (CollectionUtils.isEmpty(resources)) {
            return;
        }
        filterable.doFilter(o -> {
            Map<String, DataPermissionFieldResultVO> dataColumnMap = new HashMap<>(resources.size());
            for (DataPermissionFieldResultVO column : resources) {
                dataColumnMap.put(column.getCode(), column);
            }
            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(o.getClass());
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String name = propertyDescriptor.getName();
                DataPermissionFieldResultVO dataColumn = dataColumnMap.get(name);
                if (dataColumn != null && !dataColumn.getIsAccessible()) {
                    try {
                        propertyDescriptor.getWriteMethod().invoke(o, new Object[]{null});
                    } catch (Exception ex) {
                        // skip
                    }
                }
            }
            return o;
        });
    }

    /**
     * 设置数据结构
     *
     * @param metaSetter 方法返回的对象
     * @param methodName 拦截的方法名称
     */
    private void metaHandler(DataPermissionFieldMetaSetter metaSetter, String methodName, String permission) {
        List<DataPermissionFieldResultVO> resources = this.getDataResources(permission);
        if (resources != null) {
            metaSetter.setMeta(resources);
        } else {
            // 如果没有设置数据资源，默认用户拥有访问全部资源的权限
            List<DataPermissionFieldResultVO> allResources = findAuthorityDataResource(methodName);
            metaSetter.setMeta(allResources);
        }
    }

    /**
     * 根据方法名和用户ID获取用户的数据权限
     *
     * @param permission 权限标识
     * @return 用户的数据权限
     */
    private List<DataPermissionFieldResultVO> getDataResources(String permission) {
        return this.userAuthorityHelper.getDataResource(permission);
    }

    /**
     * 获取此方法对应的所有数据资源项
     *
     * @param methodName 拦截的方法名称
     * @return 用户的数据权限
     */
    private List<DataPermissionFieldResultVO> findAuthorityDataResource(String methodName) {
        return null;
    }

    /**
     * <p>
     * 获取方法名
     * </p>
     *
     * @param joinPoint aop
     * @return 方法名
     * @author miaoyj
     * @since 2020-12-09
     */
    private String getMethodName(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;

            StringBuilder sb = new StringBuilder();

            sb.append(methodSignature.getDeclaringTypeName());
            sb.append(".");
            sb.append(methodSignature.getName());
            sb.append("(");
            Class<?>[] parametersTypes = methodSignature.getParameterTypes();
            for (int i = 0; i < parametersTypes.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                Class<?> parametersType = parametersTypes[i];
                sb.append(parametersType.getSimpleName());
            }
            sb.append(")");
            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(signature.getDeclaringTypeName());
            sb.append(".");
            sb.append(signature.getName());
            return sb.toString();
        }
    }
}
