package com.mars.cds.filter;

import com.alibaba.fastjson.JSON;
import com.mars.cds.cache.RedisClientBean;
import com.mars.cds.entity.BizUser;
import com.mars.cds.constant.FrameConstant;
import com.mars.cds.support.FrameUtils;
import com.mars.cds.support.LogUtils;
import com.mars.cds.support.RespBodyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 配置平台过滤器
 */
@WebFilter(filterName = "PojoAccessFilter", urlPatterns = {"/api/*"})
public class PojoAccessFilter extends BaseAccessFilter {

    private static final Logger log = LoggerFactory.getLogger(PojoAccessFilter.class.getSimpleName());

    // 不需要登录验证的URL
    private static String[] notNeedValidationUrlsPrefix = new String[]{
            "/api/user/getRegisterOtp",
            "/api/user/register",
            "/api/user/login",
            "/api/hotUpgrade/queryHotUpgrade",
    };

    @Resource
    private RedisClientBean redisClientBean;

    @Override
    protected void doFilterInternal(HttpServletRequest rawRequest, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 不能删除此行代码
        super.doFilterInternal(rawRequest, response, filterChain);
        XssHttpServletRequestWraper request = new XssHttpServletRequestWraper(rawRequest);

        // ##权限验证##
        String requestURI = request.getRequestURI();// 请求的URI(形如:/xxx/sys/user/list.
        // 是否需要验证登录(因部分请求不要验证)
        boolean isNeedValidationUrl = true;
        for (String notNeedUrl : notNeedValidationUrlsPrefix) {
            if (requestURI.endsWith(notNeedUrl)) {
                isNeedValidationUrl = false;
                break;
            }
        }
        if (isNeedValidationUrl) {// 需要权限登录
            String sessionId = request.getHeader(FrameConstant.sessionIdKey);
            String appSidKey = String.format(FrameConstant.appSidKey, sessionId);
            String userStr = redisClientBean.get(appSidKey);
            if (StringUtils.isEmpty(userStr)) {// 未登录
                LogUtils.warn(log, "当前sessionId未登录", sessionId, requestURI);
                // 11表示，未登录或登录过期使用此code
                write(FrameUtils.toJSONString(RespBodyUtils.failure(11,
                        "Not logged in, no permission to operate")), response);
                return;
            }
            BizUser bizUser = JSON.parseObject(userStr, BizUser.class);
            request.setAttribute(FrameConstant.userKey, bizUser);
        }
        filterChain.doFilter(request, response);
    }
}
