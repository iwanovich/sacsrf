package com.flameling.uva.thesis.validator;

import org.jsoup.nodes.Document;

import com.flameling.uva.thesis.validator.parser.JSParser;

public class NoSecurity implements SecurityMeasure {

	public String cleanUrl(String url) {
		return url;
	}

	public void parseDOM(Document doc) {
	}

	public String parseJSData(String data) {
		JSParser jsParser = new JSParser();
		return jsParser.cleanParse(data);
	}

}
