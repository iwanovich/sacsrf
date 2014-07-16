package com.flameling.uva.thesis.validator;

import java.io.File;
import java.util.Set;

public class Config{
	static Config instance;
	TestApp currentTestApp;
	SecurityMeasures securityMeasures = new SecurityMeasures();

	public static Config getInstance(){
		if (instance == null){
			try {
				instance = Config.class.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public void setTestApp(TestApp testApp){
		this.currentTestApp = testApp;
	}
	
	public File getOutputFolder(){
		String fullPath = Constants.OUTPUT_BASE_FOLDER.getAbsolutePath() + currentTestApp.getSubFolder();
		File result = new File(fullPath);
		return result;
	}
	
	public SecurityMeasures getSecurityMeasures() {
		return securityMeasures;
	}

}
