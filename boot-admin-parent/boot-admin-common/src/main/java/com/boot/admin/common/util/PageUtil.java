package com.boot.admin.common.util;


import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-10
 */
public class PageUtil extends cn.hutool.core.util.PageUtil {

    /**
     * 开始分页
     *
     * @param list     a {@link java.util.List} object.
     * @param pageNum  页码
     * @param pageSize 每页多少条数据
     * @return a {@link java.util.List} object.
     */
    public static List startPage(List list, Integer pageNum, Integer pageSize) {
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        //记录总数
        Integer count = list.size();
        //页数
        Integer pageCount = 0;
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }
        //开始索引
        int fromIndex = 0;
        //结束索引
        int toIndex = 0;
        if (pageNum > pageCount) {
            pageNum = pageCount;
        }
        if (!pageNum.equals(pageCount)) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }
        List<Object> pageList = new ArrayList<>(list.subList(fromIndex, toIndex));
        return pageList;
    }

}
