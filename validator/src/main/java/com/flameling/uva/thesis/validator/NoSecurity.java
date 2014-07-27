package com.flameling.uva.thesis.validator;

import org.jsoup.nodes.Document;
import org.mozilla.javascript.ast.AstRoot;

import com.flameling.uva.thesis.validator.parser.JSParser;

public class NoSecurity implements SecurityMeasure {

	public String cleanUrl(String url) {
		return url;
	}

	public void parseDOM(Document doc) {
	}

	public void parseJsAst(AstRoot ast) {
	}

	public boolean hasSecurityMeasures() {
		return false;
	}

}
