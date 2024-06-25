package com.mars.cds.service;

import com.mars.cds.constant.FrameConstant;
import com.mars.cds.entity.BizUser;
import com.mars.cds.entity.BizWalletAddress;
import com.mars.cds.mapper.BizWalletAddressMapper;
import com.mars.cds.support.LogUtils;
import com.mars.cds.support.RespBody;
import com.mars.cds.support.RespBodyUtils;
import com.mars.cds.vo.WalletAddressVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BizWalletAddressService {

    static final Logger log = LoggerFactory.getLogger(BizWalletAddressService.class.getSimpleName());

    @Resource
    private BizWalletAddressMapper bizWalletAddressMapper;

    public RespBody<String> saveWalletAddress(BizUser user, BizWalletAddress walletAddress) {
        if (StringUtils.isEmpty(walletAddress.getAddress())) {
            LogUtils.info(log, "钱包地址不能为空");
            return RespBodyUtils.failure("Wallet address cannot be empty");
        }
        if (!walletAddress.getAddress().matches(FrameConstant.tronWalletRegx)) {
            LogUtils.info(log, "钱包地址格式不正确", walletAddress.getAddress());
            return RespBodyUtils.failure("The wallet address format is incorrect");
        }
        if (walletAddress.getNetworkId() <= 0) {
            LogUtils.info(log, "不支持的网络地址", walletAddress.getNetworkId());
            return RespBodyUtils.failure("Unsupported network address");
        }

        walletAddress.setUserId(user.getId());
        walletAddress.setAddress(walletAddress.getAddress());

        if (bizWalletAddressMapper.queryIsExistWallet(walletAddress.getAddress()) > 0) {
            LogUtils.info(log, "钱包地址已添加", walletAddress.getAddress());
            return RespBodyUtils.failure("Wallet address added");
        }

        bizWalletAddressMapper.saveWalletAddress(walletAddress);
        return RespBodyUtils.success("Saved successfully");
    }

    public RespBody<List<WalletAddressVo>> queryWalletAddressList(int userId) {
        List<WalletAddressVo> targetList = bizWalletAddressMapper.queryWalletAddressList(userId);
        return RespBodyUtils.success("Query successfully", targetList);
    }

    public RespBody<String> deleteWalletAddress(BizUser user, BizWalletAddress walletAddress) {
        if (walletAddress.getId() <= 0) {
            LogUtils.info(log, "参数错误", walletAddress.getAddress());
            return RespBodyUtils.failure("Parameter error");
        }
        walletAddress.setUserId(user.getId());
        bizWalletAddressMapper.deleteWalletAddress(walletAddress);
        return RespBodyUtils.success("Deleted successfully");
    }
}
