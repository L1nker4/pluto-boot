package com.github.pluto.boot.web.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;


/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/4 14:17
 * @description：
 */
public class HttpContextUtil {

	private HttpContextUtil(){

	}
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
	}
}
