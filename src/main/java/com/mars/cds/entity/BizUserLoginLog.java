package com.mars.cds.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户登录日志
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizUserLoginLog implements Serializable {

    private static final long serialVersionUID = -6342233513150323567L;

    /**
     * 标识Id
     */
    private int id;

    /**
     * 用户Id
     */
    private int userId;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 登录地区
     */
    private String ipRegion;

    /**
     * 登录时间
     */
    private String loginTime;

    /**
     * 数据来源：app，h5
     */
    private String dataSources;

}
