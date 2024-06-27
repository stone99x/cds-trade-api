package com.mars.cds.mapper;

import com.mars.cds.entity.BizWithdraw;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BizWithdrawMapper {
    
    int queryIsExistOrderNo(String orderNo);

    void saveWithdraw(BizWithdraw withdraw);

    int queryWithdrawListCount(BizWithdraw withdraw);

    List<BizWithdraw> queryWithdrawList(BizWithdraw withdraw);
}
