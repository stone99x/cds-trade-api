package com.mars.cds.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 钱包地址
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletAddressVo implements Serializable {

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

}
