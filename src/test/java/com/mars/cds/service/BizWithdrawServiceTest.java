package com.mars.cds.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class BizWithdrawServiceTest {

    @Resource
    private BizWithdrawService bizWithdrawService;

    @Test
    public void getUniqueOrderNo() throws Exception {
        String orderNo = bizWithdrawService.getUniqueOrderNo();
        System.out.println(orderNo);
    }
}
