package com.boot.admin.aop;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.*;
import cn.hutool.log.StaticLog;
import com.boot.admin.common.annotation.PermissionData;
import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.common.context.ELPermissionContext;
import com.boot.admin.common.dto.BaseQueryCriteriaDTO;
import com.boot.admin.common.dto.PermissionDataRuleDTO;
import com.boot.admin.common.pojo.DataScope;
import com.boot.admin.core.permission.DataPermissionFieldFilterable;
import com.boot.admin.core.permission.DataPermissionFieldMetaSetter;
import com.boot.admin.core.permission.DataPermissionFieldResultVO;
import com.boot.admin.core.permission.IDataPermissionFieldUserAuthorityHelper;
import com.boot.admin.core.util.SecurityUtils;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.security.permission.field.DataPermissionFieldResult;
import com.boot.admin.system.modules.system.service.DataPermissionRuleService;
import com.boot.admin.system.modules.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 数据权限切面处理类
 * 当被请求的方法有注解PermissionData时,会在往当前request中写入数据权限信息
 *
 * @Date 2019年4月10日
 * @Version: 1.0
 */
@Aspect
@Component
@Slf4j
public class PermissionDataAspect {

    @Autowired
    private DataPermissionRuleService sysDataPermissionRuleService;
    @Autowired
    private UserService userService;
    @Autowired(required = false)
    private IDataPermissionFieldUserAuthorityHelper userAuthorityHelper;

    @Pointcut("@annotation(com.boot.admin.common.annotation.PermissionData)")
    public void permissionDataCut() {

    }

    @Around("permissionDataCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object object = null;
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        PermissionData pd = method.getAnnotation(PermissionData.class);

        String permissionCode = ELPermissionContext.get();
        DataScope dataScope = null;
        //未登录情况下
        try {
            dataScope = SecurityUtils.getCurrentUserDataScope();
        } catch (Exception e) {
        }
        BaseQueryCriteriaDTO paramQueryCriteriaDTO = null;
        boolean isNotAdmin = dataScope != null && !dataScope.isAll() && StrUtil.isNotBlank(permissionCode);
        //非管理员
        if (isNotAdmin) {
            permissionCode = StrUtil.sub(permissionCode, 0, StrUtil.indexOf(permissionCode, ':') + 1);
            permissionCode = StrUtil.appendIfMissing(permissionCode, "list");
            //行级
            List<PermissionDataRuleDTO> permissionDataRuleList = CollUtil.newArrayList();
            //dataScope定义
            String deptName = pd.deptIdInFieldName(),
                    creatorName = pd.userIdEQFieldName(),
                    userName = dataScope.getUserName();
            Set<Long> deptIds = dataScope.getDeptIds();
            boolean isSelf = dataScope.isSelf();
            //部门id
            if (StrUtil.isNotBlank(deptName) && CollectionUtil.isNotEmpty(deptIds)) {
                PermissionDataRuleDTO permissionDataRuleDTO = PermissionDataRuleDTO.builder()
                        .column(deptName)
                        .condition(QueryCriteria.Type.IN)
                        .value(deptIds)
                        .build();
                permissionDataRuleList.add(permissionDataRuleDTO);
            }//作者
            else if (isSelf && StrUtil.isNotEmpty(creatorName)) {
                PermissionDataRuleDTO permissionDataRuleDTO = PermissionDataRuleDTO.builder()
                        .column(creatorName)
                        .condition(QueryCriteria.Type.EQUAL)
                        .value(userName)
                        .build();
                permissionDataRuleList.add(permissionDataRuleDTO);
            }

            //获取用户数据规则配置
            List<PermissionDataRuleDTO> permissionDataRuleDTOList =
                    sysDataPermissionRuleService.listByUserId(SecurityUtils.getCurrentUserId());
            if (CollUtil.isNotEmpty(permissionDataRuleDTOList)) {
                String finalPermissionCode = permissionCode;
                Predicate condition = (str) -> StrUtil.equals(String.valueOf(str), finalPermissionCode);
                permissionDataRuleDTOList = permissionDataRuleDTOList.stream().filter((p) -> (condition.test(p.getMenuPermission()))).collect(Collectors.toList());
                // TODO: 2021/11/17  系统上下文变量
//                    if (CollUtil.isNotEmpty(permissionDataRuleDTOList)) {
//                        for (PermissionDataRuleDTO ruleDTO : permissionDataRuleDTOList) {
//                            Object value = ruleDTO.getValue();
//                            if (value instanceof String) {
//                                value = StrUtil.replace(String.valueOf(value), "", "");
//                                ruleDTO.setValue(value);
//                            }
//                        }
//                    }
                permissionDataRuleList.addAll(permissionDataRuleDTOList);
            }

            Object[] args = point.getArgs();
            if (ArrayUtil.isNotEmpty(args)) {
                for (Object arg : args) {
                    if (arg instanceof BaseQueryCriteriaDTO) {
                        paramQueryCriteriaDTO = (BaseQueryCriteriaDTO) arg;
                        paramQueryCriteriaDTO.setPermissionDataRuleList(permissionDataRuleList);
                        break;
                    }
                }
            }
        }

        //获取泛型
        // 1层，如DataPermissionFieldResult<StudentVO>
        // 2层，如ResultWrapper<MyPage<StudentVO>>
        Type returnType = this.getTypeArgumentVO(method.getGenericReturnType());
        Class returnClass = TypeUtil.getClass(returnType);

        //场景：修改，单个对象列级，入参
        //如果字段没有修改权限，将字段置null，mybatis不会处理null值字段
        if (pd.fieldUpdate() && paramQueryCriteriaDTO != null) {
            returnClass = paramQueryCriteriaDTO.getClass();
            this.doFilter(DataPermissionFieldResult.build(paramQueryCriteriaDTO), returnClass, permissionCode, isNotAdmin, true);
        }
        object = point.proceed();
        //列级
        //必须有返回值
        if (pd.fieldReturn() && ObjectUtil.isNotEmpty(object)) {
            if (object != null) {
                Object newObject = null;
                boolean isResultWrapper = ClassUtil.isAssignable(ResultWrapper.class, object.getClass());
                boolean isDataPermissionFieldResult = ClassUtil.isAssignable(DataPermissionFieldResult.class, object.getClass());
                //控制器
                if (isResultWrapper) {
                    ResultWrapper resultWrapper = (ResultWrapper) object;
                    newObject = resultWrapper.getData();
                } else if (isDataPermissionFieldResult) {
                    newObject = object;
                }
                if (newObject != null) {
                    List<DataPermissionFieldResultVO> resources = null;
                    //行
                    if (newObject instanceof DataPermissionFieldFilterable) {
                        resources = this.doFilter((DataPermissionFieldFilterable) newObject, returnClass, permissionCode, isNotAdmin, false);
                    }
                    //列
                    if (newObject instanceof DataPermissionFieldMetaSetter) {
                        ((DataPermissionFieldMetaSetter) newObject).setMeta(resources);
                    }
                }
            }
        }
        if (object == null) {
            object = point.proceed();
        }
        return object;
    }

