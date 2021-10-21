package com.boot.admin.core.permission;

import java.util.List;

/**
 * <p>
 * 用户权限工具类
 * </p>
 *
 * @author miaoyj
 * @since 2020-11-11
 * @version 1.0.10-SNAPSHOT
 */
public interface IDataPermissionFieldUserAuthorityHelper {
     /**
      * <p>
      * 执行过滤
      * </p>
      *
      * @param permission 权限标识
      * @return 结果
      * @author miaoyj
      * @since 2020-12-09
      */
     List<DataPermissionFieldResultVO> getDataResource(String permission);
}
