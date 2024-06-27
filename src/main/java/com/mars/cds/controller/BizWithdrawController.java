package com.mars.cds.controller;

import com.alibaba.fastjson.JSONObject;
import com.mars.cds.constant.FrameConstant;
import com.mars.cds.entity.BizUser;
import com.mars.cds.entity.BizWithdraw;
import com.mars.cds.service.BizWithdrawService;
import com.mars.cds.support.LogUtils;
import com.mars.cds.support.RespBody;
import com.mars.cds.support.RespBodyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/user")
@RestController
public class BizWithdrawController {

    static final Logger log = LoggerFactory.getLogger(BizWithdrawController.class.getSimpleName());

    @Resource
    private BizWithdrawService bizWithdrawService;

    /**
     * 创建提现单
     */
    @ResponseBody
    @PostMapping("/saveWithdraw")
    public RespBody<String> saveWithdraw(@RequestBody JSONObject params, HttpServletRequest request) {
        try {
            BizUser user = (BizUser) request.getAttribute(FrameConstant.userKey);
            BizWithdraw withdraw = JSONObject.toJavaObject(params, BizWithdraw.class);
            return bizWithdrawService.saveWithdraw(user, withdraw);
        } catch (Exception e) {
            LogUtils.error(log, "创建提现单错误", e, params);
            return RespBodyUtils.failure("System error");
        }
    }

}
