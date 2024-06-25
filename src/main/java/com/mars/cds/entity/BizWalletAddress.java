package com.mars.cds.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 钱包地址
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BizWalletAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标识Id
     */
    private int id;
    /**
     * 用户Id
     */
    private int userId;
    /**
     * 网路Id
     */
    private int networkId;
    /**
     * 钱包地址
     */
    private String address;
    /**
     * 删除状态，1：正常；2：删除
     */
    private int del;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTie;

}
