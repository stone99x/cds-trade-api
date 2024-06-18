package com.mars.cds.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVo implements Serializable {

    private static final long serialVersionUID = 219137702729117273L;

    // 会话id
    private String sessionId;
    /**
     * 用户Id
     */
    private int id;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 当前用户的邀请码
     */
    private String selfId;
    /**
     * 余额，单位：元
     */
    private BigDecimal balance;
    /**
     * 头像
     */
    private String avatar;
}
