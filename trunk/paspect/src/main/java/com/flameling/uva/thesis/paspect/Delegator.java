package com.flameling.uva.thesis.paspect;

import javax.servlet.http.*;
import javax.servlet.*;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

@SuppressWarnings("restriction")
public class Delegator {
	
	void serviceMethod(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet){
		System.out.println("Ik ben een Servlet-Simon");
	}
	
//	void filterMethod(ServletRequest request, ServletResponse response, FilterChain chain){
//		System.out.println("Ik ben een Filter-Simon");
//	}
	
	Split beforeFilterMethod(ServletRequest request, ServletResponse response, FilterChain chain) throws NothingToDoException {
		//System.out.println("Ik ben een Filter-Simon");
		if(request instanceof HttpServletRequest && response instanceof HttpServletResponse){
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			if (httpRequest.getAttribute(Constants.REGISTER_TIMER) == null) {
				
				if(!isStaticResourceRequest(httpRequest)){
					//Do something here
					Stopwatch stopwatch = SimonManager.getStopwatch("test");
					//stopwatch.reset();
					Split split = stopwatch.start();
					
					httpRequest.setAttribute(Constants.REGISTER_TIMER,
							Constants.REGISTER_TIMER);
					return split;
				}
				else{
					throw new NothingToDoException("Not a dynamic resource.");
				}
			} else {
				throw new NothingToDoException("Timer already started.");
			}
		} else {
			throw new NothingToDoException("Not a HTTP-request.");
		}
	}
	
	void afterFilterMethod(Split split){
		split.stop();
		System.out.println(split.toString());
		System.out.println(SimonManager.getStopwatch("test").toString());
	}
	
	private boolean isStaticResourceRequest(HttpServletRequest request){
		boolean result = false;
		String path = request.getServletPath();
        if (request.getPathInfo() != null) {
            path = path + request.getPathInfo();
        }
		if (path == null)
				return result;
		if(ResourceType.hasExtension(path)){
			result = true;
		}
		return result;
	}

}