    /**
     * 执行过滤操作
     *
     * @param filterable  方法返回的对象
     * @param returnClass 对象类
     * @param isFilter    是否过滤
     * @param permission  权限标识
     * @return 查询内容
     */
    private List<DataPermissionFieldResultVO> doFilter(DataPermissionFieldFilterable<?> filterable,
                                                       Class returnClass,
                                                       String permission, boolean isFilter,
                                                       boolean editable) {
        List<DataPermissionFieldResultVO> resources = this.getDataResources(permission, returnClass, isFilter);
        if (CollUtil.isEmpty(resources)) {
            return null;
        }

        Map<String, DataPermissionFieldResultVO> dataColumnMap = new HashMap<>(resources.size());
        for (DataPermissionFieldResultVO column : resources) {
            dataColumnMap.put(column.getCode(), column);
        }
        filterable.doFilter(o -> {
            PropertyDescriptor[] propertyDescriptors = BeanUtil.getPropertyDescriptors(o.getClass());
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String name = propertyDescriptor.getName();
                DataPermissionFieldResultVO dataColumn = dataColumnMap.get(name);
                if (dataColumn != null && (!dataColumn.getIsAccessible() || (editable && !dataColumn.getIsEditable()))) {
                    try {
                        propertyDescriptor.getWriteMethod().invoke(o, new Object[]{null});
                    } catch (Exception ex) {
                        // skip
                        StaticLog.error("字段置null失败");
                    }
                }
            }
            return o;
        });
        return resources;
    }

    /**
     * 根据方法名和用户ID获取用户的数据权限
     *
     * @param returnClass 对象类型
     * @param permission  权限标识
     * @param isFilter    是否过滤
     * @return 用户的数据权限
     */
    private List<DataPermissionFieldResultVO> getDataResources(String permission, Class returnClass, boolean isFilter) {
        return this.userAuthorityHelper.getDataResource(permission, returnClass, isFilter);
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

    /**
     * <p>
     * 获取VO结尾的class泛型
     * </p>
     *
     * @param type 返回类型
     * @return /
     */
    private Type getTypeArgumentVO(Type type) {
        Type resultType = TypeUtil.getTypeArgument(type);
        if (resultType != null) {
            String className = resultType.getTypeName();
            if (!StrUtil.endWith(className, "VO")) {
                return getTypeArgumentVO(resultType);
            }
        }
        return resultType;
    }

}