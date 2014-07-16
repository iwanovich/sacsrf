package com.flameling.uva.thesis.validator;

public enum TestApp {
	
	ARCHIVA("/archiva"),
	OPEN_KM("/open_km");
	
	private String subFolder;
	
	TestApp(String subFolder){
		this.subFolder = subFolder;
	}
	
	public String getSubFolder(){
		return this.subFolder;
	}

}
