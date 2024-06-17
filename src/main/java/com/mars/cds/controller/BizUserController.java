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
 * 玩家登录注册操作
 */
@RequestMapping("/api/user")
@RestController
public class BizUserController {

    static final Logger log = LoggerFactory.getLogger(BizUserController.class.getSimpleName());

    @Resource
    private BizUserService bizUserService;

    /**
     * 检查会话是否有效
     *
     * @return
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
    public RespBody<String> getRegisterOtp(@RequestBody JSONObject params) {
        try {
            return bizUserService.getRegisterOtp(params);
        } catch (Exception e) {
            LogUtils.error(log, "获取注册otp失败", e, params);
            return RespBodyUtils.failure("Failed to obtain registration otp");
        }
    }

    /**
     * 注册玩家
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
     *
     * @return
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
     *
     * @param sessionId
     * @param request
     * @return
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
     *
     * @param sessionId
     * @return
     */
    @ResponseBody
    @PostMapping("/logOut")
    public RespBody logOut(@RequestHeader String sessionId, HttpServletRequest request) {
        BizUser paramBean = (BizUser) request.getAttribute(FrameConstant.userKey);
        try {
            return bizUserService.logOut(paramBean, sessionId);
        } catch (Exception e) {
            LogUtils.error(log, "退出失败", e, sessionId);
            return RespBodyUtils.failure("LogOut failed");
        }
    }

    /**
     * 修改密码
     *
     * @param params
     * @return
     */
    @ResponseBody
    @PostMapping("/updatePassword")
    public RespBody updatePassword(@RequestBody JSONObject params, HttpServletRequest request) {
        BizUser user = (BizUser) request.getAttribute(FrameConstant.userKey);
        BizUser paramBean = JSONObject.toJavaObject(params, BizUser.class);
        try {
            return bizUserService.updatePassword(paramBean, user);
        } catch (Exception e) {
            LogUtils.error(log, "密码修改错误", e, params);
            return RespBodyUtils.failure("Incorrect password change");
        }
    }

}
