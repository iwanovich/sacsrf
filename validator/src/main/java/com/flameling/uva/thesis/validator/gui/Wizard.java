package com.flameling.uva.thesis.validator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.flameling.uva.thesis.validator.crawler.Crawler;

public class Wizard extends JFrame{
	
	JTextField urlField;
	JButton nextButton;
	private JTextField securedUrlField;
	
	public Wizard(){
		getContentPane().setLayout(null);
		this.setBounds(0, 0, 800, 600);
		this.setTitle("Validator");
		
		createComponents();
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void createComponents(){
		JLabel urlLabel = new JLabel();
		urlLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		urlLabel.setBounds(86, 19, 50, 20);
		urlLabel.setText("URL");
		urlField = new JTextField("http://localhost:8080/archiva/");
		urlField.setBounds(154, 20, 600, 20);
		
		nextButton = new JButton("Next");
		nextButton.setBounds(237, 326, 200, 30);
		nextButton.setAction(new FirstCrawlRun("Start crawl"));
		
		getContentPane().add(urlLabel);
		getContentPane().add(urlField);
		getContentPane().add(nextButton);
		
		securedUrlField = new JTextField("http://");
		securedUrlField.setEnabled(false);
		securedUrlField.setBounds(154, 45, 600, 20);
		getContentPane().add(securedUrlField);
		
		JLabel securedUrlLabel = new JLabel("Secured URL");
		securedUrlLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		securedUrlLabel.setBounds(33, 47, 103, 15);
		getContentPane().add(securedUrlLabel);
	}
	
	private void startCrawl(IndicatorThread indicatorThread, String crawlUrl, boolean secured){
		Crawler crawler = new Crawler(crawlUrl, secured, this);
		AssistingThread crawlThread = new CrawlThread(indicatorThread, crawler);
		indicatorThread.start();
		crawlThread.start();
	}
	
	private void afterFirstCrawl(){
		nextButton.setAction(new SecondCrawlRun("start crawling secured url"));
		securedUrlField.setText(urlField.getText());
		securedUrlField.setEnabled(true);
	}
	
	private void afterSecondCrawl(){
		nextButton.setEnabled(false);
	}
	
	class FirstCrawlRun extends AbstractAction{
		
		public FirstCrawlRun(String buttonText){
			super(buttonText);
		}

		public void actionPerformed(ActionEvent e) {
			urlField.setEnabled(false);
			String crawlUrl = urlField.getText().trim();
			IndicatorThread indicatorThread = new IndicatorThread(nextButton, "Crawling"){

				@Override
				public void after() {
					afterFirstCrawl();
				}
				
			};
			startCrawl(indicatorThread, crawlUrl, false);
		}
	}
	
	class SecondCrawlRun extends AbstractAction{
		
		public SecondCrawlRun(String buttonText){
			super(buttonText);
		}

		public void actionPerformed(ActionEvent e) {
			securedUrlField.setEnabled(false);
			String crawlUrl = securedUrlField.getText().trim();
			IndicatorThread indicatorThread = new IndicatorThread(nextButton, "Crawling"){

				@Override
				public void after() {
				afterSecondCrawl();
				}
				
			};
			startCrawl(indicatorThread, crawlUrl, true);		
		}
	}
	

}
