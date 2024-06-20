package com.mars.cds.mapper;

import com.mars.cds.entity.BizUser;
import com.mars.cds.entity.BizUserLoginLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface BizUserMapper {

    /**
     * 根据电话号码查询用户是否存在
     */
    int queryExistBizUser(@Param("phone") String phone);

    /**
     * 查选邀请码id是否存在
     */
    BizUser queryExistInviteId(@Param("inviteId") String inviteId);

    /**
     * 保存用户信息：注册
     */
    void insertBizUser(BizUser bizUser);

    /**
     * 查询登录信息是否正确
     */
    BizUser queryLogin(@Param("phone") String phone,
                       @Param("password") String password);

    BizUser queryUserDetailById(int userId);

    /**
     * 根据手机号重置用户密码
     */
    void updatePasswordByPhone(BizUser bizUser);

    void updateUserLastLoginInfo(BizUser bizUser);

    /**
     * 修改用户在线状态
     */
    void updateOnlineStatus(Integer id);

    /**
     * 查询用户余额相关
     */
    BizUser queryBalanceInfo(int userId);

    /**
     * 设置修改基础信息
     */
    void updateBasicInfo(BizUser bizUser);

    /**
     * 签到送用户奖金
     *
     * @param build
     */
    void updateUserBalanceBySign(BizUser build);

    /**
     * 查询用户余额
     */
    Long queryUserBalanceForUpdate(Integer id);

    /**
     * 新增登录日日志
     */
    void insertUserLoginLog(BizUserLoginLog bizUserLoginLog);
}
