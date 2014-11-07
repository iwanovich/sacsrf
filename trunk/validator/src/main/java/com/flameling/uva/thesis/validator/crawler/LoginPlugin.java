package com.flameling.uva.thesis.validator.crawler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawlRules;
import com.crawljax.core.configuration.CrawlRules.CrawlRulesBuilder;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.plugin.OnBrowserCreatedPlugin;
import com.crawljax.core.plugin.PreCrawlingPlugin;
import com.crawljax.core.state.Eventable;
import com.crawljax.core.state.Eventable.EventType;
import com.crawljax.core.state.Identification;
import com.crawljax.forms.FormHandler;
import com.crawljax.forms.FormInput;
import com.crawljax.util.DomUtils;

public class LoginPlugin implements OnBrowserCreatedPlugin {
	
	public final String UNFIELD = new String("loginForm_username");
	public final String PWFIELD = new java.lang.String("loginForm_password");
	
	String username;
	String password;
	CrawlRules rules;
	
	public LoginPlugin(String un, String pw){
		this.username = un;
		this.password = pw;
	}

	public void onBrowserCreated(EmbeddedBrowser newBrowser) {
		List<FormInput> formInputs = new ArrayList<FormInput>();
		
		// LDS Account field is "userName"
		Identification unId = new Identification();
		unId.setValue(UNFIELD);
		unId.setHow(Identification.How.id);
		formInputs.add(new FormInput("text", unId, username)); 
		// LDS Account field is "j_password"
		Identification pwId = new Identification();
		pwId.setValue(PWFIELD);
		pwId.setHow(Identification.How.id);
		formInputs.add(new FormInput("text", pwId, password));
		
		String submitXpath = "//*[@id=\"loginForm__login\"]";
		
		try {
			newBrowser.goToUrl(new URI("http://localhost:8080/archiva/security/login.action"));
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		newBrowser.input(unId, username);
		newBrowser.input(pwId, password);
		
		try {
			Document dom = DomUtils.asDocument(newBrowser.getStrippedDom());
			Node nodeLogin = DomUtils.getElementByXpath(dom, submitXpath);
			
			newBrowser.fireEventAndWait(new Eventable(nodeLogin, EventType.click));
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
