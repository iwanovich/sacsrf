package com.flameling.uva.thesis.partokas;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

import com.flameling.uva.thesis.partokas.http.header.contenttype.ContentType;

public class HtmlTokenInjector {

	
	public HtmlTokenInjector(){
	}
	
	public byte[] injectToken(byte[] sourceBytes, String contentType, String token, String serverName){
		byte[] result = sourceBytes;
		switch(ContentType.getMediaType(contentType)){
		case HTML:
			String source = new String(sourceBytes);
			String injectedSource = injectToken(source, token, serverName);
			result = injectedSource.getBytes(Charset.defaultCharset());
			break;
			
			default:
				// Do nothing
		}
		return result;
	}
	
	
	public String injectToken(String source, String token, String serverName){
		Document doc = Jsoup.parse(source);
		Elements els = doc.select("*");
		URLTokenInjector utr = new URLTokenInjector(token, serverName);
		els.traverse(utr);
		doc.outputSettings().prettyPrint(false);
		return doc.outerHtml();
	}
	
private class URLTokenInjector implements NodeVisitor {
		
		private static final String csrfTokenKey = Constants.TOKEN_KEY;
		private Set<Node> modifiedNodes = new HashSet<Node>();
		private int missingTokens = 0;
		private String token = null;
		private String serverName = null;

		public URLTokenInjector(String token, String serverName) {
			this.token = token;
			this.serverName= serverName;
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
				if(!srcValue.contains(csrfTokenKey) && !Util.isOtherDomain(srcValue, this.serverName)) {
					//node.attr(attr, Util.stripToken(srcValue));
					//modifiedNodes.add(node);
					node.attr(attr, addNonce(srcValue, this.token));
				}
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
