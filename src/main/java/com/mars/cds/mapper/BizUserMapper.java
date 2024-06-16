package com.mars.cds.mapper;

import com.mars.cds.entity.BizUser;
import com.mars.cds.entity.BizUserLoginLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface BizUserMapper {

    /**
     * 根据电话号码查询玩家是否存在
     *
     * @param phone
     * @return
     */
    int queryExistBizUser(@Param("phone") String phone);

    /**
     * 查选邀请码id是否存在
     *
     * @param inviteId
     * @return
     */
    BizUser queryExistInviteId(@Param("inviteId") String inviteId);

    /**
     * 保存玩家信息：注册
     *
     * @param bizUser
     */
    void insertBizUser(BizUser bizUser);

    /**
     * 查询登录信息是否正确
     *
     * @param phone
     * @param password
     */
    BizUser queryLogin(@Param("phone") String phone,
                       @Param("password") String password);

    /**
     * 修改玩家密码
     *
     * @param bizUser
     */
    void updateUserInfo(BizUser bizUser);

    /**
     * 修改玩家登录ip
     *
     * @param bizUser
     */
    void updateUserLastLoginTime(BizUser bizUser);

    void updateUserLastLoginIP(BizUser bizUser);

    /**
     * 修改用户最后一次登录时间和累计用户次数
     *
     * @param bizUser
     */
    void updateLoginCountAndLoginTime(BizUser bizUser);

    /**
     * 修改玩家在线状态
     *
     * @param id
     */
    void updateOnlineStatus(Integer id);

    /**
     * 根据邀请码查询往下三级的余额
     *
     * @return
     */
    Long queryTeamBalance(@Param("selfId") String selfId);

    /**
     * 修改当前用户的余额
     *
     * @param build
     */
    void updateBalance(BizUser build);

    /**
     * 根据userId查询余额
     *
     * @param userId
     * @return
     */
    BizUser queryBalanceAndSelfId(Integer userId);

    /**
     * 仅修改用户余额相关
     */
    void updateBalanceInfo(BizUser bizUser);

    BizUser queryUserDetailById(BizUser bizUser);

    /**
     * 查询用户余额相关
     */
    BizUser queryBalanceInfo(int userId);

    /**
     * 设置修改基础信息
     *
     * @param bizUser
     */
    void updateBasicInfo(BizUser bizUser);

    /**
     * 根据邀请码id查询当前用户的上级用户信息
     *
     * @param inviteId
     * @return
     */
    BizUser queryBalanceBySelfId(String inviteId);

    /**
     * 修改层级Number
     *
     * @param bizUser1
     */
    void updateTeamLevelNumber(BizUser bizUser1);

    void updateTeamNumber(BizUser bizUser1);

    /**
     * 根据邀请id查询父级备注
     *
     * @param fatherInviteId
     * @return
     */
    Map<String, String> queryFatherUserInfo(String fatherInviteId);

    void updateTeamAmountInfo(BizUser bizUser);

    /**
     * 维护团队余额：充值
     *
     * @param bizUser
     */
    void updateTeamRecharge(BizUser bizUser);

    /**
     * 维护团队余额：提现
     *
     * @param bizUser
     */
    void updateTeamWithdraw(BizUser bizUser);

    /**
     * 修改 发送FB状态，首充状态是不已发送，1是；2否',
     *
     * @param bizUser
     * @return
     */
    void updateRechargeSendStatus(BizUser bizUser);

    /**
     * 签到送用户奖金
     *
     * @param build
     */
    void updateUserBalanceBySign(BizUser build);

    /**
     * 查询用户余额
     *
     * @param id
     * @return
     */
    Long queryUserBalanceForUpdate(Integer id);

    /**
     * 新增登录日日志
     *
     * @param bizUserLoginLog
     */
    void insertUserLoginLog(BizUserLoginLog bizUserLoginLog);


    Integer queryUserRemarkId(String selfId);
}
