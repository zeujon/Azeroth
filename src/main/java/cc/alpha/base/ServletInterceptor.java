package com.velcro.base;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class ServletInterceptor extends HandlerInterceptorAdapter {

	private static final ThreadLocal<HttpServletRequest> REQUEST_HOLDER = new ThreadLocal<>();
	private static final ThreadLocal<HttpServletResponse> RESPONSE_HOLDER = new ThreadLocal<>();
	private static final ThreadLocal<HttpSession> SESSION_HOLDER = new ThreadLocal<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		REQUEST_HOLDER.set(request);
		RESPONSE_HOLDER.set(response);
		SESSION_HOLDER.set(request.getSession(true));
		return true;
	}

	public static void init(HttpServletRequest request, HttpServletResponse response){
		REQUEST_HOLDER.set(request);
		RESPONSE_HOLDER.set(response);
		SESSION_HOLDER.set(request.getSession(true));
	}


	public static HttpServletRequest getRequest() {
		return REQUEST_HOLDER.get();
	}
	
	public static HttpServletResponse getResponse() {
		return RESPONSE_HOLDER.get();
	}
	
	public static HttpSession getSession() {
		return SESSION_HOLDER.get();
	}

}
