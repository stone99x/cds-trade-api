package com.mars.cds.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 提现
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BizWithdraw implements Serializable {

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
     * 加密币Id
     */
    private int cryptoId;
    /**
     * 网路Id
     */
    private int networkId;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 交易编号
     */
    private String txId;
    /**
     * 钱包地址
     */
    private String address;
    /**
     * 提现金额，单位：元
     */
    private BigDecimal amount;
    /**
     * 网络手续费，单位：元
     */
    private BigDecimal networkFee;
    /**
     * 1处理中，2成功，3失败，4取消
     */
    private int status;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTie;

}
