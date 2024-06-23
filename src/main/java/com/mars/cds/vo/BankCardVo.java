package com.mars.cds.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankCardVo implements Serializable {

    private static final long serialVersionUID = -1L;
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
}
