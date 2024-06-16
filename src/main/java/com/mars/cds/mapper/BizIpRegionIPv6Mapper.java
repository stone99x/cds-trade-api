package com.mars.cds.mapper;

import com.mars.cds.entity.BizIpRegionIPv6;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface BizIpRegionIPv6Mapper {
    BizIpRegionIPv6 selectOneByIpNum(BigInteger ipNum);
}
