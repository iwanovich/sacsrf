package com.flameling.uva.thesis.partokas;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.Filter;

@SuppressWarnings("restriction")
public aspect PartokasHook {
	
	private Delegator delegator = new Delegator();
	
	pointcut serviceMethod(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet) :
		execution(protected void HttpServlet+.service(HttpServletRequest, HttpServletResponse)
			throws IOException, ServletException)
			&& args(request, response)
			&& this(servlet);
	pointcut filterMethod(ServletRequest request, ServletResponse response, FilterChain chain) :
		execution(void Filter+.doFilter(ServletRequest, ServletResponse, FilterChain))
				&& args(request, response, chain);
	pointcut sendRedirect(String location, ServletResponse response):
		execution(void HttpServletResponse+.sendRedirect(String))
				&& args(location)
				&& this(response);
	pointcut forward(ServletRequest request, ServletResponse response):
		execution(void RequestDispatcher+.forward(ServletRequest, ServletResponse))
				&& args(request, response);
	
	before(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet) :
		serviceMethod(request, response, servlet) {
		delegator.serviceMethod(request, response, servlet);
	}
	
//	before(ServletRequest request, ServletResponse response, FilterChain chain) :
//		filterMethod(request, response, chain) {
//		delegator.filterMethod(request, response, chain);
//	}
	
	void around(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException
	: filterMethod(request, response, chain) {
		try {
			response = delegator.beforeFilterMethod(request, response, chain);
			proceed(request, response, chain);
			delegator.afterFilterMethod(response);
		} catch (NothingToDoException e) {
			proceed(request, response, chain);
		} catch (ProceedException e) {
			// Do not proceed.
		}
	}
	
	void around(String location, ServletResponse response):
		sendRedirect(location, response){
		delegator.beforeRedirectMethod(location, response);
		proceed(location, response);
	}
}
