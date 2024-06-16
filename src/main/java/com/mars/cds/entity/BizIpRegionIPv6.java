package com.mars.cds.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class BizIpRegionIPv6 implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标识Id
     */
    private int id;
    /**
     * ip号开始
     */
    private BigInteger start;
    /**
     * ip号结束
     */
    private BigInteger end;
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