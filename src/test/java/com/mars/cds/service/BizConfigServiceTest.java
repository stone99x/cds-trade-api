package com.mars.cds.service;

import com.mars.cds.entity.BizConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BizConfigServiceTest {

    private BizConfigService bizConfigService;

    @Test
    public void selectConfig() {
        String name = "";
        BizConfig config = bizConfigService.selectConfig(name);
        System.out.println(config);
    }

}
