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
import com.flameling.uva.thesis.validator.Config;
import com.flameling.uva.thesis.validator.TestApp;
import com.flameling.uva.thesis.validator.TokenSecurity;

public class Crawler {
	
	private static final String OUTPUT_FOLDER = "htmloutput";
	private String url;
	boolean secured;
	
	public Crawler(String url, boolean secured){
		this.url = url;
		this.secured = secured;
	}
	
	public void run(){
		CrawljaxRunner crawljax = new CrawljaxRunner(initArchivaBuilder(url, secured).build());
		crawljax.call();
	}
	
	private CrawljaxConfigurationBuilder initArchivaBuilder(String url, boolean secured){
		Config.getInstance().setTestApp(TestApp.ARCHIVA);
		Config.getInstance().getSecurityMeasures().add(new TokenSecurity());
		CrawljaxConfigurationBuilder builder = initGeneralBuilder(url, secured, "archiva");
		builder.crawlRules().setInputSpec(newArchivaSpec());
		builder.crawlRules().insertRandomDataInInputForms(false);
		builder.crawlRules().click("a");
		builder.crawlRules().dontClick("a").withText("Logout");
		builder.crawlRules().click("input").withAttribute("id", "loginForm__login").withAttribute("value", "Login");
		return builder;
	}
	
	private CrawljaxConfigurationBuilder initGeneralBuilder(String url, boolean secured, String folder){
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
		return builder;
	}
	
	private InputSpecification newArchivaSpec(){
		InputSpecification spec = new InputSpecification();
		spec.field("loginForm_username").setValue("admin");
		spec.field("loginForm_password").setValue("admin123");

		return spec;
	}

}
