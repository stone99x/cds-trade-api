package com.mars.cds.controller;

import com.alibaba.fastjson.JSONObject;
import com.mars.cds.constant.FrameConstant;
import com.mars.cds.entity.BizUser;
import com.mars.cds.service.BizUserService;
import com.mars.cds.support.LogUtils;
import com.mars.cds.support.RespBody;
import com.mars.cds.support.RespBodyUtils;
import com.mars.cds.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户登录注册操作
 */
@RequestMapping("/api/user")
@RestController
public class BizUserController {

    static final Logger log = LoggerFactory.getLogger(BizUserController.class.getSimpleName());

    @Resource
    private BizUserService bizUserService;

    /**
     * 检查会话是否有效
     */
    @ResponseBody
    @PostMapping("/checkSessionId")
    public RespBody<String> checkSessionId() {
        return RespBodyUtils.success("OK");
    }

    /**
     * 获取注册otp
     */
    @ResponseBody
    @PostMapping("/getRegisterOtp")
    public RespBody<Integer> getRegisterOtp(@RequestBody JSONObject params) {
        try {
            return bizUserService.getRegisterOtp(params);
        } catch (Exception e) {
            LogUtils.error(log, "获取注册otp失败", e, params);
            return RespBodyUtils.failure("Failed to obtain registration otp");
        }
    }

    /**
     * 注册用户
     */
    @ResponseBody
    @PostMapping("/register")
    public RespBody<String> register(@RequestBody JSONObject params) {
        BizUser paramBean = JSONObject.toJavaObject(params, BizUser.class);
        try {
            return bizUserService.registerUser(params, paramBean);
        } catch (Exception e) {
            LogUtils.error(log, "注册失败", e, params);
            return RespBodyUtils.failure("Registration failed");
        }
    }

    /**
     * 登录
     */
    @ResponseBody
    @PostMapping("/login")
    public RespBody<UserVo> login(@RequestBody JSONObject params, HttpServletRequest request) {
        BizUser paramBean = JSONObject.toJavaObject(params, BizUser.class);
        try {
            String dataSources = params.getString("dataSources");
            return bizUserService.login(dataSources, paramBean, request);
        } catch (Exception e) {
            LogUtils.error(log, "登录失败", e, params);
            return RespBodyUtils.failure("Login failed");
        }
    }

    /**
     * 保持在线状态
     */
    @ResponseBody
    @GetMapping("/refresh")
    public RespBody refresh(@RequestHeader String sessionId, HttpServletRequest request) {
        BizUser paramBean = (BizUser) request.getAttribute(FrameConstant.userKey);
        try {
            return bizUserService.refresh(paramBean, sessionId);
        } catch (Exception e) {
            LogUtils.error(log, "刷新失败", e, sessionId);
            return RespBodyUtils.failure("Refresh failed");
        }
    }

    /**
     * 退出登录
     */
    @ResponseBody
    @PostMapping("/logOut")
    public RespBody<String> logOut(@RequestHeader String sessionId, HttpServletRequest request) {
        BizUser paramBean = (BizUser) request.getAttribute(FrameConstant.userKey);
        try {
            return bizUserService.logOut(paramBean, sessionId);
        } catch (Exception e) {
            LogUtils.error(log, "退出失败", e, sessionId);
            return RespBodyUtils.failure("LogOut failed");
        }
    }

    /**
     * 忘记密码获取otp
     */
    @ResponseBody
    @PostMapping("/getForgetPwdOtp")
    public RespBody<Integer> getForgetPwdOtp(@RequestBody JSONObject params) {
        try {
            return bizUserService.getForgetPwdOtp(params);
        } catch (Exception e) {
            LogUtils.error(log, "忘记密码获取otp失败", e, params);
            return RespBodyUtils.failure("Failed to obtain otp");
        }
    }

    /**
     * 验证忘记密码获取otp
     */
    @ResponseBody
    @PostMapping("/checkForgetPwdOtp")
    public RespBody<String> checkForgetPwdOtp(@RequestBody JSONObject params) {
        try {
            return bizUserService.checkForgetPwdOtp(params);
        } catch (Exception e) {
            LogUtils.error(log, "验证忘记密码获取otp失败", e, params);
            return RespBodyUtils.failure("Failed to verify OTP");
        }
    }

    /**
     * 忘记密码重置(未登录)
     */
    @ResponseBody
    @PostMapping("/forgetResetPassword")
    public RespBody<String> forgetResetPassword(@RequestBody JSONObject params) {
        try {
            return bizUserService.forgetResetPassword(params);
        } catch (Exception e) {
            LogUtils.error(log, "忘记密码重置(未登录)错误", e, params);
            return RespBodyUtils.failure("Password reset failed");
        }
    }

    /**
     * 查询用户信息
     */
    @ResponseBody
    @PostMapping("/queryUserDetail")
    public RespBody<UserVo> queryUserDetail(@RequestHeader String sessionId, HttpServletRequest request) {
        BizUser paramBean = (BizUser) request.getAttribute(FrameConstant.userKey);
        try {
            return bizUserService.queryUserDetail(sessionId, paramBean);
        } catch (Exception e) {
            LogUtils.error(log, "忘记密码重置(未登录)错误", e, sessionId);
            return RespBodyUtils.failure("Query user info failed");
        }
    }
}
