package com.mars.cds.service;

import com.mars.cds.entity.BizIpRegionIPv6;
import com.mars.cds.mapper.BizIpRegionIPv6Mapper;
import com.mars.cds.support.LogUtils;
import com.mars.cds.support.IPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

@Service
public class BizIpRegionIPv6Service {

    static final Logger log = LoggerFactory.getLogger(BizIpRegionIPv6Service.class.getSimpleName());

    static BizIpRegionIPv6 bizIpRegionDefault;

    static {
        bizIpRegionDefault = new BizIpRegionIPv6();
        bizIpRegionDefault.setCountry("unknown");
        bizIpRegionDefault.setCity("unknown");
        bizIpRegionDefault.setProvince("unknown");
        bizIpRegionDefault.setCountryShort("unknown");
    }

    @Resource
    private BizIpRegionIPv6Mapper bizIpRegionIPv6Mapper;

    public BizIpRegionIPv6 selectOneByIpNum(String ipv6) {
        try {
            BigInteger ipNum = IPUtils.ipv6Dot2LongIP(ipv6);
            BizIpRegionIPv6 ipRegionIPv6 = bizIpRegionIPv6Mapper.selectOneByIpNum(ipNum);
            if (ipRegionIPv6 == null) {
                LogUtils.info(log, "根据ipv6未查到地区", ipv6);
                return bizIpRegionDefault;
            } else {
                return ipRegionIPv6;
            }
        } catch (Exception e) {
            LogUtils.error(log, "根据ipv6查地区错误", e, ipv6);
            return bizIpRegionDefault;
        }
    }
}
