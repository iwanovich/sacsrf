package com.flameling.uva.thesis.partokas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

import com.flameling.uva.thesis.partokas.http.header.contenttype.MediaType;
import com.flameling.uva.thesis.partokas.http.header.contenttype.MediaTypes;
import com.flameling.uva.thesis.partokas.http.header.contenttype.GroupType;

public class HtmlTokenInjector {
	
	public MediaTypes imageTypes;
	public MediaTypes htmlTypes;
	public MediaTypes allTypes;
	
	public HtmlTokenInjector(){
		
		this.imageTypes = new MediaTypes(MediaType.getMediaTypes(GroupType.IMAGE));
		this.htmlTypes = new MediaTypes(MediaType.HTML);
		this.allTypes = new MediaTypes(MediaType.values());
	}
	
	public String injectToken(byte[] sourceBytes, String contentType, String token){
		String result = null;
		switch(getMediaType(sourceBytes, contentType)){
		case HTML:
			String source = new String(sourceBytes);
			result = injectToken(source, token);
			break;
			
			default:
				// Do nothing
		}
		return result;
	}
	
	
	public String injectToken(String source, String token){
		Document doc = Jsoup.parse(source);
		Elements els = doc.select("*");
		URLTokenInjector utr = new URLTokenInjector(token);
		els.traverse(utr);
		doc.outputSettings().prettyPrint(false);
		return doc.outerHtml();
	}
	
	public MediaType getMediaType(byte[] sourceBytes, String contentType){
		MediaType ct = MediaType.NULL;
		if(contentType != null && !contentType.isEmpty()){
			String[] contentTypes;
			contentTypes = contentType.split(";");
			List<String> types = Arrays.asList(contentTypes);
			Set<String> knownTypes = allTypes.getStringNotations();
			if(!Collections.disjoint(types, knownTypes)){
				//do parsing
				Collection<String> intersect  = CollectionUtils.intersection(types, knownTypes);
				String type = intersect.iterator().next();
				ct = MediaType.getMediaType(type);
			}
		}
		return ct;
	}
	
private class URLTokenInjector implements NodeVisitor {
		
		private static final String csrfTokenKey = Constants.CSRF_NONCE_REQUEST_PARAM;
		private Set<Node> modifiedNodes = new HashSet<Node>();
		private int missingTokens = 0;
		private String token = null;

		public URLTokenInjector(String token) {
			this.token = token;
		}

		public void head(Node node, int depth) {
			alterAttr(node, "src");
			alterAttr(node, "href");
			alterAttr(node, "action");
		}

		public void tail(Node node, int depth) {
			//removeTokenInput(node);
		}
		
		private void removeTokenInput(Node node){
			if(node instanceof Element){
				Element element = (Element) node;
				String tagName = element.tagName();
				if(tagName != null && tagName.equals("input")){
					String nameAttr = element.attr("name");
					if(nameAttr != null && nameAttr.equals(csrfTokenKey)){
						if(element.parent() != null){
							element.remove();
						}
						
					}
				}
			}
		}
		
		private void alterAttr(Node node, String attr){
			String srcValue = node.attr(attr);
			if (!srcValue.isEmpty()) {
				node.attr(attr, addNonce(srcValue, this.token));
			}
		}
		
		/**
		 * Return the specified URL with the nonce added to the query string. 
		 *
		 * @param url URL to be modified
		 * @param nonce The nonce to add
		 */
    	String addNonce(String url, String nonce) {
    		
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

}
