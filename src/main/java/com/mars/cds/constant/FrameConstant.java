package com.mars.cds.constant;

import java.text.SimpleDateFormat;

public class FrameConstant {

    public static final String userKey = "userKey";
    public static final String appSidKey = "app.sid.%s";
    public static final String appRefreshKey = "app.rfh.%s";
    public static final String appListKey = "app.list.%s";
    public static final String appRegisterKey = "app.reg.%s";
    public static final String appChPasswordKey = "app.chp.%s";
    public static final String sessionIdKey = "sessionId";

    public static final String dfs = "yyyy-MM-dd";
    public static final String dfs2 = "yyyy-MM-dd HH:mm:ss";
    public static final String dfs3 = "yyMM";
    public static final String dfs4 = "yyyyMMddhhmmsss";
    public static final String dfs5 = "yyMMdd";
    public static final String dfs6 = "yyyyMMdd";
    public static final String dfs7 = "yyyy-MM-dd 00:00:00";
    public static final String dfs8 = "yyyy-MM-dd 23:59:59";


    public static final ThreadLocal<SimpleDateFormat> dateFormatter = ThreadLocal.withInitial(() -> new SimpleDateFormat(dfs));
    public static final ThreadLocal<SimpleDateFormat> dateFormatter2 = ThreadLocal.withInitial(() -> new SimpleDateFormat(dfs2));
    public static final ThreadLocal<SimpleDateFormat> dateFormatter3 = ThreadLocal.withInitial(() -> new SimpleDateFormat(dfs3));
    public static final ThreadLocal<SimpleDateFormat> dateFormatter4 = ThreadLocal.withInitial(() -> new SimpleDateFormat(dfs4));
    public static final ThreadLocal<SimpleDateFormat> dateFormatter5 = ThreadLocal.withInitial(() -> new SimpleDateFormat(dfs5));

    public static final String amountZero = "0.00";

    /**
     * 保留2位小数
     */
    public static final int amountDigit2 = 2;

    // 验证手机号正则: ^(?:\+91|91|0)?[6-9]\d{9}$
    public static final String phoneRegx = "^[6-9]\\d{9}$";
    public static final String otpCodeRegx = "^\\d{6}$";
    // otp过期时间
    public static final int otpExpired = 120;
}
