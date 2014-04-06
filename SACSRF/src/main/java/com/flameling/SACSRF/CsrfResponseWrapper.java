package com.flameling.SACSRF;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

class CsrfResponseWrapper extends HttpServletResponseWrapper {

	private String nonce;

	public CsrfResponseWrapper(HttpServletResponse response, String nonce) {
		super(response);
		this.nonce = nonce;
	}
	
	@Override
	@Deprecated
	public String encodeRedirectUrl(String url) {
		return encodeRedirectURL(url);
	}

	@Override
	public String encodeRedirectURL(String url) {
		return addNonce(super.encodeRedirectURL(url));
	}
	
	@Override
	@Deprecated
	public String encodeUrl(String url) {
		return encodeURL(url);
	}
	
	@Override
	public String encodeURL(String url) {
		return addNonce(super.encodeURL(url));
	}

	private String addNonce(String url){
		return addNonce(url, this.nonce);
	}

	/**
	 * Return the specified URL with the nonce added to the query string. 
	 *
	 * @param url URL to be modified
	 * @param nonce The nonce to add
	 */
	public String addNonce(String url, String nonce) {
		
		if ((url == null) || (nonce == null))
			return (url);
		
		String path = url;
		String query = "";
		String anchor = "";
		int pound = path.indexOf('#');
		if (pound >= 0) {
			anchor = path.substring(pound);
			path = path.substring(0, pound);
		}
		int question = path.indexOf('?');
		if (question >= 0) {
			query = path.substring(question);
			path = path.substring(0, question);
		}
		StringBuilder sb = new StringBuilder(path);
		if (query.length() >0) {
			sb.append(query);
			sb.append('&');
		} else {
			sb.append('?');
		}
		sb.append(Constants.CSRF_NONCE_REQUEST_PARAM);
		sb.append('=');
		sb.append(nonce);
		sb.append(anchor);
		return (sb.toString());
	}
}
