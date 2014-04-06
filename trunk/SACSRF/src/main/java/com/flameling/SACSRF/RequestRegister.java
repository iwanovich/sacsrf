package com.flameling.SACSRF;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class RequestRegister {
	
	private final String REGISTER_TOKEN = "registerToken";
	
	void register(ServletRequest request){
		if(request.getAttribute(REGISTER_TOKEN) == null){
			String target = null;
			if(request instanceof HttpServletRequest){
				HttpServletRequest httpRequest = (HttpServletRequest) request;
				target = httpRequest.getRequestURI();
			}
			System.out.println("new registration for " + target + "!");
			request.setAttribute(REGISTER_TOKEN, REGISTER_TOKEN);
		}
	}
	
	void register(ServletRequest request, ServletContext sc){
		register(request);
	}

}
