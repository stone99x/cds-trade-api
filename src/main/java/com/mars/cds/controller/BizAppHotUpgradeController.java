package com.mars.cds.controller;

import com.alibaba.fastjson.JSONObject;
import com.mars.cds.service.BizAppHotUpgradeService;
import com.mars.cds.support.LogUtils;
import com.mars.cds.support.RespBody;
import com.mars.cds.support.RespBodyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/api/hotUpgrade")
@RestController
public class BizAppHotUpgradeController {

    static final Logger log = LoggerFactory.getLogger(BizAppHotUpgradeController.class.getSimpleName());

    @Resource
    private BizAppHotUpgradeService bizAppHotUpgradeService;

    /**
     * 查询app热更记录
     */

    @ResponseBody
    @PostMapping("/queryHotUpgrade")
    public RespBody<JSONObject> queryHotUpgrade() {
        try {
            return bizAppHotUpgradeService.queryHotUpgrade();
        } catch (Exception e) {
            LogUtils.error(log, "查询app热更记录错误", e);
            return RespBodyUtils.failure("System error");
        }
    }

}
