package com.mars.cds.mapper;

import com.mars.cds.entity.BizUser;
import com.mars.cds.entity.BizUserLoginLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

    BizUser queryUserDetailByIdLock(int userId);

    /**
     * 根据手机号重置用户密码
     */
    void updatePasswordByPhone(BizUser bizUser);

    void updateUserLastLoginInfo(BizUser bizUser);

    /**
     * 修改用户在线状态为：离线
     */
    void updateOfflineStatus(int id);

    /**
     * 设置修改基础信息
     */
    void updateBasicInfo(BizUser bizUser);

    /**
     * 新增登录日日志
     */
    void insertUserLoginLog(BizUserLoginLog bizUserLoginLog);

    void updateBalanceById(BizUser user);
}
