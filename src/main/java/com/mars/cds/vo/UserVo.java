package com.mars.cds.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVo implements Serializable {

    private static final long serialVersionUID = 219137702729117273L;

    /**
     * 用户Id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邀请Id（当前登录用户的推荐二维码）
     */
    private String selfId;

    /**
     * 邀请人员Id,type=1时，给0： 是业务员(登录的时候填写的)
     */
    private String inviteId;

    /**
     * 余额，单位：分
     */
    private Long balance;

    /**
     * 用户类型,1业务员；2用户
     */
    private int type;

    /**
     * 用户状态,1启用；2禁用；3、整条线启用；4整条线禁用
     */
    private int status;

    /**
     * '在线状态，1在线；2离线',
     */
    private Integer onlineStatus;

    /**
     * 会话id
     */
    private String sessionId;

    /**
     * 头像
     */
    private String profilePicture;

    /**
     * 性别：1:男，2:女
     */
    private Integer sex;

    /**
     * 账户id
     */
    private String remarkId;

    /**
     * 数据来源：app，h5
     */
    private String dataSources;

    /**
     * 客服地址
     */
    private String serviceUrl;

    /**
     * 客服地址
     */
    private String serviceUrlWs;

    /**
     * 用户等级
     */
    private int levelNumber;

    /**
     * 账号属性,1正常用户；2测试用户
     */
    private int userProperty;

    // 充值次数
    private int chargeCount;

    /**
     * '发送FB状态，首充状态是不已发送，1是；2否',
     */
    private int firstRechargeSendStatus;
    /**
     * 推广码
     */
    private String promoterCode;

    private int chargeCount2;
    // 是否已加业务员，1:是，2：否
    private int addContactStatus;

    /**
     * aqTelegram
     */
    private String aqTelegram;

    /**
     * aq电话
     */
    private String aqPhone;

    /**
     * aq描述
     */
    private String aqDescription;
}
