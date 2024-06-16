package com.mars.cds.service;

import com.mars.cds.entity.BizConfig;
import com.mars.cds.mapper.BizConfigMapper;
import com.mars.cds.support.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BizConfigService {

    static final Logger log = LoggerFactory.getLogger(BizConfigService.class.getSimpleName());

    @Resource
    private BizConfigMapper bizConfigMapper;

    public BizConfig selectConfig(String name) {
        try {
            return bizConfigMapper.selectConfig(name);
        } catch (Exception e) {
            LogUtils.error(log, "查询配置错误", e, name);
            return null;
        }
    }
}
