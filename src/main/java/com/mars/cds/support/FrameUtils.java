package com.mars.cds.support;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mars.cds.constant.FrameConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrameUtils {

    private static final Logger log = LoggerFactory.getLogger(FrameUtils.class.getSimpleName());

    public static final char[] charsArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    // 字符匹配所有中文字符及英文字符
    public static final String normalTextRegex = "([\u4e00-\u9fa5A-Za-z0-9]+)";

    /**
     * 非空判断
     *
     * @return
     */
    public static boolean isEmpty(Map<?, ?> params) {
        return null == params || params.size() == 0;
    }

    /**
     * 非空判断
     *
     * @return
     */
    public static boolean isEmpty(List<?> list) {
        return null == list || list.size() == 0;
    }

    /**
     * 判断字符是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 将Object转换成String对象
     *
     * @param object
     * @return 字符串
     */
    public static String objectToString(Object object) {
        return object == null ? "" : object.toString();
    }

    /**
     * 判断传入参数是否为数字且大于0.<br>
     *
     * @param integer 数字
     * @return true:是,false:否
     */
    public static boolean isIntegerAndGt0(Integer integer) {
        if (null == integer || integer <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断数字是否在指定小数位(包含正负)
     *
     * @param number 数字
     * @param length 小数位数，大于1
     */
    public static boolean isNumberX(String number, int length) {
        //{0," + length + "}
        // 判断小数点后length位的数字的正则表达式
        Pattern pattern = Pattern.compile("^(([-1-9]{1}\\d*)|([0]{1}))(\\.(\\d){" + length + "})?$");
        Matcher match = pattern.matcher(number);
        return match.matches();
    }

    /**
     * 判断字符串是否为数字类型并且大于0.<br>
     *
     * @param number 要判断的字符串
     * @return true:表是为大于0的数字,false:表示小于0或不为数字
     */
    public static boolean isNumberAndGt0(String number) {
        if (!NumberUtils.isCreatable(number)) {
            return false;
        }
        try {
            Integer proNumber = Integer.valueOf(number);
            return proNumber > 0 ? true : false;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    /**
     * 判断字符串是否为数字类型并且大于等于0.<br>
     *
     * @param number 要判断的字符串
     * @return true:表是为大于等于0的数字,false:表示小于0或不为数字
     */
    public static boolean isNumberAndGet0(String number) {
        if (!NumberUtils.isCreatable(number)) {
            return false;
        }
        try {
            Integer proNumber = Integer.valueOf(number);
            return proNumber >= 0 ? true : false;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    /**
     * 生成指定长度的随机字符串
     *
     * @param randomStrLen 长度
     * @return
     */
    public static String getRandomStrLen(int randomStrLen) {
        StringBuffer buffer = new StringBuffer();
        try {
            Random random = new Random();
            for (int i = 0; i < randomStrLen; i++) {
                buffer.append(charsArray[random.nextInt(charsArray.length)]);
            }
        } catch (Exception e) {
            return "randomStrLen";
        }
        return buffer.toString();
    }

    /**
     * 生成指定长度的随机字符串
     *
     * @param randomStrLen 长度
     * @return
     */
    public static String getRandomIntLen(int randomStrLen) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < randomStrLen; i++) {
            buffer.append((int) (Math.random() * 10));
        }
        return buffer.toString();
    }

    /**
     * 生成UUID
     *
     * @return
     */
    public static String getUuidString() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成系统流水号
     */
    public static String getFlowNo(int no) {
        String date = FrameConstant.dateFormatter5.get().format(new Date());
        date = date.concat(FrameUtils.getRandomIntLen(2));
        return date.concat(String.valueOf(no));
    }

    /**
     * 获取请求的IP.<br>
     *
     * @return IP地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        /*String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;*/
        String ip = "", cip = "";
        try {
            cip = request.getHeader("x-forwarded-for");
            if (!StringUtils.isEmpty(cip)) {
                ip = cip.split(",")[0];
                if (ip.matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$")) {
                    return ip;
                }
            }
            ip = request.getHeader("X-Real-IP");
            if (!StringUtils.isEmpty(ip)) {
                return ip;
            }
            return request.getRemoteAddr();
        } catch (Exception e) {
            LogUtils.info(log, "解决IP错误", cip, ip);
            return request.getRemoteAddr();
        }
    }

    public static String getRemoteAddr2(HttpServletRequest request) {
        /*JSONObject param = new JSONObject();
        param.put("X-Real-IP", request.getHeader("X-Real-IP"));
        param.put("x-forwarded-for", request.getHeader("x-forwarded-for"));
        param.put("Proxy-Client-IP", request.getHeader("Proxy-Client-IP"));
        param.put("WL-Proxy-Client-IP", request.getHeader("WL-Proxy-Client-IP"));
        param.put("GetRemoteAddr", request.getRemoteAddr());*/
        String ip = "", cip = "";
        try {
            cip = request.getHeader("x-forwarded-for");
            if (!StringUtils.isEmpty(cip)) {
                ip = cip.split(",")[0];
                if (!StringUtils.isEmpty(ip) && ip.length() > 8) {
                    return ip;
                }
            }
            ip = request.getHeader("X-Real-IP");
            if (!StringUtils.isEmpty(ip)) {
                return ip;
            }
            return request.getRemoteAddr();
        } catch (Exception e) {
            LogUtils.info(log, "解决IP错误", cip, ip);
            return request.getRemoteAddr();
        }
    }

    public static String getDecimalDigit2(BigDecimal decimal) {
        if (decimal == null) {
            return FrameConstant.amountZero;
        }
        return decimal.setScale(FrameConstant.amountDigit2).toPlainString();
    }

    public static Date getDateTimeA(String date) throws Exception {
        return FrameConstant.dateFormatter2.get().parse(date);
    }

    public static Calendar getCalendarOnSecond(int second) throws Exception {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.SECOND, second);
        return currentCalendar;
    }

    public static Calendar getCalendarA(String date) throws Exception {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(FrameConstant.dateFormatter2.get().parse(date));
        return currentCalendar;
    }

    public static String getDateInterval(int date) {
        try {
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.add(Calendar.DAY_OF_MONTH, date);
            return FrameConstant.dateFormatter.get().format(currentCalendar.getTime());
        } catch (Exception e) {
            LogUtils.error(log, "getDateInterval error", e, date);
            return null;
        }
    }

    public static String getDateIntervalMonth(String date, int month) throws Exception {
        Calendar currentCalendar = getCalendarA(date);
        currentCalendar.add(Calendar.MONTH, month);
        return FrameConstant.dateFormatter2.get().format(currentCalendar.getTime());
    }

    public static String getDateIntervalHour(int hour) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.HOUR_OF_DAY, hour);
        return FrameConstant.dateFormatter2.get().format(currentCalendar.getTime());
    }

    public static String getDateIntervalMinute(int minute) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.MINUTE, minute);
        return FrameConstant.dateFormatter2.get().format(currentCalendar.getTime());
    }

    /**
     * 使用FastJSON将对象转JSON
     *
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
    }

    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (Exception e) {
            return null;
        }
    }

    public static String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, "utf-8");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 保留2位小数
     * 将元为单位的转换为分 （乘100）
     */
    public static String convertY2F(BigDecimal amount) {
        String plain = amount.multiply(new BigDecimal(100)).toPlainString();
        int pointPos = plain.indexOf(".");
        // amount，如果有小数点，toPlainString后也会带相应的小数点，所以这种情况要处理
        if (pointPos > 0) {
            return plain.substring(0, pointPos);
        }
        return plain;
    }

    /**
     * 保留2位小数
     * 将分为单位的转换为元 （除100）
     */
    public static BigDecimal convertF2Y(String amount) {
        BigDecimal amountBig = new BigDecimal(amount);
        return amountBig.divide(new BigDecimal(100)).setScale(FrameConstant.amountDigit2);
    }

    /**
     * 将“1,2,3”字符串转换成List
     */
    public static List<Integer> convertStr2ListInt(String numStr, String tips) throws Exception {
        String[] numArray = numStr.split(",");
        List<Integer> numList = new ArrayList<>(numArray.length);
        for (String one : numArray) {
            int oneNum = FrameUtils.parseInt(one, 0);
            if (oneNum <= 0) {
                throw new Exception(String.format("%s必须为大于0的数字", tips));
            }
            numList.add(oneNum);
        }
        return numList;
    }

    /**
     * 将“1,2,3”字符串转换成List
     */
    public static List<Integer> convertStr2ListIntGet0(String numStr, String tips) throws Exception {
        String[] numArray = numStr.split(",");
        List<Integer> numList = new ArrayList<>(numArray.length);
        for (String one : numArray) {
            int oneNum = FrameUtils.parseInt(one, 0);
            if (oneNum < 0) {
                throw new Exception(String.format("%s必须为大于等于0的数字", tips));
            }
            numList.add(oneNum);
        }
        return numList;
    }

    /**
     * 将yyyyMMddHHmmss格式日期转换成yyyy-MM-dd HH:mm:ss
     */
    public static String convertDateFormatter(String oldDateFormatter) {
        String newDateFormatter = "";
        char[] times = oldDateFormatter.toCharArray();
        for (int i = 0; i < times.length; i++) {
            if (i == 3 || i == 5) {
                newDateFormatter = newDateFormatter.concat(String.valueOf(times[i])).concat("-");
            } else if (i == 7) {
                newDateFormatter = newDateFormatter.concat(String.valueOf(times[i])).concat(" ");
            } else if (i == 9 || i == 11) {
                newDateFormatter = newDateFormatter.concat(String.valueOf(times[i])).concat(":");
            } else {
                newDateFormatter = newDateFormatter.concat(String.valueOf(times[i]));
            }
        }
        return newDateFormatter;
    }

    /**
     * 将字符串转成数字
     *
     * @param moRen 出错时默认数字
     */
    public static Integer parseInt(String number, int moRen) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            log.error("parseInt error, message = {}", e.getMessage());
            return moRen;
        }
    }

    /**
     * 将字符串转成数字（大于0）
     *
     * @param moRen 出错时默认数字
     */
    public static Integer parseIntGt0(String number, int moRen) {
        try {
            int num = Integer.parseInt(number);
            return num > 0 ? num : moRen;
        } catch (NumberFormatException e) {
            log.error("parseIntGt0 error, message = {}", e.getMessage());
            return moRen;
        }
    }

    /**
     * 将字符串转成数字
     *
     * @param moRen 出错时默认数字
     */
    public static Double parseDouble(String number, double moRen) {
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            log.error("parseDouble error, message = {}", e.getMessage());
            return moRen;
        }
    }

    /**
     * 将字符串转成数字
     *
     * @param moRen 出错时默认数字
     */
    public static BigDecimal parseBigDecimal(String number, BigDecimal moRen) {
        try {
            return new BigDecimal(number);
        } catch (NumberFormatException e) {
            log.error("parseBigDecimal error, message = {}", e.getMessage());
            return moRen;
        }
    }

    /**
     * 将特殊字符用*号代替
     */
    public static String getNewText(String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        }
        String buffer = "";
        Pattern pattern = Pattern.compile(normalTextRegex);
        for (char t : text.toCharArray()) {
            Matcher matcher = pattern.matcher(String.valueOf(t));
            if (matcher.find()) {
                buffer += matcher.group(1);
            } else {
                buffer += "*";
            }
        }
        return buffer;
    }

    /**
     * 输出json数据
     */
    public static void write(String msg, HttpServletResponse response) {
        try {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转JSONObject
     */
    public static JSONObject getJsonObject(String json) {
        if (json == null || json.length() <= 2) {
            return null;
        }
        try {
            return JSONObject.parseObject(json);
        } catch (Exception e) {
            LogUtils.error(log, "getJsonObject错误", json, e);
            return null;
        }
    }

    /**
     * 转JSONObject
     */
    public static <T> T getJsonObject(String json, Class<T> c) {
        try {
            if (json == null || json.length() <= 2) {
                return c.newInstance();
            }
            try {
                return JSONObject.parseObject(json, c);
            } catch (Exception e) {
                LogUtils.error(log, "getJsonObject格式错误", json, e);
                return c.newInstance();
            }
        } catch (Exception e) {
            LogUtils.error(log, "getJsonObject错误", json, e);
            return null;
        }
    }

    /**
     * 转JSONArray
     */
    public static JSONArray getJsonArray(String json) {
        if (json == null || json.length() <= 2) {
            return null;
        }
        try {
            return JSONArray.parseArray(json);
        } catch (Exception e) {
            LogUtils.error(log, "getJsonArray错误", json, e);
            return null;
        }
    }

    /**
     * 转JSONArray
     */
    public static <T> List<T> getJsonArray(String json, Class<T> t) {
        if (json == null || json.length() <= 2) {
            return new ArrayList<>(0);
        }
        try {
            return JSONArray.parseArray(json, t);
        } catch (Exception e) {
            LogUtils.error(log, "getJsonArray错误", json, e);
            return new ArrayList<>(0);
        }
    }

    public static String getOrderNo() {
        String timeSeq = DateUtil.date().toString(FrameConstant.dfs9);
        String randomStr = getRandomIntLen(5);
        return String.format("TC%s%s", timeSeq, randomStr);
    }
}
