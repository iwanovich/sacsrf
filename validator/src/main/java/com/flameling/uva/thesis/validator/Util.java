package com.flameling.uva.thesis.validator;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class Util {
	
	private static final String csrfTokenKey = Constants.TOKEN_KEY;
	private static final String queryPathDelimiter = "?";
	private static final String queryPathParameterDelimiter = "&";
	
	public static String stripToken(String url){
		String result = url;
		String csrfToken = "";
		String queryPath = StringUtils.substringAfter(url, queryPathDelimiter);
		String[] splits = StringUtils.split(queryPath, queryPathParameterDelimiter);
		for(String part : splits){
			if(part.contains(csrfTokenKey)){
				csrfToken = part;
			}
		}
		if(!csrfToken.isEmpty()){
			splits = ArrayUtils.remove(splits, ArrayUtils.indexOf(splits, csrfToken));
		}
		String urlBase = StringUtils.substringBefore(url, queryPathDelimiter);
		String newQueryPath = StringUtils.join(splits, queryPathParameterDelimiter);
		if(newQueryPath.isEmpty()){
			result = urlBase;
		} else{
			result = urlBase + queryPathDelimiter + newQueryPath;
		}
		
		return result;
	}
	
}
