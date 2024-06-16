package com.mars.cds.support;

import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 分页数据
 */
@Data
public class PageWork<T> {
    // 页码名称
    private static final String pageNoName = "pageNo";
    // 分页查询时的每页的记录数77
    private static final String pageSizeName = "pageSize";
    // 分页查询时的总行数
    private static final String rowCountName = "rowCount";
    // 分页查询时的总页数
    private static final String pageCountName = "pageCount";
    // pageNo最小不低于1
    private static final int pageNoMinValue = 1;
    // pageSize最大不超过50
    private static final int pageSizeMaxValue = 50;
    // pageSize最小不低于1
    private static final int pageSizeMinValue = 1;

    // pageNo最小不低于1
    private int pageNo = pageNoMinValue;
    // pageSize最大不超过50
    private int pageSize = pageSizeMaxValue;
    // 分页位置
    private int position;
    // 行数
    private int rowCount;
    // 总页数
    private int pageCount;
    // 返回数据
    private T result;

    public PageWork(String pageNoStr, String pageSizeStr, int rowCount) {
        if (NumberUtils.isCreatable(pageNoStr)) {
            this.pageNo = Integer.valueOf(pageNoStr);
            if (this.pageNo < pageNoMinValue) {
                this.pageNo = pageNoMinValue;
            }
        }
        if (NumberUtils.isCreatable(pageSizeStr)) {
            this.pageSize = Integer.valueOf(pageSizeStr);
            if (this.pageSize < pageSizeMinValue) {
                this.pageSize = pageSizeMinValue;
            } else if (this.pageSize > pageSizeMaxValue) {
                this.pageSize = pageSizeMaxValue;
            }
        }
        this.rowCount = rowCount;
        this.pageCount = this.rowCount / this.pageSize;
        if (this.rowCount % this.pageSize != 0)
            this.pageCount = this.pageCount + 1;

//        if (this.pageNo > this.pageCount) {
//            this.pageNo = this.pageCount;
//        }
        this.position = (this.pageNo - 1) * this.pageSize;
        // rowCount为0时
        this.position = this.position < 0 ? 0 : this.position;
    }
}
