package com.mars.cds.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mars.cds.entity.BizAppHotUpgrade;
import com.mars.cds.mapper.BizAppHotUpgradeMapper;
import com.mars.cds.support.LogUtils;
import com.mars.cds.support.RespBody;
import com.mars.cds.support.RespBodyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BizAppHotUpgradeService {

    static final Logger log = LoggerFactory.getLogger(BizAppHotUpgradeService.class.getSimpleName());

    @Resource
    private BizAppHotUpgradeMapper bizAppHotUpgradeMapper;


    public RespBody<JSONObject> queryHotUpgrade() {

        BizAppHotUpgrade bizAppHotUpgrade = bizAppHotUpgradeMapper.selectOne();

        if (bizAppHotUpgrade == null) {
            LogUtils.info(log, "系统未添加热更版本");
            return RespBodyUtils.failure("System error");
        }

        JSONObject respBody = new JSONObject();

        respBody.put("version", bizAppHotUpgrade.getVersion());
        respBody.put("download", bizAppHotUpgrade.getDownload());
        if (StringUtils.isEmpty(bizAppHotUpgrade.getNewDownload())) {
            respBody.put("new_download", new JSONArray());
        } else {
            String[] newDownloadArr = bizAppHotUpgrade.getNewDownload().split("\n");
            respBody.put("new_download", newDownloadArr);
        }
        respBody.put("size", bizAppHotUpgrade.getSize());

        JSONObject apkJo = new JSONObject();
        apkJo.put("min_hotupdate_ver", bizAppHotUpgrade.getApkMinHotupdateVer());
        apkJo.put("install", bizAppHotUpgrade.getApkInstall() == null ? "" : bizAppHotUpgrade.getApkInstall());
        if (StringUtils.isEmpty(bizAppHotUpgrade.getApkNewInstall())) {
            apkJo.put("new_install", new JSONArray());
        } else {
            String[] newInstallArr = bizAppHotUpgrade.getApkNewInstall().split("\n");
            apkJo.put("new_install", newInstallArr);
        }
        respBody.put("android", apkJo);


        JSONObject iosJo = new JSONObject();
        iosJo.put("min_hotupdate_ver", bizAppHotUpgrade.getIosMinHotupdateVer());
        iosJo.put("install", bizAppHotUpgrade.getIosInstall() == null ? "" : bizAppHotUpgrade.getIosInstall());
        if (StringUtils.isEmpty(bizAppHotUpgrade.getIosNewInstall())) {
            iosJo.put("new_install", new JSONArray());
        } else {
            String[] newInstallArr = bizAppHotUpgrade.getIosNewInstall().split("\n");
            iosJo.put("new_install", newInstallArr);
        }
        respBody.put("iphone", iosJo);

        return RespBodyUtils.success("Successful", respBody);
    }

}
