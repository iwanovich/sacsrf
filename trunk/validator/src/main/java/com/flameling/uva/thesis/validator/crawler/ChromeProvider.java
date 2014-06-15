package com.flameling.uva.thesis.validator.crawler;

import java.io.File;

import javax.inject.Provider;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.browser.WebDriverBackedEmbeddedBrowser;

public class ChromeProvider implements Provider<EmbeddedBrowser> {

	public EmbeddedBrowser get() {
		EmbeddedBrowser browser = newChromeBrowser();
		return browser;
	}
	
	private EmbeddedBrowser newChromeBrowser(){
		File file = new File("chromedriver");
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
		WebDriver driver = new ChromeDriver();
		Window window = driver.manage().window();
		window.setSize(new Dimension(800, 600));
		window.setPosition(new Point(800, 0));
		return WebDriverBackedEmbeddedBrowser.withDriver(driver);
	}

}
