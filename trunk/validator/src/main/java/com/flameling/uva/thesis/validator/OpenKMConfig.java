package com.flameling.uva.thesis.validator;

import org.jsoup.nodes.Document;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.condition.Condition;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.plugins.crawloverview.CrawlOverview;

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
		builder.crawlRules().setInputSpec(newOpenKMSpec());
		builder.crawlRules().insertRandomDataInInputForms(false);
		builder.crawlRules().click("a");
		builder.crawlRules().dontClick("a").underXPath("//BODY/H2[text()='Info']/following-sibling::ul/LI/B[text()='Actions']/following-sibling::a|//BODY/H2[text()='Info']/following-sibling::ul/LI/B[text()='Scripting']/following-sibling::a");
		builder.crawlRules().dontClick("a").underXPath("//A[@class='ds']/IMG[contains(@src, 'browse')]/..");
		builder.crawlRules().dontClick("a").underXPath("//A/IMG[@title='Delete']/..");
		builder.crawlRules().dontClick("a").underXPath("//A/IMG[@title='Exit']/..");
		
		builder.crawlRules().click("input").withAttribute("id", "loginForm__login").withAttribute("value", "Login");
	}
	
	private InputSpecification newOpenKMSpec(){
		InputSpecification spec = new InputSpecification();
		spec.field("j_username").setValue("okmAdmin");
		spec.field("j_password").setValue("admin");
		return spec;
	}

}
