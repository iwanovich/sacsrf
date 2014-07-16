package com.flameling.uva.thesis.validator;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;

public class SecurityMeasures implements SecurityMeasure {
	
	Set<SecurityMeasure> securityMeasures;
	
	public SecurityMeasures(){
		this.securityMeasures = new HashSet<SecurityMeasure>();
	}

	public String cleanUrl(String url){
		for(SecurityMeasure sm : securityMeasures){
			url = sm.cleanUrl(url);
		}
		return url;
	}
	
	public void add(SecurityMeasure securityMeasure){
		this.securityMeasures.add(securityMeasure);
	}

	public void parseDOM(Document doc) {
		for(SecurityMeasure sm : securityMeasures){
			sm.parseDOM(doc);
		}
	}

	public String parseJSData(String data) {
		for(SecurityMeasure sm : securityMeasures){
			data = sm.parseJSData(data);
		}
		return data;
	}

}
