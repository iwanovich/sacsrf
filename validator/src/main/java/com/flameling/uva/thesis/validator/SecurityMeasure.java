package com.flameling.uva.thesis.validator;

import org.jsoup.nodes.Document;

public interface SecurityMeasure {
	
	String cleanUrl(String url);
	public void parseDOM(Document doc);
	public String parseJSData(String data);

}
