package com.flameling.uva.thesis.validator;

import java.io.File;

import org.jsoup.nodes.Document;

import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;

public abstract class Config{
	private static Config instance;
	TestApp currentTestApp;
	SecurityMeasures securityMeasures = new SecurityMeasures();

	Config(TestApp testApp){
		this.currentTestApp = testApp;
	}
	
	public static void setInstance(Config config){
		instance = config;
	}
	
	public static Config getInstance(){
		return instance;
	}
	
	public File getOutputFolder(){
		String fullPath = Constants.OUTPUT_BASE_FOLDER.getAbsolutePath() + currentTestApp.getSubFolder();
		File result = new File(fullPath);
		return result;
	}
	
	public SecurityMeasures getSecurityMeasures() {
		return securityMeasures;
	}
	
	abstract public void stripDom(Document doc);
	abstract public void configBuilder(CrawljaxConfigurationBuilder builder);

}
