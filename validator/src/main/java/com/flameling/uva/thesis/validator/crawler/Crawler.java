package com.flameling.uva.thesis.validator.crawler;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawlRules;
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
import com.flameling.uva.thesis.validator.Constants;
import com.flameling.uva.thesis.validator.OpenKMConfig;
import com.flameling.uva.thesis.validator.TestApp;
import com.flameling.uva.thesis.validator.TokenSecurity;

public class Crawler {
	
	//private static final String OUTPUT_FOLDER = "htmloutput";
	private String url;
//	boolean secured;
	
	public Crawler(String url, TestApp testApp){
		this.url = url;
//		this.secured = secured;
//		Config config;
//		switch(testApp){
//		case ARCHIVA:	
//			config = new ArchivaConfig();
//			break;
//		case OPEN_KM:
//			config = new OpenKMConfig();
//			break;
//		default:
//			config = null;
//		}
//		Config.setInstance(config);
//		config.getSecurityMeasures().add(new TokenSecurity());
	}
	
	public void run(){
		CrawljaxRunner crawljax = new CrawljaxRunner(initBuilder(url).build());
		crawljax.call();
	}
	
	private CrawljaxConfigurationBuilder initBuilder(String url){
		boolean secured = Config.getInstance().getSecurityMeasures().hasSecurityMeasures();
		String outputFolder = Config.getInstance().getOutputFolder() + "/" + (secured ? Constants.SECURED_FOLDER_NAME : Constants.CLEAN_FOLDER_NAME);
		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
		builder.setOutputDirectory(new File(outputFolder));
		LoginPlugin loginPlugin = new LoginPlugin("admin", "admin123");
		BrowserConfiguration bc = new BrowserConfiguration(BrowserType.FIREFOX, 1, new FirefoxProvider(loginPlugin));
		builder.setBrowserConfig(bc);
		OracleComparator oc = new OracleComparator("something", new PlainStructureComparator());
		builder.crawlRules().addOracleComparator(oc);
		//builder.addPlugin(new SaveHTMLPlugin());
		builder.addPlugin(loginPlugin);
		builder.crawlRules().clickOnce(true);
		builder.setUnlimitedCrawlDepth();
		//builder.setMaximumRunTime(20, TimeUnit.MINUTES);
		builder.setUnlimitedRuntime();
		builder.setUnlimitedStates();
		Config.getInstance().configBuilder(builder);
		return builder;
	}

}
