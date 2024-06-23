package com.mars.cds.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 用户银行卡
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BizBankCard implements Serializable {

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
     * 账户名称
     */
    private String payee;
    /**
     * 银行IFSC代码
     */
    private String bankIfsc;
    /**
     * 银行卡号
     */
    private String cardNumber;
    /**
     * 默认状态，1是；2否
     */
    private int priority;
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
