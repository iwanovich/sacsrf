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

public class Crawler {
	
	private static final String OUTPUT_FOLDER = "htmloutput";
	private String url;
	private String outputFolder;
	JFrame frame;
	
	public Crawler(String url, boolean secured, JFrame frame){
		config(url, secured);
		this.frame = frame;
	}
	
	public void run(){
		CrawljaxRunner crawljax = new CrawljaxRunner(initConfig().build());
		crawljax.call();
	}
	
	public void config(String url, boolean secured){
		this.url = url;
		this.outputFolder = OUTPUT_FOLDER + (secured ? "/secured" : "/clean");
	}
	
	private CrawljaxConfigurationBuilder initConfig(){
		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.CHROME, 1, new ChromeProvider()));
		
		builder.crawlRules().setInputSpec(newArchivaSpec());
		
		builder.crawlRules().insertRandomDataInInputForms(false);
		builder.setOutputDirectory(new File(outputFolder));

		builder.crawlRules().click("a");
		//builder.crawlRules().click("button");
		builder.crawlRules().dontClick("a").withText("Logout");
		builder.crawlRules().clickOnce(true).click("input").withAttribute("id", "loginForm__login").withAttribute("value", "Login");
		OracleComparator oc = new OracleComparator("something", new PlainStructureComparator());
		builder.crawlRules().addOracleComparator(oc);
		builder.crawlRules().clickOnce(false);
		
		//builder.addPlugin(new SamplePlugin());
		builder.addPlugin(new SaveHTMLPlugin(frame));
		//builder.addPlugin(new CrawlOverview());
		
		builder.setUnlimitedCrawlDepth();
		builder.setUnlimitedRuntime();
		builder.setUnlimitedStates();
		
		return builder;
	}
	
	private InputSpecification newArchivaSpec(){
		InputSpecification spec = new InputSpecification();
		spec.field("loginForm_username").setValue("admin");
		spec.field("loginForm_password").setValue("admin123");

		return spec;
	}

}
