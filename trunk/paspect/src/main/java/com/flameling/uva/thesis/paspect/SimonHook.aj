package com.flameling.uva.thesis.paspect;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import javax.servlet.*;

import org.javasimon.Split;

public aspect SimonHook {
	
	Delegator delegator = new Delegator();

	pointcut serviceMethod(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet) :
		execution(protected void HttpServlet+.service(HttpServletRequest, HttpServletResponse)
			throws IOException, ServletException)
			&& args(request, response)
			&& this(servlet);
	pointcut filterMethod(ServletRequest request, ServletResponse response, FilterChain chain) :
		execution(void Filter+.doFilter(ServletRequest, ServletResponse, FilterChain))
				&& args(request, response, chain);
	
	before(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet) :
		serviceMethod(request, response, servlet) {
		delegator.serviceMethod(request, response, servlet);
	}
	
//	before(ServletRequest request, ServletResponse response, FilterChain chain) :
//		filterMethod(request, response, chain) {
//		delegator.filterMethod(request, response, chain);
//	}
	
	void around(ServletRequest request, ServletResponse response, FilterChain chain) : filterMethod(request, response, chain) {
		try {
			Split split = delegator.beforeFilterMethod(request, response, chain);
			proceed(request, response, chain);
			delegator.afterFilterMethod(split);
		} catch (NothingToDoException e) {
			proceed(request, response, chain);
		}
	}
}
