package com.mars.cds.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseAccessFilter extends OncePerRequestFilter {

    @Value("${pojo.filter.headers:}")
    private String addHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        /*if (!StringUtils.isEmpty(addHeader)) {
            Map<String, String> addHeaders = JSONObject.parseObject(addHeader, Map.class);
            for (String headerKey : addHeaders.keySet()) {
                response.addHeader(headerKey, addHeaders.get(headerKey));
            }
        }*/
        //设置允许跨域请求地址即为当前请求地址
        /*response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        if (request.getMethod().toUpperCase().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }*/
    }

    @Override
    protected void initFilterBean() throws ServletException {

    }

    protected void write(String msg, HttpServletResponse response) {
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
