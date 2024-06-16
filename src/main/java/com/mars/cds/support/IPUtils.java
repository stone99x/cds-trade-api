package com.mars.cds.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public final class IPUtils {

    static final Logger log = LoggerFactory.getLogger(IPUtils.class.getSimpleName());

    // 将 IPv4地址 转为 ip号
    public static long ipv4Dot2LongIP(String ipv4) {
        String[] ipAddressInArray = ipv4.split("\\.");
        long result = 0;
        long ip = 0;

        for (int x = 3; x >= 0; x--) {
            ip = Long.parseLong(ipAddressInArray[3 - x]);
            result |= ip << (x << 3);
        }
        return result;
    }

    // 将 IPv4号 转为 ip地址
    public static String ipv4Long2DotIP(long ipNum) {
        String result = "";
        result = ((ipNum / 16777216) % 256) + "." + ((ipNum / 65536) % 256) + "." + ((ipNum / 256) % 256) + "." + (ipNum % 256);
        return result;
    }

    public static BigInteger ipv6Dot2LongIP(String ipv6) {
        try {
            java.net.InetAddress ia = java.net.InetAddress.getByName(ipv6);
            byte byteArr[] = ia.getAddress();

            if (ia instanceof java.net.Inet6Address) {
                BigInteger ipNumber = new BigInteger(1, byteArr);
                return ipNumber;
            }
            return BigInteger.ZERO;
        } catch (Exception e) {
            LogUtils.error(log, "ipv6转ip号错误", e, ipv6);
            return BigInteger.ZERO;
        }
    }

    public static String ipv6Long2DotIP(String integer) {
        String ipStr = new BigInteger(integer).toString(16);
        String padding = new String(new char[32 - ipStr.length()]).replace("\0", "0");
        String retval = padding + ipStr;
        retval = retval.replaceAll("(.{4})", "$1:").substring(0, 39);
        return retval;
    }

}
