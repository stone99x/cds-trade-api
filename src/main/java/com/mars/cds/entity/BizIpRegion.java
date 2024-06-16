package com.mars.cds.entity;

import lombok.Data;

import java.io.Serializable;


/**
 * IP区域库
 */
@Data
public class BizIpRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标识Id
     */
    private int id;
    /**
     * ip号开始
     */
    private long start;
    /**
     * ip号结束
     */
    private long end;
    /**
     * 国家简称
     */
    private String countryShort;
    /**
     * 国家名称
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;

}
