package com.flameling.uva.thesis.validator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.plugins.crawloverview.CrawlOverview;

public class ArchivaConfig extends Config {

	public ArchivaConfig(){
		super(TestApp.ARCHIVA);
	}
	
	@Override
	public void stripDom(Document doc) {
		//doc.getElementsByTag("head").first().getElementsByAttributeValue("src", "/archiva/struts/dojo/src/browser_debug.js").remove();
		//doc.getElementsByTag("head").first().getElementsByAttributeValue("src", "/archiva/struts/dojo/src/debug.js").remove();
		//doc.getElementsByTag("head").first().getElementsByAttributeValue("href", "/archiva/struts/xhtml/styles.css").remove();
		//doc.getElementsByTag("head").first().getElementsByAttributeValue("src", "/archiva/struts/dojo/dojo.js").remove();
		//doc.getElementsByTag("head").first().getElementsByAttributeValue("src", "/archiva/struts/simple/dojoRequire.js").remove();
		//doc.getElementsByTag("body").first().getElementById("topSearchBox").getElementsByAttributeValue("src", "/archiva/struts/xhtml/validation.js").remove();
		//doc.getElementsByTag("body").first().getElementsByAttributeValue("href", "http://www.apache.org/").remove();
		//doc.getElementsByTag("body").first().getElementsByAttributeValue("href", "http://archiva.apache.org/").remove();
//		Elements elements = doc.getElementsByTag("head").first().getElementsByTag("script");
//		for(Element element : elements){
//			if(!element.dataNodes().isEmpty() && element.dataNodes().get(0).attr("data").contains("dojo.hostenv._global_omit_module_check = false;")){
//				element.remove();
//			}
//		}
	}

	@Override
	public void configBuilder(CrawljaxConfigurationBuilder builder) {
		//builder.crawlRules().setInputSpec(newArchivaSpec());
		builder.crawlRules().insertRandomDataInInputForms(false);
		builder.crawlRules().click("a");
		builder.crawlRules().dontClick("a").withText("Logout");
		builder.crawlRules().click("input").withAttribute("id", "loginForm__login").withAttribute("value", "Login");
	}
	
	private InputSpecification newArchivaSpec(){
		InputSpecification spec = new InputSpecification();
		spec.field("loginForm_username").setValue("admin");
		spec.field("loginForm_password").setValue("admin123");

		return spec;
	}

}
