package com.mars.cds.service;

import com.mars.cds.entity.BizUser;
import com.mars.cds.entity.BizWithdraw;
import com.mars.cds.mapper.BizUserMapper;
import com.mars.cds.mapper.BizWithdrawMapper;
import com.mars.cds.support.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeoutException;

@Service
public class BizWithdrawService {

    static final Logger log = LoggerFactory.getLogger(BizWithdrawService.class.getSimpleName());

    private static final long maxUniqueCheckTime = 9 * 1000L;

    @Resource
    private BizWithdrawMapper bizWithdrawMapper;
    @Resource
    private BizUserMapper bizUserMapper;

    public synchronized String getUniqueOrderNo() throws Exception {
        String orderNo;
        long overTimeMillis = (System.currentTimeMillis() + maxUniqueCheckTime);
        while (true) {
            orderNo = FrameUtils.getOrderNo();
            if (System.currentTimeMillis() > overTimeMillis) {
                throw new TimeoutException("生成唯一提现订单号超时");
            }
            if (bizWithdrawMapper.queryIsExistOrderNo(orderNo) > NumberUtil.NUMBER_ZERO) {
                continue;
            } else {
                return orderNo;
            }
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public RespBody<String> saveWithdraw(BizUser user, BizWithdraw withdraw) throws Exception {
        int userId = user.getId();
        if (withdraw.getCryptoId() <= NumberUtil.NUMBER_ZERO) {
            LogUtils.info(log, "提现时不支持的加密码币类型", userId, withdraw.getNetworkId());
            return RespBodyUtils.failure("Unsupported crypto currency");
        }
        if (withdraw.getNetworkId() <= NumberUtil.NUMBER_ZERO) {
            LogUtils.info(log, "提现时不支持的网络地址", userId, withdraw.getNetworkId());
            return RespBodyUtils.failure("Unsupported network address");
        }
        if (withdraw.getAmount() == null || withdraw.getAmount().compareTo(BigDecimal.ZERO) <= NumberUtil.NUMBER_ZERO) {
            LogUtils.info(log, "提现时金额值不正确", userId, withdraw.getAmount());
            return RespBodyUtils.failure("Incorrect amount value");
        }
        BizUser realUser = bizUserMapper.queryUserDetailByIdLock(userId);
        if (realUser.getStatus() != NumberUtil.NUMBER_ONE || realUser.getWithdrawStatus() != NumberUtil.NUMBER_ONE) {
            LogUtils.info(log, "提现时用户提现状态已关闭", userId, withdraw.getNetworkId());
            return RespBodyUtils.failure("Withdrawal disabled");
        }
        if (realUser.getBalance().compareTo(withdraw.getAmount()) == NumberUtil.NUMBER_ONE_NEGATE) {
            LogUtils.info(log, "提现时用户余额不足", userId, withdraw.getAmount(), realUser.getBalance());
            return RespBodyUtils.failure("Insufficient balance");
        }
        String orderNo = this.getUniqueOrderNo();

        withdraw.setUserId(userId);
        withdraw.setOrderNo(orderNo);
        // 保存提现记录
        bizWithdrawMapper.saveWithdraw(withdraw);

        // 扣减用户余额
        BigDecimal afterDecimal = realUser.getBalance().subtract(withdraw.getAmount());
        // negate函数，将正数转为负数
        BizUser balanceUser = BizUser.builder().id(userId).balance(withdraw.getAmount().negate()).build();
        bizUserMapper.updateBalanceById(balanceUser);

        LogUtils.info(log, "提现提交成功", userId, realUser.getBalance(),
                withdraw.getAmount(), afterDecimal);

        return RespBodyUtils.success("Success");
    }

}
