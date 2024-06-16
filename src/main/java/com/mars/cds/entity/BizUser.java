package com.mars.cds.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 用户表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BizUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private int id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 密码
     */
    private String password;
    /**
     * 当前用户的邀请码
     */
    private String selfId;
    /**
     * 邀请人员的邀请码
     */
    private String inviteId;
    /**
     * 用户等级
     */
    private int level;
    /**
     * 余额，单位：元
     */
    private BigDecimal balance;
    /**
     * 第一次登录时间
     */
    private String firstLoginTime;
    /**
     * 最后一次登录时间
     */
    private String lastLoginTime;
    /**
     * 登录Ip
     */
    private String ip;
    /**
     * 登录地区
     */
    private String ipRegion;
    /**
     * 用户状态,1启用；2禁用；
     */
    private int status;
    /**
     * h5在线状态，1在线；2离线
     */
    private int onlineH5Status;
    /**
     * app在线状态：1:在线，2离线
     */
    private int onlineAppStatus;
    /**
     * 备注
     */
    private String remark;
    /**
     * 提现状态,1开启；2关闭
     */
    private int withdrawStatus;
    /**
     * 删除状态，1：正常；2：删除
     */
    private int del;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;

}
