package com.mars.cds.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * app版本更新
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BizAppHotUpgrade implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标识Id
     */
    private int id;
    /**
     * 当前服务器上的版本 manifest.json里面的版本号
     */
    private String version;
    /**
     * 必填 热更下载地址 string格式只能唯一
     */
    private String download;
    /**
     * 非必填 Array格式循环下载，挨着尝试（直到成功结束），尝试完成。当前此项存在的时候忽略download 属性
     */
    private String newDownload;
    /**
     * 当前安装包文件大小，用于对比当前下载的包是否完整
     */
    private int size;
    /**
     * 最小支持的热更版本，如果此选项和version选项相同值的情况下，会采取浏览器打开的方式下载。整包更新
     */
    private String apkMinHotupdateVer;
    /**
     * 必填 整包下载地址 string格式只能唯一
     */
    private String apkInstall;
    /**
     * 非必填 Array格式 随机选择一个下载地址打开浏览器下载。当前此项存在的时候忽略install 属性
     */
    private String apkNewInstall;
    /**
     * 最小支持的热更版本，如果此选项和version选项相同值的情况下，会采取浏览器打开的方式下载。整包更新
     */
    private String iosMinHotupdateVer;
    /**
     * 必填 整包下载地址 string格式只能唯一
     */
    private String iosInstall;
    /**
     * 非必填 Array格式 随机选择一个下载地址打开浏览器下载。当前此项存在的时候忽略install 属性
     */
    private String iosNewInstall;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 状态,1启用；2禁用
     */
    private int status;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;

}
