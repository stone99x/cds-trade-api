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

@RequestMapping("/api/opt")
@RestController
public class BizOtpController {

    static final Logger log = LoggerFactory.getLogger(BizOtpController.class.getSimpleName());

    @Resource
    private BizAppHotUpgradeService bizAppHotUpgradeService;

    /**
     * 查获取opt代码
     */

    @ResponseBody
    @PostMapping("/getOtpCode")
    public RespBody<JSONObject> getOtpCode() {
        try {
            return bizAppHotUpgradeService.queryHotUpgrade();
        } catch (Exception e) {
            LogUtils.error(log, "查获取opt代码错误", e);
            return RespBodyUtils.failure("System error");
        }
    }

}
