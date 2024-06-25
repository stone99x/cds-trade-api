package com.mars.cds.controller;

import com.alibaba.fastjson.JSONObject;
import com.mars.cds.constant.FrameConstant;
import com.mars.cds.entity.BizUser;
import com.mars.cds.entity.BizWalletAddress;
import com.mars.cds.service.BizWalletAddressService;
import com.mars.cds.support.LogUtils;
import com.mars.cds.support.RespBody;
import com.mars.cds.support.RespBodyUtils;
import com.mars.cds.vo.WalletAddressVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/api/user")
@RestController
public class BizWalletAddressController {

    static final Logger log = LoggerFactory.getLogger(BizWalletAddressService.class.getSimpleName());

    @Resource
    private BizWalletAddressService bizWalletAddressService;

    /**
     * 添加钱包地址
     */
    @ResponseBody
    @PostMapping("/saveWalletAddress")
    public RespBody<String> saveWalletAddress(@RequestBody JSONObject params, HttpServletRequest request) {
        try {
            BizUser user = (BizUser) request.getAttribute(FrameConstant.userKey);
            BizWalletAddress walletAddress = JSONObject.toJavaObject(params, BizWalletAddress.class);
            return bizWalletAddressService.saveWalletAddress(user, walletAddress);
        } catch (Exception e) {
            LogUtils.error(log, "添加钱包地址错误", e, params);
            return RespBodyUtils.failure("System error");
        }
    }

    /**
     * 查询钱包地址列表
     */
    @ResponseBody
    @PostMapping("/queryWalletAddressList")
    public RespBody<List<WalletAddressVo>> queryWalletAddressList(HttpServletRequest request) {
        try {
            BizUser user = (BizUser) request.getAttribute(FrameConstant.userKey);
            return bizWalletAddressService.queryWalletAddressList(user.getId());
        } catch (Exception e) {
            LogUtils.error(log, "查询钱包地址列表错误", e);
            return RespBodyUtils.failure("System error");
        }
    }

    /**
     * 删除钱包地址
     */
    @ResponseBody
    @PostMapping("/deleteWalletAddress")
    public RespBody<String> deleteWalletAddress(@RequestBody JSONObject params, HttpServletRequest request) {
        try {
            BizUser user = (BizUser) request.getAttribute(FrameConstant.userKey);
            BizWalletAddress walletAddress = JSONObject.toJavaObject(params, BizWalletAddress.class);
            return bizWalletAddressService.deleteWalletAddress(user, walletAddress);
        } catch (Exception e) {
            LogUtils.error(log, "删除钱包地址错误", e, params);
            return RespBodyUtils.failure("System error");
        }
    }
}
