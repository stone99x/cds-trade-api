package com.mars.cds.service;

import com.mars.cds.constant.FrameConstant;
import com.mars.cds.entity.BizBankCard;
import com.mars.cds.entity.BizUser;
import com.mars.cds.mapper.BizBankCardMapper;
import com.mars.cds.support.LogUtils;
import com.mars.cds.support.RespBody;
import com.mars.cds.support.RespBodyUtils;
import com.mars.cds.vo.BankCardVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BizBankCardService {

    static final Logger log = LoggerFactory.getLogger(BizBankCardService.class.getSimpleName());

    @Resource
    private BizBankCardMapper bizBankCardMapper;

    public RespBody<String> saveBankCard(BizUser user, BankCardVo bankCardVo) {
        int userId = user.getId();
        if (StringUtils.isEmpty(bankCardVo.getPayee())) {
            LogUtils.info(log, "银行卡Payee不能为空", userId);
            return RespBodyUtils.failure("Payee cannot be empty");
        }
        if (StringUtils.isEmpty(bankCardVo.getBankIfsc())) {
            LogUtils.info(log, "银行卡ifsc不能为空", userId);
            return RespBodyUtils.failure("IFSC cannot be empty");
        }
        if (!bankCardVo.getBankIfsc().matches(FrameConstant.ifscRegx)) {
            LogUtils.info(log, "银行卡ifsc不能为空", userId, bankCardVo.getBankIfsc());
            return RespBodyUtils.failure("Incorrect IFSC format");
        }
        if (StringUtils.isEmpty(bankCardVo.getCardNumber())) {
            LogUtils.info(log, "银行卡号不能为空", userId);
            return RespBodyUtils.failure("Bank card number cannot be empty");
        }
        if (!bankCardVo.getCardNumber().matches(FrameConstant.bankCardRegx)) {
            LogUtils.info(log, "银行卡号格式不正确", userId, bankCardVo.getCardNumber());
            return RespBodyUtils.failure("The bank card number format is incorrect");
        }

        bankCardVo.setUserId(userId);
        bankCardVo.setPayee(bankCardVo.getPayee().trim());
        bankCardVo.setBankIfsc(bankCardVo.getBankIfsc().trim());
        bankCardVo.setCardNumber(bankCardVo.getCardNumber().trim());

        if (bizBankCardMapper.queryExistBankIfsc(bankCardVo) > 0) {
            LogUtils.info(log, "银行ifsc已添加", userId, bankCardVo.getBankIfsc());
            return RespBodyUtils.failure("Bank ifsc has been added");
        }

        if (bizBankCardMapper.queryExistCardNumber(bankCardVo) > 0) {
            LogUtils.info(log, "银行卡号已添加", userId, bankCardVo.getCardNumber());
            return RespBodyUtils.failure("Bank card number has been added");
        }
        bizBankCardMapper.saveBankCard(bankCardVo);
        return RespBodyUtils.success("Saved successfully");
    }

    public RespBody<BankCardVo> queryBankCardDefault(int userId) {
        BankCardVo bankCardVo = bizBankCardMapper.queryBankCardDefault(userId);
        return RespBodyUtils.success("Query successfully", bankCardVo);
    }

    public RespBody<String> updateBankCardDefault(BizUser user, BizBankCard bankCard) {
        int userId = user.getId();
        if (bankCard.getId() <= 0) {
            LogUtils.info(log, "设置默认银行卡参数错误", userId, bankCard.getId());
            return RespBodyUtils.failure("Parameter error");
        }
        bankCard.setUserId(userId);
        bizBankCardMapper.updateBankCardDefault(bankCard);
        return RespBodyUtils.success("Saved successfully");
    }

    public RespBody<List<BankCardVo>> queryBankCardList(int userId) {
        List<BankCardVo> targetList = bizBankCardMapper.queryBankCardList(userId);
        return RespBodyUtils.success("Query successfully", targetList);
    }

    public RespBody<String> deleteBackCard(BizUser user, BizBankCard bankCard) {
        int userId = user.getId();
        if (bankCard.getId() <= 0) {
            LogUtils.info(log, "删除银行卡参数错误", userId, bankCard.getId());
            return RespBodyUtils.failure("Parameter error");
        }
        bankCard.setUserId(userId);
        bizBankCardMapper.deleteBackCard(bankCard);
        return RespBodyUtils.success("Saved successfully");
    }
}
