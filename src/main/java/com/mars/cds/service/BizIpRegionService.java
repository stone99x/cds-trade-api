package com.mars.cds.service;

import com.mars.cds.entity.BizIpRegion;
import com.mars.cds.mapper.BizIpRegionMapper;
import com.mars.cds.support.LogUtils;
import com.mars.cds.support.IPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BizIpRegionService {

    static final Logger log = LoggerFactory.getLogger(BizIpRegionService.class.getSimpleName());

    @Resource
    private BizIpRegionMapper bizIpRegionMapper;

    public BizIpRegion selectOneByIpNum(String ipv4) {
        try {
            long ipNum = IPUtils.ipv4Dot2LongIP(ipv4);
            return bizIpRegionMapper.selectOneByIpNum(ipNum);
        } catch (Exception e) {
            BizIpRegion bizIpRegion = new BizIpRegion();
            bizIpRegion.setCountry("unknown");
            bizIpRegion.setCity("unknown");
            bizIpRegion.setProvince("unknown");
            bizIpRegion.setCountryShort("unknown");
            LogUtils.error(log, "根据ipv4查地区错误", e, ipv4);
            return bizIpRegion;
        }
    }
}
