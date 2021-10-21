package com.boot.admin.mybatis.extension;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill;
import com.boot.admin.mybatis.extension.injector.LogicDeleteBatchByIdsWithFill;
import com.boot.admin.mybatis.extension.injector.LogicDeleteBatchWithFill;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * Sql注入器
 * </p>
 *
 * @author miaoyj
 * @since 2020-08-26
 * @version 1.0.0-SNAPSHOT
 */
@Component
public class MySqlInjector extends DefaultSqlInjector {

    /** {@inheritDoc} */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        /**根据id逻辑删除并自动填充*/
        methodList.add(new LogicDeleteByIdWithFill());
        /**根据ids逻辑删除并自动填充*/
        methodList.add(new LogicDeleteBatchByIdsWithFill());
        /**根据自定义条件，逻辑批量删除并自动填充*/
        methodList.add(new LogicDeleteBatchWithFill());
        return methodList;
    }
}
