package com.booking.utils;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class STablePageRequest {

    /**
     * F12
     * 离线数据 mock：参考https://preview.pro.loacg.com/list/table-list
     * Console
     * parameter {pageNo: 2, pageSize: 10, sortField: "callNo", sortOrder: "ascend"}
     * <p>
     * 前后端交互:参考https://vue.ant.design/components/table-cn/
     * Network ->Headers
     * Query String Parameters
     * pageNo: 2, pageSize: 10, sortField: "callNo", sortOrder: "ascend"
     */

    //1.声明前端STable插件 的 分页条件
    //分页条件
    private int pageNo = 1;
    private int pageSize = 10;
    //排序条件
    private String sortField = "createTime";
    private String sortOrder = "descend";

    //3.
    public Pageable getPageable() {
        //前端分页 默认第一页为 1  ， Spring data jpa Pageable 默认第一页为0
        Pageable pageable = null;

        //如果排序条件不为null 或 ""
        if (StringUtils.isNotBlank(sortField) || StringUtils.isNotBlank(sortOrder)) {
            //new 一个默认 降序 排序对象Sort
            Sort pageSort = new Sort(Sort.Direction.DESC, sortField);
            //否则 new 升序  排序对象Sort
            if (!sortOrder.equals("descend")) {
                pageSort = new Sort(Sort.Direction.ASC, sortField);
            }

            //如果排序条件 不为null 或 ""  分页 + 排序
            pageable = PageRequest.of(pageNo - 1, pageSize, pageSort);

        } else {
            //如果排序条件 为null 或 "" 则 只分页 不排序
            pageable = PageRequest.of(pageNo - 1, pageSize);
        }

        return pageable;
    }
}
