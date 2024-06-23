package com.mars.cds.controller;

import com.alibaba.fastjson.JSONObject;
import com.mars.cds.constant.FrameConstant;
import com.mars.cds.entity.BizBankCard;
import com.mars.cds.entity.BizUser;
import com.mars.cds.service.BizBankCardService;
import com.mars.cds.support.LogUtils;
import com.mars.cds.support.RespBody;
import com.mars.cds.support.RespBodyUtils;
import com.mars.cds.vo.BankCardVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/api/bank")
@RestController
public class BizBankCardController {

    static final Logger log = LoggerFactory.getLogger(BizBankCardController.class.getSimpleName());

    @Resource
    private BizBankCardService bizBankCardService;

    /**
     * 添加银行卡
     */
    @ResponseBody
    @PostMapping("/saveBankCard")
    public RespBody<String> saveBankCard(@RequestBody JSONObject params, HttpServletRequest request) {
        try {
            BizUser user = (BizUser) request.getAttribute(FrameConstant.userKey);
            BankCardVo bankCardVo = JSONObject.toJavaObject(params, BankCardVo.class);
            return bizBankCardService.saveBankCard(user, bankCardVo);
        } catch (Exception e) {
            LogUtils.error(log, "添加银行卡错误", e, params);
            return RespBodyUtils.failure("System error");
        }
    }

    /**
     * 查询默认银行卡
     */
    @ResponseBody
    @PostMapping("/queryBankCardDefault")
    public RespBody<BankCardVo> queryBankCardDefault(HttpServletRequest request) {
        try {
            BizUser user = (BizUser) request.getAttribute(FrameConstant.userKey);
            return bizBankCardService.queryBankCardDefault(user.getId());
        } catch (Exception e) {
            LogUtils.error(log, "查询默认银行卡错误", e);
            return RespBodyUtils.failure("System error");
        }
    }

    /**
     * 修改默认银行卡
     */
    @ResponseBody
    @PostMapping("/updateBankCardDefault")
    public RespBody<String> updateBankCardDefault(@RequestBody JSONObject params, HttpServletRequest request) {
        try {
            BizUser user = (BizUser) request.getAttribute(FrameConstant.userKey);
            BizBankCard bankCard = JSONObject.toJavaObject(params, BizBankCard.class);
            return bizBankCardService.updateBankCardDefault(user, bankCard);
        } catch (Exception e) {
            LogUtils.error(log, "修改默认银行卡错误", e, params);
            return RespBodyUtils.failure("System error");
        }
    }

    /**
     * 查询银行列表
     */
    @ResponseBody
    @PostMapping("/queryBankCardList")
    public RespBody<List<BankCardVo>> queryBankCardList(HttpServletRequest request) {
        try {
            BizUser user = (BizUser) request.getAttribute(FrameConstant.userKey);
            return bizBankCardService.queryBankCardList(user.getId());
        } catch (Exception e) {
            LogUtils.error(log, "查询银行列表错误", e);
            return RespBodyUtils.failure("System error");
        }
    }

    /**
     * 删除银行
     */
    @ResponseBody
    @PostMapping("/deleteBackCard")
    public RespBody<String> deleteBackCard(@RequestBody JSONObject params, HttpServletRequest request) {
        try {
            BizUser user = (BizUser) request.getAttribute(FrameConstant.userKey);
            BizBankCard bankCard = JSONObject.toJavaObject(params, BizBankCard.class);
            return bizBankCardService.deleteBackCard(user, bankCard);
        } catch (Exception e) {
            LogUtils.error(log, "删除银行错误", e, params);
            return RespBodyUtils.failure("System error");
        }
    }
}
