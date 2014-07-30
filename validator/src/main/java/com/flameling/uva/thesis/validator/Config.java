package com.flameling.uva.thesis.validator;

import java.io.File;

import org.jsoup.nodes.Document;

import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;

public abstract class Config{
	private static Config instance;
	private boolean securityBlock = false;
	TestApp currentTestApp;
	SecurityMeasures securityMeasures = new SecurityMeasures();
	public File currentFile;

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
	
	public void setSecurityMeasureBlock(boolean block){
		this.securityBlock = block;
	}
	
	public SecurityMeasures getSecurityMeasures() {
		return securityBlock ? new SecurityMeasures() : securityMeasures;
	}
	
	abstract public void stripDom(Document doc);
	abstract public void configBuilder(CrawljaxConfigurationBuilder builder);

}
