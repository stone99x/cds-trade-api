package com.mars.cds.mapper;

import com.mars.cds.entity.BizIpRegion;
import org.springframework.stereotype.Repository;

@Repository
public interface BizIpRegionMapper {
    BizIpRegion selectOneByIpNum(long ipNum);
}
