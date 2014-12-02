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
			throws NothingToDoException, IOException {
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
					//httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
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
			String source = new String(mResponse.getContent());
			//System.out.println(source);
			HtmlTokenInjector hti = new HtmlTokenInjector();
			//String newSource = hti.injectToken(source, "HEY-IK-BEN-IWAN");
			//System.out.print("\n\n-------------------------\n\n");
			System.out.println(mResponse.getContent());
			mResponse.setContent(mResponse.getContent());
			System.out.println(response.getCharacterEncoding());
			System.out.println(response.getContentType());
			mResponse.writeContent();
		}
	}
	
	private HttpServletResponse doProcess(HttpServletRequest request, HttpServletResponse response)
			throws IOException, NoValidTokenException{
		MutableHttpResponse mResponse = new MutableHttpResponse(response);
		RequestProcessor rp = new RequestProcessor();
		System.out.println(new String(mResponse.getContent()));
		//rp.process(request, response);
		return mResponse;
	}

}
