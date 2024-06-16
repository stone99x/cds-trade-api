package com.mars.cds.service;

import com.alibaba.fastjson.JSONObject;
import com.mars.cds.CommonPool;
import com.mars.cds.cache.RedisClientBean;
import com.mars.cds.constant.FrameConstant;
import com.mars.cds.entity.BizIpRegion;
import com.mars.cds.entity.BizIpRegionIPv6;
import com.mars.cds.entity.BizUser;
import com.mars.cds.entity.BizUserLoginLog;
import com.mars.cds.mapper.BizConfigMapper;
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
import java.util.concurrent.ThreadLocalRandom;

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
    private BizConfigMapper bizConfigMapper;

    /**
     * 保存用户：注册 code:10000
     */
    @Transactional(rollbackFor = Exception.class)
    public RespBody saveBizUser(BizUser bizUser) {

        int session = 10000;

        //电话号码不能为空
        if (StringUtils.isBlank(bizUser.getPhone())) {
            LogUtils.info(log, "电话号码不能为空", session, bizUser.getPhone());
            return RespBodyUtils.failure("The phone number cannot be empty！");
        }
        //邀请码id不能为空，如果没有不能注册
        if (StringUtils.isBlank(bizUser.getInviteId())) {
            LogUtils.info(log, "邀请id不能为空", session, bizUser.getPhone());
            return RespBodyUtils.failure("Please enter the invitation id");
        }

        //密码不能为空
        if (StringUtils.isBlank(bizUser.getPassword())) {
            LogUtils.info(log, "密码不能为空！", session, bizUser.getPhone());
            return RespBodyUtils.failure("The password cannot be empty！");
        }

        //查询同一个盘中，查询用户是否存在在,通过电话号码进行判断
        int queryExistBizUser = bizUserMapper.queryExistBizUser(bizUser.getPhone());

        //如果存在，该用户已经存在，请登录
        if (queryExistBizUser > NumberUtil.NUMBER_ZERO) {
            LogUtils.info(log, "此号码已注册，请重新输入!", session, bizUser.getPhone());
            return RespBodyUtils.failure("This number is registered, please re-enter!");
        }

        //查询redis里面的OTP，校验通过，则注册成功
        String appRegisterKey = String.format(FrameConstant.appRegisterKey, bizUser.getPhone());
        String registerOTPValue = redisClientBean.get(appRegisterKey);
        String registerOTP = "";
        if (StringUtils.isNotBlank(registerOTPValue)) {
            registerOTP = registerOTPValue.substring(0, 6);
        }

        /*if (!Objects.equals(registerOTP, bizUser.getOTP())) {
            LogUtils.info(log, "OTP错误，请重新输入");
            return RespBodyUtils.failure("OTP error, please re-enter！");
        }*/
        redisClientBean.remove(appRegisterKey);
        int min = 1;
        int max = 99;
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        Integer picture = tlr.nextInt(min, max + 1);


        int levelNumber = 0;
        //头像随机1-100
        bizUser.setSelfId(FrameUtils.getRandomStrLen(NumberUtil.NUMBER_EIGHT));
        bizUser.setInviteId(bizUser.getInviteId());
        bizUser.setUsername(bizUser.getPhone());
        String invitationCode = "";
        String promoterCode = bizUser.getInviteId();

        //查询邀请码id是否存在
        int queryExistInviteId = bizUserMapper.queryExistInviteId(invitationCode);

        if (Objects.equals(queryExistInviteId, NumberUtil.NUMBER_ZERO)) {
            LogUtils.info(log, "邀请id不正确。请重新输入", session, bizUser.getPhone(), invitationCode);
            return RespBodyUtils.failure("The invitation id is incorrect. Please re-enter it");
        }

        bizUserMapper.insertBizUser(bizUser);

        return RespBodyUtils.success("registered successfully！");
    }


    /**
     * 用户登录
     *
     * @param dataSources 数据来源：app，h5
     */
    public RespBody login(String dataSources, BizUser userParam, HttpServletRequest request) {
        //手机号码不能为空
        if (StringUtils.isBlank(userParam.getPhone())) {
            LogUtils.info(log, "电话号码不能为空", userParam.getPhone());
            return RespBodyUtils.failure("The phone number cannot be empty");
        }
        //密码不能为空
        if (StringUtils.isBlank(userParam.getPassword())) {
            LogUtils.info(log, "密码不能为空", userParam.getPhone());
            return RespBodyUtils.failure("The password cannot be empty");
        }
        try {
            //验证帐号密码是否正确
            BizUser userInfo = bizUserMapper.queryLogin(userParam.getPhone(), userParam.getPassword());

            if (Objects.isNull(userInfo)) {
                LogUtils.info(log, "帐号密码不正确", userParam.getPhone());
                return RespBodyUtils.failure("The account password is incorrect");
            }

            // 状态为1表示，可用；其他均不可以登录
            if (userInfo.getStatus() != NumberUtil.NUMBER_ONE) {
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

            //bizUserMapper.updateUserLastLoginTime(bizUserInfo);

            CommonPool.submit(() -> {
                try {
                    // 修改用户最后一次登录时间,地址，ip
                    String remoteAddrIp = FrameUtils.getRemoteAddr2(request);
                    LogUtils.info(log, "登录玩家IP", userParam.getPhone(), remoteAddrIp);

                    String city;
                    if (remoteAddrIp.matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$")) {
                        // ipv4
                        BizIpRegion bizIpRegion = bizIpRegionService.selectOneByIpNum(remoteAddrIp);
                        city = bizIpRegion.getCity();
                        LogUtils.info(log, "登录玩家IPv4", userParam.getPhone(), remoteAddrIp, city);
                    } else {
                        // ipv6
                        BizIpRegionIPv6 bizIpRegion = bizIpRegionIPv6Service.selectOneByIpNum(remoteAddrIp);
                        city = bizIpRegion.getCity();
                        LogUtils.info(log, "登录玩家IPv6", userParam.getPhone(), remoteAddrIp, city);
                    }

                    // 更新用户登录信息
                    BizUser updateUserInfo = BizUser.builder().id(userInfo.getId())
                            .ip(remoteAddrIp).ipRegion(city).build();
                    bizUserMapper.updateUserLastLoginIP(updateUserInfo);

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
            return RespBodyUtils.success("Login successfully", userInfo);
        } catch (Exception e) {
            LogUtils.error(log, "登录错误", e, userParam.getPhone());
            return RespBodyUtils.failure("Login error");
        }
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
            bizUserMapper.updateOnlineStatus(bizUser.getId());
            // 删除缓存信息
            redisClientBean.remove(appSidKey);
            redisClientBean.remove(appRefreshKey);
            LogUtils.info(log, "退出成功！", sessionId);
            return RespBodyUtils.success("Exit successfully！");
        } catch (Exception e) {
            LogUtils.error(log, "退出失败", e, sessionId);
            return RespBodyUtils.failure("Exit failure");
        }
    }

    /**
     * 重置密码
     *
     * @param bizUser
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public RespBody<String> updatePassword(BizUser bizUser, BizUser user) {
        //电话不能为空
        if (StringUtils.isBlank(bizUser.getPhone())) {
            LogUtils.info(log, "电话号码不能为空！", bizUser.getPhone());
            return RespBodyUtils.failure("The phone number cannot be empty！");
        }

        //判定当前用户的手机号码是否为空，如果为空，则未登录，不需要校验手机号码，如果登录了，需要对比两个手机号码
        /*if (!StringUtils.isBlank(bizUser.getUserPhone())) {
            if (!bizUser.getPhone().equals(bizUser.getUserPhone())) {
                LogUtils.info(log, "请输入当前注册的手机号码！", bizUser.getPhone());
                return RespBodyUtils.failure("Local please enter the correct mobile phone number ！");
            }
        }*/

        //查询电话号码是否存在，如果存在获取otp
        // 修改密码
        int queryExistBizUser = bizUserMapper.queryExistBizUser(bizUser.getPhone());
        if (queryExistBizUser == NumberUtil.NUMBER_ZERO) {
            LogUtils.info(log, "请输入正确的手机号！", bizUser.getPhone());
            return RespBodyUtils.failure("Local please enter the correct mobile phone number ！");
        }

        if (StringUtils.isBlank(bizUser.getPassword())) {
            LogUtils.info(log, "密码不能为空！", bizUser.getPhone());
            return RespBodyUtils.failure("The password cannot be empty！");
        }

        //查询redis里面的OTP，校验通过，则注册成功
        String appChPasswordKey = String.format(FrameConstant.appChPasswordKey, bizUser.getPhone());

        String registerOTPValue = redisClientBean.get(appChPasswordKey);
        String registerOTP = "";
        if (StringUtils.isNotBlank(registerOTPValue)) {
            registerOTP = registerOTPValue.substring(0, 6);
        }

        /*if (!Objects.equals(registerOTP, bizUser.getOTP())) {
            LogUtils.info(log, "OTP错误，请重新输入", bizUser.getPhone());
            return RespBodyUtils.failure("OTP error, please re-enter！");
        }*/
        redisClientBean.remove(appChPasswordKey);
        //验证正确之后修改密码
        bizUserMapper.updateUserInfo(bizUser);
        LogUtils.info(log, "修改成功，请重新登录！", bizUser.getPhone());
        return RespBodyUtils.success("Modified successfully, please log in again!");
    }

    /**
     * 根据用户id查询用户详情
     *
     * @param bizUser
     * @return
     */
    public RespBody<UserVo> queryUserDetail(BizUser bizUser, HttpServletRequest request) {
        if (bizUser.getId() == NumberUtil.NUMBER_ZERO) {
            LogUtils.info(log, "用户id不能为空！");
            return RespBodyUtils.failure("The user id cannot be empty！");
        }

        UserVo userVO = new UserVo();
        CommonPool.submit(() -> {
            try {
                // 修改用户最后一次登录时间,地址，ip
                String remoteAddrIp = FrameUtils.getRemoteAddr2(request);
//                LogUtils.info(log, "查询玩家详情IP", userVO.getPhone(), remoteAddrIp);

                String city;
                if (remoteAddrIp.matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$")) {
                    // ipv4
                    BizIpRegion bizIpRegion = bizIpRegionService.selectOneByIpNum(remoteAddrIp);
                    city = bizIpRegion.getCity();
//                    LogUtils.info(log, "查询玩家详情IPv4", userVO.getPhone(), remoteAddrIp, city);
                } else {
                    // ipv6
                    BizIpRegionIPv6 bizIpRegion = bizIpRegionIPv6Service.selectOneByIpNum(remoteAddrIp);
                    city = bizIpRegion.getCity();
//                    LogUtils.info(log, "查询玩家详情IPv6", userVO.getPhone(), remoteAddrIp, city);
                }

                bizUserMapper.updateUserLastLoginIP(BizUser.builder().id(userVO.getId())
                        .ip(remoteAddrIp).ipRegion(city).build());
            } catch (Exception e) {
                LogUtils.error(log, "查询玩家详情异步处理登录后信息维护错误", e, userVO.getPhone());
            }
        });
        return RespBodyUtils.success("Description Querying user details succeeded！", userVO);
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
            LogUtils.info(log, "用户id不能为空！");
            return RespBodyUtils.failure("The user id cannot be empty！");
        }
        //验证正确之后修改密码
        bizUserMapper.updateBasicInfo(bizUser);
        LogUtils.info(log, "修改用户基础信息成功！");
        return RespBodyUtils.success("Basic user information is modified successfully. Procedure！");
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
            return RespBodyUtils.success("Refresh successfully！");
        } catch (Exception e) {
            LogUtils.error(log, "refresh会话失败", e, bizUser.getId(), sessionId);
            return RespBodyUtils.failure("Refresh failure");
        }
    }

}
