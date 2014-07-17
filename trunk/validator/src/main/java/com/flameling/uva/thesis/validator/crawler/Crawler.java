package com.flameling.uva.thesis.validator.crawler;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.configuration.Form;
import com.crawljax.core.configuration.FormAction;
import com.crawljax.core.configuration.InputField;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.oraclecomparator.OracleComparator;
import com.crawljax.oraclecomparator.comparators.PlainStructureComparator;
import com.crawljax.oraclecomparator.comparators.XPathExpressionComparator;
import com.crawljax.plugins.crawloverview.CrawlOverview;
import com.flameling.uva.thesis.validator.ArchivaConfig;
import com.flameling.uva.thesis.validator.Config;
import com.flameling.uva.thesis.validator.OpenKMConfig;
import com.flameling.uva.thesis.validator.TestApp;
import com.flameling.uva.thesis.validator.TokenSecurity;

public class Crawler {
	
	//private static final String OUTPUT_FOLDER = "htmloutput";
	private String url;
	boolean secured;
	private Config config;
	
	public Crawler(String url, boolean secured, TestApp testApp){
		this.url = url;
		this.secured = secured;
		switch(testApp){
		case ARCHIVA:	
			this.config = new ArchivaConfig();
		case OPEN_KM:
			this.config = new OpenKMConfig();
		}
		Config.setInstance(this.config);
		config.getSecurityMeasures().add(new TokenSecurity());
	}
	
	public void run(){
		CrawljaxRunner crawljax = new CrawljaxRunner(initBuilder(url, secured).build());
		crawljax.call();
	}
	
	private CrawljaxConfigurationBuilder initBuilder(String url, boolean secured){
		String outputFolder = Config.getInstance().getOutputFolder() + (secured ? "/secured" : "/clean");
		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
		builder.setOutputDirectory(new File(outputFolder));
		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.CHROME, 1, new ChromeProvider()));
		OracleComparator oc = new OracleComparator("something", new PlainStructureComparator());
		builder.crawlRules().addOracleComparator(oc);
		builder.addPlugin(new SaveHTMLPlugin());
		builder.crawlRules().clickOnce(false);
		builder.setUnlimitedCrawlDepth();
		builder.setUnlimitedRuntime();
		builder.setUnlimitedStates();
		Config.getInstance().configBuilder(builder);
		return builder;
	}

}
