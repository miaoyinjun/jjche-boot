package com.boot.admin.security.permission.field;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.boot.admin.common.util.FileUtil;
import com.boot.admin.core.permission.DataPermissionFieldFilterable;
import com.boot.admin.core.permission.DataPermissionFieldMetaSetter;
import com.boot.admin.core.permission.DataPermissionFieldResultVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * <p>
 * 支持过滤的结构，用于在AOP方法中对要显示的数据进行控制
 * </p>
 *
 * @author miaoyj
 * @since 2020-11-11
 * @version 1.0.10-SNAPSHOT
 */
public class DataPermissionFieldResult<T> implements DataPermissionFieldFilterable<T>, DataPermissionFieldMetaSetter {

    @Getter
    @Setter
    @ApiModelProperty("数据")
    private List<T> records;
    @ApiModelProperty("字段")
    private List<DataPermissionFieldResultVO> meta;

    /** {@inheritDoc} */
    @Override
    public void doFilter(Function<T, T> filterFunc) {
        for (T row : records) {
            filterFunc.apply(row);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setMeta(List<DataPermissionFieldResultVO> dataResources) {
        this.meta = dataResources;
    }

    /** {@inheritDoc} */
    @Override
    public List<DataPermissionFieldResultVO> getMeta() {
        return this.meta;
    }

    /**
     * <p>
     * 过滤集合
     * </p>
     *
     * @param rows 集合
     * @return 结果
     * @author miaoyj
     * @since 2020-12-09
     * @param <T> a T object.
     */
    public static <T> DataPermissionFieldResult<T> build(List<T> rows) {
        DataPermissionFieldResult<T> result = new DataPermissionFieldResult<>();
        result.setRecords(rows);
        return result;
    }

    /**
     * <p>
     * 导出过滤后的excel
     * </p>
     *
     * @param dataPermissionFieldResult 待导出数据
     * @author miaoyj
     * @since 2020-12-10
     */
    public static void downloadExcel(DataPermissionFieldResult dataPermissionFieldResult){
        List<Map<String, Object>> list = new ArrayList<>();
        List data = dataPermissionFieldResult.getRecords();
        List<DataPermissionFieldResultVO> meta = dataPermissionFieldResult.getMeta();
        JSONArray jsonArray = JSONUtil.parseArray(data);
        for (Object o : jsonArray) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (DataPermissionFieldResultVO fieldResultVO : meta) {
                Boolean isAccessible = fieldResultVO.getIsAccessible();
                if (isAccessible) {
                    String name = fieldResultVO.getName();
                    String code = fieldResultVO.getCode();
                    String value = new JSONObject(o).get(code, String.class);
                    map.put(name, value);
                }
            }
            list.add(map);
        }
        try {
            HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            FileUtil.downloadExcel(list, httpServletResponse);
        } catch (IOException e) {
            throw new IllegalArgumentException("文件下载失败");
        }
    }
}
