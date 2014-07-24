package com.flameling.uva.thesis.validator;

import java.util.Collection;
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
	
	public void add(SecurityMeasure securityMeasure){
		this.securityMeasures.add(securityMeasure);
	}
	
	public void addAll(Collection<SecurityMeasure> securityMeasures){
		this.securityMeasures.addAll(securityMeasures);
	}
	
	public void setAll(Set<SecurityMeasure> securityMeasures){
		this.securityMeasures = securityMeasures;
	}

	public int size() {
		return securityMeasures.size();
	}

	public boolean isEmpty() {
		return securityMeasures.isEmpty();
	}

	public boolean contains(Object o) {
		return securityMeasures.contains(o);
	}

	public boolean remove(Object o) {
		return securityMeasures.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return securityMeasures.containsAll(c);
	}

	public boolean removeAll(Collection<?> c) {
		return securityMeasures.removeAll(c);
	}

	public void clear() {
		securityMeasures.clear();
	}

	public boolean hasSecurityMeasures() {
		for (SecurityMeasure sm : this.securityMeasures){
			if(sm.hasSecurityMeasures())
				return true;
		}
		return false;
	}

}
