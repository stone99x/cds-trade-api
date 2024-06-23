package com.mars.cds.mapper;

import com.mars.cds.entity.BizBankCard;
import com.mars.cds.vo.BankCardVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BizBankCardMapper {

    void saveBankCard(BankCardVo bankCard);

    int queryExistBankIfsc(BankCardVo bankCard);

    int queryExistCardNumber(BankCardVo bankCard);

    BankCardVo queryBankCardDefault(int userId);

    void updateBankCardDefault(BizBankCard bankCard);

    List<BankCardVo> queryBankCardList(int userId);

    void updateBankCard(BizBankCard bankCard);

    void deleteBackCard(BizBankCard bankCard);

}
