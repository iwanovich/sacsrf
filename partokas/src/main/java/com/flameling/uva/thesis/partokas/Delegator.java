package com.flameling.uva.thesis.partokas;

import java.io.IOException;

import javax.servlet.http.*;
import javax.servlet.*;

import com.flameling.uva.thesis.partokas.http.MutableHttpResponse;

@SuppressWarnings("restriction")
public class Delegator {
	
	void serviceMethod(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet){
		// Nothing
	}
	
	
	ServletResponse beforeFilterMethod(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ProceedException, NothingToDoException, IOException {
		//System.out.println("Ik ben een Filter-Simon");
		if(request instanceof HttpServletRequest && response instanceof HttpServletResponse){
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			if (httpRequest.getAttribute(Constants.REGISTER_TOKEN) == null) {
				
				//Do something here
				try {
					response = doProcess(httpRequest, httpResponse);
					httpRequest.setAttribute(Constants.REGISTER_TOKEN,
								Constants.REGISTER_TOKEN);
				} catch (NoValidTokenException e) {
					if(!httpResponse.isCommitted())
						httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
					throw new ProceedException();
				}
			} else {
				throw new NothingToDoException("Token already added.");
			}
		} else {
			throw new NothingToDoException("Not a HTTP-request.");
		}
		return response;
	}
	
	void afterFilterMethod(ServletResponse response) throws IOException{
		if (response instanceof MutableHttpResponse){
			MutableHttpResponse mResponse = (MutableHttpResponse) response;
			HtmlTokenInjector hti = new HtmlTokenInjector();
			byte[] newBytes = hti.injectToken(mResponse.getContent(),
					mResponse.getContentType(), mResponse.getTokenValue(), mResponse.getCachedServerName());
			mResponse.setContent(newBytes);
			mResponse.writeContent();
		}
	}
	
	ServletResponse beforeRedirectMethod(String location, ServletResponse response)
			throws ProceedException, NothingToDoException, IOException {
		return null;
	}	
	
	private HttpServletResponse doProcess(HttpServletRequest request, HttpServletResponse response)
			throws IOException, NoValidTokenException{
		RequestProcessor rp = new RequestProcessor();
		HttpServletResponse newResponse = rp.process(request, response);
		return newResponse;
	}

}
