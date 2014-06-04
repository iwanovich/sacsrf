package com.flameling.uva.thesis.validator.gui;

import com.flameling.uva.thesis.validator.crawler.Crawler;

class CrawlThread extends AssistingThread{
	Crawler crawler;
	
	public CrawlThread(Thread masterThread, Crawler crawler){
		super(masterThread);
		this.crawler = crawler;
	}
	
	public CrawlThread(Crawler crawler){
		this(null, crawler);
	}

	@Override
	public void assist() {
		crawler.run();
	}

	
}
