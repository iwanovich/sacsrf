package com.flameling.SACSRF;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.maven.archiva.web.action.admin.appearance.EditOrganisationInfoAction;

import com.flameling.SACSRF.TokenProcessor.LruCache;

public aspect CSRFPrevention {
	
	private TokenProcessor tokenProcessor = new TokenProcessor();
	
	//private HttpSession session;
	private ThreadLocal<HttpSession> tlSession;
	
	pointcut servletClass() : within(HttpServlet);
	pointcut serviceMethod(HttpServletRequest request,
							HttpServletResponse response, HttpServlet httpServlet):
				execution(protected void HttpServlet+.service(HttpServletRequest, HttpServletResponse)
						throws IOException, ServletException)
						&& args(request, response)
						&& this(httpServlet);
	pointcut doFilterMethod(ServletRequest request, ServletResponse response, FilterChain chain):
				execution(void Filter+.doFilter(ServletRequest, ServletResponse, FilterChain))
						&& args(request, response, chain);
	pointcut sendRedirect(String location, ServletResponse response):
				execution(void HttpServletResponse+.sendRedirect(String))
						&& args(location)
						&& this(response);
	pointcut forward(ServletRequest request, ServletResponse response):
				execution(void RequestDispatcher+.forward(ServletRequest, ServletResponse))
						&& args(request, response);
	
//	before(HttpServletRequest request, HttpServletResponse response, HttpServlet httpServlet) throws IOException:
//		serviceMethod(request, response, httpServlet) {
//			//System.out.println("Ik zit before de service method");
//			ServletContext sc = httpServlet.getServletContext();
//			register.register(request, sc);
//	}
	
	void around(HttpServletRequest request, HttpServletResponse response, HttpServlet httpServlet) throws IOException:
		serviceMethod(request, response, httpServlet) {
		createSessionRef(request);
		System.out.println("boe");
		if(request instanceof HttpServletRequest && response instanceof HttpServletResponse){
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			if (httpRequest.getAttribute(Constants.REGISTER_TOKEN) == null) {
				try {
					httpResponse = tokenProcessor.blockInvalidRequest(httpRequest, httpResponse);
				} catch (NoValidTokenException e) {
					httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
					return;
				}
				httpRequest.setAttribute(Constants.REGISTER_TOKEN,
						Constants.REGISTER_TOKEN);
			}
			proceed(httpRequest, httpResponse, httpServlet);
		} else {
			proceed(request, response, httpServlet);
		}
		if(request instanceof HttpServletRequest){
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String query = httpRequest.getQueryString() == null ? "" : httpRequest.getQueryString();
			System.out.println("Ik zit einde-around de doService method tijdens: " + httpRequest.getRequestURI() + "?" + query);
		}
	}
	
	void around(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException:
		doFilterMethod(request, response, chain) {
		System.out.println("boe");
		if(request instanceof HttpServletRequest && response instanceof HttpServletResponse){
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			createSessionRef(httpRequest);
			if (httpRequest.getAttribute(Constants.REGISTER_TOKEN) == null) {
				try {
					httpResponse = tokenProcessor.blockInvalidRequest(httpRequest, httpResponse);
				} catch (NoValidTokenException e) {
					httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
					return;
				}
				httpRequest.setAttribute(Constants.REGISTER_TOKEN,
						Constants.REGISTER_TOKEN);
			}
			proceed(httpRequest, httpResponse, chain);
		} else {
			proceed(request, response, chain);
		}
		if(request instanceof HttpServletRequest){
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String query = httpRequest.getQueryString() == null ? "" : httpRequest.getQueryString();
			System.out.println("Ik zit einde-around de doFilter method tijdens: " + httpRequest.getRequestURI() + "?" + query);
		}
	}

	
//	before(ServletRequest request, ServletResponse response, FilterChain chain):
//		doFilterMethod(request, response, chain) {
//			if(request instanceof HttpServletRequest){
//				HttpServletRequest httpRequest = (HttpServletRequest) request;
//				String query = httpRequest.getQueryString() == null ? "" : httpRequest.getQueryString();
//				System.out.println("Ik zit before de doFilter method tijdens: " + httpRequest.getRequestURI() + "?" + query);
//			}
//	}
	
//	after(String location, ServletResponse response):
//		sendRedirect(location, response){
//			System.out.println("Redirect happened towards: " + location + ". in " + response.toString());
//	}
	
	void around(String location, ServletResponse response):
		sendRedirect(location, response){
			if (!location.contains(Constants.CSRF_NONCE_REQUEST_PARAM)) {
				HttpSession session = tlSession.get();
				@SuppressWarnings("unchecked")
				LruCache<String> nonceCache = (LruCache<String>) session
						.getAttribute(Constants.CSRF_NONCE_SESSION_ATTR_NAME);
				if (nonceCache == null) {
					nonceCache = new LruCache<String>(TokenProcessor.nonceCacheSize);
				}
				session.setAttribute(Constants.CSRF_NONCE_SESSION_ATTR_NAME,
						nonceCache);
				String newNonce = tokenProcessor.generateNonce();
				nonceCache.add(newNonce);
				location = TokenProcessor.CsrfResponseWrapper.addNonce(
						location, newNonce);
			}
			proceed(location, response);
	}
	
//	before(ServletRequest request, ServletResponse response):
//		forward(request, response){
//		String target = "unknown";
//		if(request instanceof HttpServletRequest){
//			HttpServletRequest httpRequest = (HttpServletRequest) request;
//			target = httpRequest.getRequestURI();
//		}
//			System.out.println("Forward happened from/to: " + target);
//	}
	
	void around(ServletRequest request, ServletResponse response) throws IOException:
		forward(request, response) {
		System.out.println("boe");
		if(request instanceof HttpServletRequest && response instanceof HttpServletResponse){
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			createSessionRef(httpRequest);
			if (httpRequest.getAttribute(Constants.REGISTER_TOKEN) == null) {
				try {
					httpResponse = tokenProcessor.blockInvalidRequest(httpRequest, httpResponse);
				} catch (NoValidTokenException e) {
					httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
					return;
				}
				httpRequest.setAttribute(Constants.REGISTER_TOKEN,
						Constants.REGISTER_TOKEN);
			}
			proceed(httpRequest, httpResponse);
		} else {
			proceed(request, response);
		}
	}
	
	void createSessionRef(HttpServletRequest request){
		final HttpSession session = request.getSession();
		tlSession = new ThreadLocal<HttpSession>(){
			@Override
			protected HttpSession initialValue(){
				return session;
			}
		};
	}
	
	pointcut myClass(): within(EditOrganisationInfoAction);
    
    /**
     * The methods of those classes.
     */
    pointcut myMethod(): myClass() && execution(* *(..));

    /**
     * Prints trace messages before and after executing methods.
     */
    before (): myMethod() {
    	System.out.println("Before method (CSRFPrevention).");
    }
    after(): myMethod() {
        System.out.println("After method (CSRFPrevention).");
    }

}
