package com.mars.cds.mapper;

import com.mars.cds.entity.BizAppHotUpgrade;
import org.springframework.stereotype.Repository;

@Repository
public interface BizAppHotUpgradeMapper {

    BizAppHotUpgrade selectOne();
}
