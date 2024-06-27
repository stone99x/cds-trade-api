package com.mars.cds.service;

import com.alibaba.fastjson.JSONObject;
import com.mars.cds.CommonPool;
import com.mars.cds.cache.RedisClientBean;
import com.mars.cds.constant.FrameConstant;
import com.mars.cds.entity.BizIpRegion;
import com.mars.cds.entity.BizIpRegionIPv6;
import com.mars.cds.entity.BizUser;
import com.mars.cds.entity.BizUserLoginLog;
import com.mars.cds.mapper.BizUserMapper;
import com.mars.cds.support.*;
import com.mars.cds.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class BizUserService {

    static final Logger log = LoggerFactory.getLogger(BizUserService.class.getSimpleName());

    @Value("${pojo.session.timeout:600}")
    private int timeout;

    @Resource
    private BizUserMapper bizUserMapper;
    @Resource
    private RedisClientBean redisClientBean;
    @Resource
    private BizIpRegionService bizIpRegionService;
    @Resource
    private BizIpRegionIPv6Service bizIpRegionIPv6Service;
    @Resource
    private BizOtpService bizOtpService;

    // 获取注册otp
    public RespBody<Integer> getRegisterOtp(JSONObject params) {
        String phone = params.getString("phone");
        if (StringUtils.isEmpty(phone)) {
            LogUtils.info(log, "注册时手机号码不能为空");
            return RespBodyUtils.failure("Mobile phone number cannot be empty");
        }
        if (!phone.matches(FrameConstant.phoneRegx)) {
            LogUtils.info(log, "注册时手机号码格式不正确", phone);
            return RespBodyUtils.failure("The mobile number format is incorrect");
        }
        // 先获取缓存是否存在，若存在等失效后，才可以发送
        String appRegisterKey = String.format(FrameConstant.appRegisterKey, phone);
        String registerOtpCode = redisClientBean.get(appRegisterKey);
        if (!StringUtils.isEmpty(registerOtpCode)) {
            long et = System.currentTimeMillis();
            long st = Long.parseLong(registerOtpCode.substring(6));
            int sec = (FrameConstant.otpExpired - (int) ((et - st) / 1000)); // 剩余有效秒数
            LogUtils.info(log, "注册时OTP已发送, 请稍后再试", phone, registerOtpCode.substring(6));
            return RespBodyUtils.success("OTP sent successfully", sec);
        }
        //查询用户是否存在在, 通过电话号码进行判断
        int queryExistBizUser = bizUserMapper.queryExistBizUser(phone);

        //如果存在，该用户已经存在，请登录
        if (queryExistBizUser > NumberUtil.NUMBER_ZERO) {
            LogUtils.info(log, "注册时手机号码已注册，请重新输入", phone);
            return RespBodyUtils.failure("This number is registered, please re-enter");
        }
        //随机生成6位验证码
        String code = FrameUtils.getRandomIntLen(6);
        String content = String.format("Your OTP is: %s, please do not give it to others to avoid losses", code);
        redisClientBean.put(appRegisterKey, String.format("%s%s", code, System.currentTimeMillis()), FrameConstant.otpExpired);
        bizOtpService.sendOtp(phone, content);
        LogUtils.info(log, "注册OTP代码", phone, code);
        return RespBodyUtils.success("OTP sent successfully", FrameConstant.otpExpired);
    }

    // 用户注册
    public RespBody<String> registerUser(JSONObject params, BizUser bizUser) {
        if (StringUtils.isEmpty(bizUser.getPhone())) {
            LogUtils.info(log, "注册手机号码不能为空", bizUser.getPhone());
            return RespBodyUtils.failure("Phone number can not be empty");
        }
        String phone = bizUser.getPhone();
        if (!phone.matches(FrameConstant.phoneRegx)) {
            LogUtils.info(log, "注册时手机号码格式不正确", phone);
            return RespBodyUtils.failure("The mobile number format is incorrect");
        }
        String otpCode = params.getString("otpCode");
        if (StringUtils.isEmpty(otpCode)) {
            LogUtils.info(log, "注册时OTP不能为空", phone);
            return RespBodyUtils.failure("Registration OTP cannot be empty");
        }
        if (!otpCode.matches(FrameConstant.otpCodeRegx)) {
            LogUtils.info(log, "注册时OTP格式不正确", phone);
            return RespBodyUtils.failure("The OTP format is incorrect");
        }
        if (StringUtils.isBlank(bizUser.getPassword())) {
            LogUtils.info(log, "注册时密码不能为空", phone);
            return RespBodyUtils.failure("The password cannot be empty");
        }
        if (!bizUser.getPassword().matches(FrameConstant.passwordRegx)) {
            LogUtils.info(log, "注册时密码长度不正确", phone);
            return RespBodyUtils.failure("The password length should be between 6 and 18 characters.");
        }
        String confirmPassword = params.getString("confirmPassword");
        if (!bizUser.getPassword().equals(confirmPassword)) {
            LogUtils.info(log, "注册时确认密码不匹配", phone);
            return RespBodyUtils.failure("Confirm password mismatch");
        }
        //查询redis里面的OTP，校验通过，则注册成功
        String appRegisterKey = String.format(FrameConstant.appRegisterKey, phone);
        String registerOtpCode = redisClientBean.get(appRegisterKey);
        if (StringUtils.isEmpty(registerOtpCode)) {
            LogUtils.info(log, "注册时OTP为空", phone);
            return RespBodyUtils.failure("Please obtain OTP first");
        }
        String realOtp = registerOtpCode.substring(0, 6);
        if (!Objects.equals(realOtp, otpCode)) {
            LogUtils.info(log, "OTP错误，请重新输入");
            return RespBodyUtils.failure("OTP error, please re-enter");
        }
        int level; // 注册用户等级
        if (StringUtils.isEmpty(bizUser.getInviteId())) {
            level = 1;
            bizUser.setInviteId("");
        } else {
            if (!bizUser.getInviteId().matches(FrameConstant.inviteRegx)) {
                LogUtils.info(log, "邀请码格式不正确", phone, bizUser.getInviteId());
                return RespBodyUtils.failure("The invitation code format is incorrect");
            }
            // 查询邀请码id是否存在
            BizUser userInviteId = bizUserMapper.queryExistInviteId(bizUser.getInviteId());
            if (userInviteId == null) {
                LogUtils.info(log, "邀请码不正确", phone, bizUser.getInviteId());
                return RespBodyUtils.failure("Invalid invitation code");
            }
            if (userInviteId.getStatus() != NumberUtil.NUMBER_ONE) {
                LogUtils.info(log, "邀请码不可用", phone, bizUser.getInviteId());
                return RespBodyUtils.failure("Invitation code not available");
            }
            level = userInviteId.getLevel() + NumberUtil.NUMBER_ONE;
        }

        //查询用户是否存在在, 通过电话号码进行判断
        int queryExistBizUser = bizUserMapper.queryExistBizUser(phone);
        //如果存在，该用户已经存在，请登录
        if (queryExistBizUser > NumberUtil.NUMBER_ZERO) {
            LogUtils.info(log, "注册时手机号码已注册，请重新输入", phone);
            return RespBodyUtils.failure("This number is registered, please re-enter");
        }
        // 清除注册otp
        redisClientBean.remove(appRegisterKey);

        // 当前注册用户自己的邀请码
        String selfId = FrameUtils.getRandomStrLen(NumberUtil.NUMBER_EIGHT);
        bizUser.setSelfId(selfId);
        bizUser.setLevel(level);
        // 保存注册用户
        bizUserMapper.insertBizUser(bizUser);
        return RespBodyUtils.success("Registered successfully");
    }


    /**
     * 用户登录
     *
     * @param dataSources 数据来源：app，h5
     */
    public RespBody<UserVo> login(String dataSources, BizUser userParam, HttpServletRequest request) {
        if (StringUtils.isBlank(userParam.getPhone())) {
            LogUtils.info(log, "登录手机号码不能为空", userParam.getPhone());
            return RespBodyUtils.failure("Phone number can not be empty");
        }
        String phone = userParam.getPhone();
        if (!phone.matches(FrameConstant.phoneRegx)) {
            LogUtils.info(log, "登录手机号码格式不正确", phone);
            return RespBodyUtils.failure("The mobile number format is incorrect");
        }
        if (StringUtils.isBlank(userParam.getPassword())) {
            LogUtils.info(log, "登录密码不能为空", userParam.getPhone());
            return RespBodyUtils.failure("The password cannot be empty");
        }
        if (!userParam.getPassword().matches(FrameConstant.passwordRegx)) {
            LogUtils.info(log, "登录密码长度不正确", phone);
            return RespBodyUtils.failure("The password length should be between 6 and 18 characters.");
        }
        //验证帐号密码是否正确
        BizUser userInfo = bizUserMapper.queryLogin(userParam.getPhone(), userParam.getPassword());

        if (Objects.isNull(userInfo)) {
            LogUtils.info(log, "帐号密码不正确", userParam.getPhone());
            return RespBodyUtils.failure("The account password is incorrect");
        }

        // 状态为1表示，可用；其他均不可以登录
        if (userInfo.getStatus() != NumberUtil.NUMBER_ONE || userInfo.getDel() != NumberUtil.NUMBER_ONE) {
            LogUtils.info(log, "此帐号已被禁用", userParam.getPhone());
            return RespBodyUtils.failure("This account has been disabled");
        }

        String sessionId = FrameUtils.getUuidString();

        // 会话缓存key
        String appSidKey = String.format(FrameConstant.appSidKey, sessionId);
        String userInfoStr = JSONObject.toJSONString(userInfo);
        redisClientBean.put(appSidKey, userInfoStr);

        // 会话有效key
        String appRefreshKey = String.format(FrameConstant.appRefreshKey, sessionId);
        JSONObject userRefreshJo = new JSONObject();
        userRefreshJo.put("id", userInfo.getId());
        userRefreshJo.put("effectiveTime", System.currentTimeMillis() + (timeout * 1000));
        String userRefreshStr = userRefreshJo.toJSONString();
        redisClientBean.put(appRefreshKey, userRefreshStr);

        // 记录会话集合
        String appListKey = String.format(FrameConstant.appListKey, userInfo.getId());
        BoundListOperations<String, String> userBoundList = redisClientBean.
                getRedisTemplate().boundListOps(appListKey);
        userBoundList.rightPush(appSidKey);

        CommonPool.submit(() -> {
            try {
                String remoteAddrIp = FrameUtils.getRemoteAddr2(request);
                LogUtils.info(log, "登录用户IP", userParam.getPhone(), remoteAddrIp);

                String city;
                if (remoteAddrIp.matches(FrameConstant.ipv4Regx)) {
                    // ipv4
                    BizIpRegion bizIpRegion = bizIpRegionService.selectOneByIpNum(remoteAddrIp);
                    city = bizIpRegion.getCity();
                    LogUtils.info(log, "登录用户IPv4", userParam.getPhone(), remoteAddrIp, city);
                } else {
                    // ipv6
                    BizIpRegionIPv6 bizIpRegion = bizIpRegionIPv6Service.selectOneByIpNum(remoteAddrIp);
                    city = bizIpRegion.getCity();
                    LogUtils.info(log, "登录用户IPv6", userParam.getPhone(), remoteAddrIp, city);
                }

                // 更新用户登录信息
                BizUser updateUserInfo = BizUser.builder().id(userInfo.getId()).ip(remoteAddrIp)
                        .ipRegion(city).firstLoginTime(userInfo.getFirstLoginTime()).build();
                if (FrameConstant.sourceH5.equalsIgnoreCase(dataSources)) {
                    updateUserInfo.setOnlineH5Status(NumberUtil.NUMBER_ONE);
                } else {
                    updateUserInfo.setOnlineAppStatus(NumberUtil.NUMBER_ONE);
                }
                bizUserMapper.updateUserLastLoginInfo(updateUserInfo);

                // 记录登录日志
                BizUserLoginLog userLoginLog = BizUserLoginLog.builder()
                        .userId(userInfo.getId())
                        .ip(remoteAddrIp)
                        .ipRegion(city)
                        .dataSources(dataSources)
                        .build();
                bizUserMapper.insertUserLoginLog(userLoginLog);
            } catch (Exception e) {
                LogUtils.error(log, "异步处理登录后信息维护错误", e, userParam.getPhone());
            }
        });
        UserVo userVo = UserVo.builder().sessionId(sessionId).build();
        ReflectUtils.transform(userVo, userInfo);
        return RespBodyUtils.success("Login successfully", userVo);
    }

    /**
     * 退出登录
     *
     * @param sessionId
     * @return
     */
    public RespBody<String> logOut(BizUser bizUser, String sessionId) {
        try {
            String appSidKey = String.format(FrameConstant.appSidKey, sessionId);
            String appRefreshKey = String.format(FrameConstant.appRefreshKey, sessionId);
            bizUserMapper.updateOfflineStatus(bizUser.getId());
            // 删除缓存信息
            redisClientBean.remove(appSidKey);
            redisClientBean.remove(appRefreshKey);

            // 删除集合中的元素keys
            String appListKey = String.format(FrameConstant.appListKey, bizUser.getId());
            BoundListOperations<String, String> userBoundList = redisClientBean.
                    getRedisTemplate().boundListOps(appListKey);
            userBoundList.remove(1, appSidKey);
            LogUtils.info(log, "退出成功", sessionId);
            return RespBodyUtils.success("Exit successfully");
        } catch (Exception e) {
            LogUtils.error(log, "退出失败", e, sessionId);
            return RespBodyUtils.failure("Exit failure");
        }
    }

    // 找回密码获取otp
    public RespBody<Integer> getForgetPwdOtp(JSONObject params) {
        String phone = params.getString("phone");
        if (StringUtils.isEmpty(phone)) {
            LogUtils.info(log, "找回密码手机号码不能为空");
            return RespBodyUtils.failure("Mobile phone number cannot be empty");
        }
        if (!phone.matches(FrameConstant.phoneRegx)) {
            LogUtils.info(log, "找回密码手机号码格式不正确", phone);
            return RespBodyUtils.failure("The mobile number format is incorrect");
        }
        // 先获取缓存是否存在，若存在等失效后，才可以发送
        String appForgetPwdKey = String.format(FrameConstant.appForgetPwdKey, phone);
        String forgetPwdOtpCode = redisClientBean.get(appForgetPwdKey);
        if (!StringUtils.isEmpty(forgetPwdOtpCode)) {
            long et = System.currentTimeMillis();
            long st = Long.parseLong(forgetPwdOtpCode.substring(6));
            int sec = (FrameConstant.otpExpired - (int) ((et - st) / 1000)); // 剩余有效秒数
            LogUtils.info(log, "找回密码OTP已发送, 请稍后再试", phone, forgetPwdOtpCode.substring(6));
            return RespBodyUtils.success("OTP sent successfully", sec);
        }
        // 查询用户是否存在在, 通过电话号码进行判断
        int queryExistBizUser = bizUserMapper.queryExistBizUser(phone);

        // 如果用户不正存在
        if (queryExistBizUser == NumberUtil.NUMBER_ZERO) {
            LogUtils.info(log, "找回密码手机号码未注册", phone);
            return RespBodyUtils.failure("This mobile number is not registered");
        }
        // 随机生成6位验证码
        String code = FrameUtils.getRandomIntLen(6);
        String content = String.format("Your OTP is: %s, please do not give it to others to avoid losses", code);
        redisClientBean.put(appForgetPwdKey, String.format("%s%s", code, System.currentTimeMillis()), FrameConstant.otpExpired);
        bizOtpService.sendOtp(phone, content);
        LogUtils.info(log, "找回密码OTP代码", phone, code);
        return RespBodyUtils.success("OTP sent successfully", FrameConstant.otpExpired);
    }

    // 验证找回密码获取otp
    public RespBody<String> checkForgetPwdOtp(JSONObject params) {
        String phone = params.getString("phone");
        if (StringUtils.isEmpty(phone)) {
            LogUtils.info(log, "验证找回密码手机号码不能为空");
            return RespBodyUtils.failure("Mobile phone number cannot be empty");
        }
        if (!phone.matches(FrameConstant.phoneRegx)) {
            LogUtils.info(log, "验证找回密码手机号码格式不正确", phone);
            return RespBodyUtils.failure("The mobile number format is incorrect");
        }
        String otpCode = params.getString("otpCode");
        if (StringUtils.isEmpty(otpCode)) {
            LogUtils.info(log, "验证找回密码OTP不能为空", phone);
            return RespBodyUtils.failure("The OTP cannot be empty");
        }
        if (!otpCode.matches(FrameConstant.otpCodeRegx)) {
            LogUtils.info(log, "验证找回密码OTP格式不正确", phone);
            return RespBodyUtils.failure("The OTP format is incorrect");
        }
        // 先获取缓存是否存在，若存在等失效后，才可以发送
        String appForgetPwdKey = String.format(FrameConstant.appForgetPwdKey, phone);
        String forgetPwdOtpCode = redisClientBean.get(appForgetPwdKey);
        if (StringUtils.isEmpty(forgetPwdOtpCode)) {
            LogUtils.info(log, "验证找回密码根据手机号码未查到otp", phone, otpCode);
            return RespBodyUtils.failure("OTP not found");
        }
        String realOtp = forgetPwdOtpCode.substring(0, 6);
        if (!otpCode.equals(realOtp)) {
            LogUtils.info(log, "验证找回密码otp不正确", phone, otpCode, forgetPwdOtpCode);
            return RespBodyUtils.failure("The OTP is incorrect");
        }
        // 查询用户是否存在在, 通过电话号码进行判断
        int queryExistBizUser = bizUserMapper.queryExistBizUser(phone);

        // 如果用户不正存在
        if (queryExistBizUser == NumberUtil.NUMBER_ZERO) {
            LogUtils.info(log, "验证找回密码手机号码未注册", phone);
            return RespBodyUtils.failure("This mobile number is not registered");
        }
        redisClientBean.remove(appForgetPwdKey);

        // 生成token
        String token = FrameUtils.getUuidString();
        String appForgetPwdTokenKey = String.format(FrameConstant.appForgetPwdTokenKey, phone);
        redisClientBean.put(appForgetPwdTokenKey, token, FrameConstant.otpExpired);
        return RespBodyUtils.success("OTP verification passed", token);
    }

    // 忘记密码重置(未登录)
    public RespBody<String> forgetResetPassword(JSONObject params) {
        String phone = params.getString("phone");
        if (StringUtils.isEmpty(phone)) {
            LogUtils.info(log, "忘记密码重置(未登录)手机号码不能为空");
            return RespBodyUtils.failure("Mobile phone number cannot be empty");
        }
        if (!phone.matches(FrameConstant.phoneRegx)) {
            LogUtils.info(log, "忘记密码重置(未登录)手机号码格式不正确", phone);
            return RespBodyUtils.failure("The mobile number format is incorrect");
        }
        String password = params.getString("password");
        if (StringUtils.isEmpty(password)) {
            LogUtils.info(log, "忘记密码重置(未登录)密码不能为空", phone);
            return RespBodyUtils.failure("The password cannot be empty");
        }
        if (!password.matches(FrameConstant.passwordRegx)) {
            LogUtils.info(log, "忘记密码重置(未登录)密码长度不正确", phone);
            return RespBodyUtils.failure("The password length should be between 6 and 18 characters.");
        }
        String confirmPassword = params.getString("confirmPassword");
        if (!password.equals(confirmPassword)) {
            LogUtils.info(log, "忘记密码重置(未登录)确认密码与设置密码不相等", phone);
            return RespBodyUtils.failure("Confirm passwords are not equal");
        }
        String token = params.getString("token");
        String appForgetPwdTokenKey = String.format(FrameConstant.appForgetPwdTokenKey, phone);
        String appForgetPwdTokenValue = redisClientBean.get(appForgetPwdTokenKey);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(appForgetPwdTokenValue)
                || !token.equals(appForgetPwdTokenValue)) {
            LogUtils.info(log, "忘记密码重置(未登录)token验证未通过", phone, token, appForgetPwdTokenValue);
            return RespBodyUtils.failure("Authentication failed");
        }

        redisClientBean.remove(appForgetPwdTokenKey);
        //验证正确之后修改密码
        BizUser resetPasswordUser = BizUser.builder().phone(phone).password(password).build();
        bizUserMapper.updatePasswordByPhone(resetPasswordUser);
        LogUtils.info(log, "密码修改成功", phone);
        return RespBodyUtils.success("Password reset complete");
    }

    /**
     * 根据用户id查询用户详情
     *
     * @param bizUser
     * @return
     */
    public RespBody<UserVo> queryUserDetail(String sessionId, BizUser bizUser) {
        BizUser userInfo = bizUserMapper.queryUserDetailById(bizUser.getId());
        UserVo userVo = UserVo.builder().sessionId(sessionId).build();
        ReflectUtils.transform(userVo, userInfo);
        return RespBodyUtils.success("Description Querying user details succeeded", userVo);
    }


    /**
     * 修改用户基础信息
     *
     * @param bizUser
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public RespBody updateBasicInfo(BizUser bizUser) {
        if (bizUser.getId() == NumberUtil.NUMBER_ZERO) {
            LogUtils.info(log, "用户id不能为空");
            return RespBodyUtils.failure("The user id cannot be empty");
        }
        //验证正确之后修改密码
        bizUserMapper.updateBasicInfo(bizUser);
        LogUtils.info(log, "修改用户基础信息成功");
        return RespBodyUtils.success("Basic user information is modified successfully. Procedure");
    }

    /**
     * 刷新在线时间
     *
     * @param bizUser
     * @return
     */
    public RespBody<String> refresh(BizUser bizUser, String sessionId) {
        try {
            String appRefreshKey = String.format(FrameConstant.appRefreshKey, sessionId);
            String userRefreshCacheStr = redisClientBean.get(appRefreshKey);
            JSONObject userRefreshJo = JSONObject.parseObject(userRefreshCacheStr);
            userRefreshJo.put("effectiveTime", System.currentTimeMillis() + (timeout * 1000));
            String userRefreshStr = userRefreshJo.toJSONString();
            redisClientBean.put(appRefreshKey, userRefreshStr);
            return RespBodyUtils.success("Refresh successfully");
        } catch (Exception e) {
            LogUtils.error(log, "refresh会话失败", e, bizUser.getId(), sessionId);
            return RespBodyUtils.failure("Refresh failure");
        }
    }

}
