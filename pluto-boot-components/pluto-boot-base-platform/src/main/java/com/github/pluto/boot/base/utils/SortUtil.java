package com.github.pluto.boot.base.utils;

import com.github.pluto.boot.base.common.BaseSystemConstant;
import com.github.pluto.boot.base.common.QueryRequest;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/19 10:03
 * @description： 处理排序工具类
 */
@SuppressWarnings("unchecked")
public class SortUtil {
    /**
     * 处理排序 for mybatis-plus
     *
     * @param request           QueryRequest
     * @param wrapper           wrapper
     * @param defaultSort       默认排序的字段
     * @param defaultOrder      默认排序规则
     * @param camelToUnderscore 是否开启驼峰转下划线
     */
    public static void handleWrapperSort(QueryRequest request, QueryWrapper wrapper, String defaultSort, String defaultOrder, boolean camelToUnderscore) {
        String sortField = request.getSortField();
        if (camelToUnderscore) {
            sortField = ForestUtil.camelToUnderscore(sortField);
            defaultSort = ForestUtil.camelToUnderscore(defaultSort);
        }
        if (StringUtils.isNotBlank(request.getSortField())
                && StringUtils.isNotBlank(request.getSortOrder())
                && !StringUtils.equalsIgnoreCase(request.getSortField(), "undefined")
                && !StringUtils.equalsIgnoreCase(request.getSortOrder(), "undefined")) {
            if (StringUtils.equals(request.getSortOrder(), BaseSystemConstant.ORDER_DESC))
                wrapper.orderBy(sortField, false);
            else
                wrapper.orderBy(sortField, true);
        } else {
            if (StringUtils.isNotBlank(defaultSort)) {
                if (StringUtils.equals(defaultOrder, BaseSystemConstant.ORDER_DESC))
                    wrapper.orderBy(defaultSort, false);
                else
                    wrapper.orderBy(defaultSort, true);
            }
        }
    }

    /**
     * 处理排序 for mybatis-plus
     *
     * @param request QueryRequest
     * @param wrapper wrapper
     */
    public static void handleWrapperSort(QueryRequest request, QueryWrapper wrapper) {
        handleWrapperSort(request, wrapper, null, null, false);
    }

    /**
     * 处理排序 for mybatis-plus
     *
     * @param request           QueryRequest
     * @param wrapper           wrapper
     * @param camelToUnderscore 是否开启驼峰转下划线
     */
    public static void handleWrapperSort(QueryRequest request, QueryWrapper wrapper, boolean camelToUnderscore) {
        handleWrapperSort(request, wrapper, null, null, camelToUnderscore);
    }

    /**
     * 处理排序（分页情况下） for mybatis-plus
     *
     * @param request           QueryRequest
     * @param page              Page
     * @param defaultSort       默认排序的字段
     * @param defaultOrder      默认排序规则
     * @param camelToUnderscore 是否开启驼峰转下划线
     */
    public static void handlePageSort(QueryRequest request, QueryWrapper queryWrapper, Page page, String defaultSort, String defaultOrder, boolean camelToUnderscore) {
        page.setPageNumber(request.getPageNum());
        page.setPageSize(request.getPageSize());
        handleWrapperSort(request, queryWrapper, defaultSort, defaultOrder, camelToUnderscore);
    }

    public static void handlePageSort(QueryRequest request,QueryWrapper queryWrapper, Page page, boolean camelToUnderscore) {
        handlePageSort(request, queryWrapper,page, null, null, camelToUnderscore);
    }
}
