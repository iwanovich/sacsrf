package com.flameling.uva.thesis.validator.parser;

import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;

import com.flameling.uva.thesis.validator.Constants;
import com.flameling.uva.thesis.validator.Util;

public class SrcVisitor implements NodeVisitor {
	
	private static final String csrfTokenKey = Constants.TOKEN_KEY;

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
			node.attr(attr, Util.stripToken(srcValue));
		}
	}

}
