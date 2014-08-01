package com.flameling.uva.thesis.validator;

import java.io.File;

import org.jsoup.nodes.Document;
import org.mozilla.javascript.ast.AstRoot;

public class NoSecurity implements SecurityMeasure {

	public String cleanUrl(String url) {
		return url;
	}

	public void parseDOM(Document doc, File currentFile) {
	}

	public void parseJsAst(AstRoot ast) {
	}

	public boolean hasSecurityMeasures() {
		return false;
	}

}
