package com.mars.cds.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 全局配置表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置项名称
     */
    private String name;
    /**
     * 配置项数值
     */
    private String value;
    /**
     * 描述
     */
    private String desc;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;

}
