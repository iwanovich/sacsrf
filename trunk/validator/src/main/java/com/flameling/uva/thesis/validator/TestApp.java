package com.flameling.uva.thesis.validator;

public enum TestApp {
	
	ARCHIVA("/archiva", "http://localhost:8080/archiva"),
	OPEN_KM("/open_km", "http://localhost:8080/OpenKM/admin");
	
	private String subFolder;
	private String defaultUrl;
	
	TestApp(String subFolder, String defaultUrl){
		this.subFolder = subFolder;
		this.defaultUrl = defaultUrl;
	}
	
	public String getSubFolder(){
		return this.subFolder;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

}
