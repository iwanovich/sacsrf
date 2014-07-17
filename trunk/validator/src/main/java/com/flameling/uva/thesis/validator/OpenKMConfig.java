package com.flameling.uva.thesis.validator;

import org.jsoup.nodes.Document;

import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;

public class OpenKMConfig extends Config {
	
	public OpenKMConfig(){
		super(TestApp.OPEN_KM);
	}

	@Override
	public void stripDom(Document doc) {
		// TODO Auto-generated method stub
	}

	@Override
	public void configBuilder(CrawljaxConfigurationBuilder builder) {
		// TODO Auto-generated method stub
	}

}
