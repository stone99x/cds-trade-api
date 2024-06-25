package com.mars.cds.mapper;

import com.mars.cds.entity.BizWalletAddress;
import com.mars.cds.vo.WalletAddressVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BizWalletAddressMapper {
    int queryIsExistWallet(String address);

    void saveWalletAddress(BizWalletAddress walletAddress);

    List<WalletAddressVo> queryWalletAddressList(int userId);

    void deleteWalletAddress(BizWalletAddress walletAddress);
}
