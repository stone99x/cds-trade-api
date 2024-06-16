package com.mars.cds.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssHttpServletRequestWraper extends HttpServletRequestWrapper {

	public XssHttpServletRequestWraper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		return clearXss(super.getParameter(name));
		// return xssEncode(super.getParameter(name));
	}

	@Override
	public String getHeader(String name) {
		return clearXss(super.getHeader(name));
		// return xssEncode(super.getParameter(name));
	}

	@Override
	public String[] getParameterValues(String name) {
		if (!StringUtils.isEmpty(name)) {
			String[] values = super.getParameterValues(name);
			if (values != null && values.length > 0) {
				String[] newValues = new String[values.length];

				for (int i = 0; i < values.length; i++) {
					newValues[i] = clearXss(values[i]);
					// newValues[i] = xssEncode(values[i]);
				}
				return newValues;
			}
		}
		return null;
	}

	/**
	 * 
	 * 处理字符转义【勿删，请保留该注释代码】
	 * 
	 * @param value
	 * @return
	 */
	public String clearXss(String value) {
		if (value == null || "".equals(value)) {
			return value;
		}
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		value = value.replaceAll("'", "&#39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		// value = value.replace("script", "");
		return value;
	}

	/**
	 * 将特殊字符替换为全角
	 * 
	 * @param s
	 * @return
	 */
	public String xssEncode(String s) {
		if (s == null || s.isEmpty()) {
			return s;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
				case '>':
					sb.append('＞');// 全角大于号
					break;
				case '<':
					sb.append('＜');// 全角小于号
					break;
				case '\'':
					sb.append('‘');// 全角单引号
					break;
				case '\"':
					sb.append('“');// 全角双引号
					break;
				case '&':
					sb.append('＆');// 全角＆
					break;
				case '\\':
					sb.append('＼');// 全角斜线
					break;
				case '/':
					sb.append('／');// 全角斜线
					break;
				// case '#':
				// sb.append('＃');// 全角井号
				// break;
				case '(':
					sb.append('（');// 全角(号
					break;
				case ')':
					sb.append('）');// 全角)号
					break;
				default:
					sb.append(c);
					break;
			}
		}
		return sb.toString();
	}
}
