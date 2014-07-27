package com.flameling.uva.thesis.validator;

import org.jsoup.nodes.Document;
import org.mozilla.javascript.ast.AstRoot;

public interface SecurityMeasure {
	
	String cleanUrl(String url);
	public void parseDOM(Document doc);
	public void parseJsAst(AstRoot ast);
	public boolean hasSecurityMeasures();

}
