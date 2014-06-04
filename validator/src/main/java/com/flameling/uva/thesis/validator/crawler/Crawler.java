package com.flameling.uva.thesis.validator.crawler;

import java.io.File;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;

public class Crawler {
	
	private static final String OUTPUT_FOLDER = "htmloutput";
	private static final int MAX_DEPTH = 2;
	private static final int MAX_NUMBER_STATES = 8;
	
	private String url;
	private String outputFolder;
	
	public Crawler(String url){
		config(url, false);
	}
	
	public void run(){
		CrawljaxRunner crawljax = new CrawljaxRunner(initConfig().build());
		crawljax.call();
	}
	
	public void config(String url, boolean secured){
		this.url = url;
		this.outputFolder = OUTPUT_FOLDER + (secured ? "-secured" : "-clean");
	}
	
	private CrawljaxConfigurationBuilder initConfig(){
		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.FIREFOX, 1, new HiddenFirefoxProvider()));
		builder.crawlRules().insertRandomDataInInputForms(false);
		builder.setOutputDirectory(new File(outputFolder));

		builder.crawlRules().click("a");
		builder.crawlRules().click("button");

		// except these
		builder.crawlRules().dontClick("a").underXPath("//DIV[@id='guser']");
		builder.crawlRules().dontClick("a").withText("Language Tools");

		// limit the crawling scope
		builder.setMaximumStates(MAX_NUMBER_STATES);
		builder.setMaximumDepth(MAX_DEPTH);

		//builder.addPlugin(new SamplePlugin());
		builder.addPlugin(new SaveHTMLPlugin());
		
		return builder;
	}

}
