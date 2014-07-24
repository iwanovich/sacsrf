package com.flameling.uva.thesis.validator;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.flameling.uva.thesis.validator.parser.JSParser;
import com.flameling.uva.thesis.validator.parser.TokenURLCleaner;

public class TokenSecurity implements SecurityMeasure {

	public String cleanUrl(String url) {
		return Util.stripToken(url);
	}
	
	public void parseDOM(Document doc){
		Elements els = doc.select("*");
		els.traverse(new TokenURLCleaner());
	}
	
	public String parseJSData(String data){
		String parsedData;
		JSParser jsParser = new JSParser();
		parsedData = jsParser.removeTokens(data);
		return parsedData;
	}

	public boolean hasSecurityMeasures() {
		return true;
	}

}
