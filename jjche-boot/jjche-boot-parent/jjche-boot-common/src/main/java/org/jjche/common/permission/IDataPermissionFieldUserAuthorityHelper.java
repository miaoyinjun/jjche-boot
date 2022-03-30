package org.jjche.common.permission;

import org.jjche.common.vo.DataPermissionFieldResultVO;

import java.util.List;

/**
 * <p>
 * 用户权限工具类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-11
 */
public interface IDataPermissionFieldUserAuthorityHelper {
    /**
     * <p>
     * 执行过滤
     * </p>
     *
     * @param permission  权限标识
     * @param returnClass 对象类型
     * @param isFilter    是否过滤
     * @return 结果
     */
    List<DataPermissionFieldResultVO> getDataResource(String permission, Class returnClass, boolean isFilter);
}
