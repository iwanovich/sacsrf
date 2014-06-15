package com.flameling.uva.thesis.validator.parser;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;

import com.flameling.uva.thesis.validator.Constants;

public class SrcVisitor implements NodeVisitor {
	
	private static final String csrfTokenKey = Constants.TOKEN_KEY;
	private static final String queryPathDelimiter = "?";
	private static final String queryPathParameterDelimiter = "&";

	public SrcVisitor() {
		// TODO Auto-generated constructor stub
	}

	public void head(Node node, int depth) {
		alterAttr(node, "src");
		alterAttr(node, "href");
		alterAttr(node, "action");
	}

	public void tail(Node node, int depth) {
		// TODO Auto-generated method stub

	}
	
	private void alterAttr(Node node, String attr){
		String srcValue = node.attr(attr);
		if (!srcValue.isEmpty() && srcValue.contains(csrfTokenKey)) {
			node.attr(attr, stripToken(srcValue));
		}
	}
	
	private String stripToken(String url){
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
