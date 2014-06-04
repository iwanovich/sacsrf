package com.flameling.uva.thesis.validator.crawler;

import javax.inject.Provider;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.browser.WebDriverBackedEmbeddedBrowser;

public class HiddenFirefoxProvider implements Provider<EmbeddedBrowser> {

	public EmbeddedBrowser get() {
		EmbeddedBrowser browser = newInvisibleFirefoxBrowser();
		return browser;
	}
	
	private EmbeddedBrowser newInvisibleFirefoxBrowser(){
		WebDriver driver = new FirefoxDriver();
		Window window = driver.manage().window();
		window.setSize(new Dimension(800, 600));
		window.setPosition(new Point(800, 0));
		return WebDriverBackedEmbeddedBrowser.withDriver(driver);
	}

}
