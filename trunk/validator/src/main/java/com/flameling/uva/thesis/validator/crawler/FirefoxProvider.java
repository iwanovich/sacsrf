package com.flameling.uva.thesis.validator.crawler;

import javax.inject.Provider;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.browser.WebDriverBackedEmbeddedBrowser;

public class FirefoxProvider implements Provider<EmbeddedBrowser> {
	
	private LoginPlugin loginPlugin;
	
	public FirefoxProvider(LoginPlugin loginPlugin){
		this.loginPlugin = loginPlugin;
	}

	public EmbeddedBrowser get() {
		EmbeddedBrowser browser = newFirefoxBrowser();
		return browser;
	}
	
	private EmbeddedBrowser newFirefoxBrowser(){
		WebDriver driver = new FirefoxDriver();
//		FirefoxProfile profile = new FirefoxProfile();
//		profile.setPreference("browser.cache.disk.enable", false);
//		profile.setPreference("browser.cache.memory.enable", false);
//		profile.setPreference("browser.cache.offline.enable", false);
//		profile.setPreference("network.http.use-cache", false);
//		WebDriver driver = new FirefoxDriver(profile); 
		Window window = driver.manage().window();
		window.setSize(new Dimension(800, 600));
		window.setPosition(new Point(800, 0));
		
		EmbeddedBrowser browser =  WebDriverBackedEmbeddedBrowser.withDriver(driver);
		if(loginPlugin != null){
			loginPlugin.onBrowserCreated(browser);
		}
		return browser;
	}
}
