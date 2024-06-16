package com.mars.cds.support;

import org.slf4j.Logger;

/**
 *         输出传入参数
 */
public class LogUtils {
    /**
     *
     * @param e 异常类
     * @return 组合异常日志输出
     */
    public static String stackTrace(Throwable e) {
        if (null == e)
            return "Throwable is empty.";
        final String L = "\n";
        StringBuffer buffer = new StringBuffer(L);
        buffer.append(e).append(L);
        StackTraceElement[] trace = e.getStackTrace();
        for (int i = 0; i < trace.length; i++)
            buffer.append("\tat " + trace[i]).append(L);
        return buffer.toString();
    }

    public static void info(Logger LOG, String tagName, Object... params) {
        if (LOG.isInfoEnabled()) {
            StringBuffer paramsBuffer = new StringBuffer(tagName);
            if (params.length > 0) {
                paramsBuffer.append(" ").append("params : ");
                for (Object paramValue : params) {
                    paramsBuffer.append(paramValue).append("|");
                }
            }
            LOG.info("{}", paramsBuffer);
        }
    }

    public static void debug(Logger LOG, String tagName, Object... params) {
        if (LOG.isDebugEnabled()) {
            StringBuffer paramsBuffer = new StringBuffer(tagName);
            if (params.length > 0) {
                paramsBuffer.append(" ").append("params : ");
                for (Object paramValue : params) {
                    paramsBuffer.append(paramValue).append("|");
                }
            }
            LOG.debug("{}", paramsBuffer);
        }
    }

    public static void warn(Logger LOG, String tagName, Object... params) {
        if (LOG.isWarnEnabled()) {
            StringBuffer paramsBuffer = new StringBuffer(tagName);
            if (params.length > 0) {
                paramsBuffer.append(" ").append("params : ");
                for (Object paramValue : params) {
                    paramsBuffer.append(paramValue).append("|");
                }
            }
            LOG.warn("{}", paramsBuffer);
        }
    }

    public static void error(Logger LOG, String tagName, Object... params) {
        StringBuffer paramsBuffer = new StringBuffer(tagName);
        if (params.length > 0) {
            paramsBuffer.append(" ").append("params : ");
            for (Object paramValue : params) {
                paramsBuffer.append(paramValue).append("|");
            }
        }
        LOG.error("{}", paramsBuffer);
    }

    public static void error(Logger LOG, String tagName, Throwable e, Object... params) {
        StringBuffer paramsBuffer = new StringBuffer(tagName);
        if (params.length > 0) {
            paramsBuffer.append(" ").append("params : ");
            for (Object paramValue : params) {
                paramsBuffer.append(paramValue).append("|");
            }
        }
        paramsBuffer.append(stackTrace(e));
        LOG.error("{}", paramsBuffer);
    }
}
