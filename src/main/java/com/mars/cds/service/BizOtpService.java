package com.mars.cds.service;

import com.mars.cds.support.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BizOtpService {

    static final Logger log = LoggerFactory.getLogger(BizOtpService.class.getSimpleName());

    public boolean sendOtp(String phone, String content) {
        try {

            return true;
        } catch (Exception e) {
            LogUtils.error(log, "发送otp错误", e, phone, content);
            return false;
        }
    }

}
