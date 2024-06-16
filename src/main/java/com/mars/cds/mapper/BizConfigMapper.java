package com.mars.cds.mapper;

import com.mars.cds.entity.BizConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface BizConfigMapper {
    BizConfig selectConfig(String name);
}
