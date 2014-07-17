package com.flameling.uva.thesis.validator.gui;

import java.awt.event.ActionEvent;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.flameling.uva.thesis.validator.TestApp;
import com.flameling.uva.thesis.validator.crawler.Crawler;

public class Wizard extends JFrame{
	
	JTextField urlField;
	JButton nextButton;
	private JTextField securedUrlField;
	private String unsecuredUrl;
	private String securedUrl;
	private final RadioButtonGroupEnumAdapter<TestApp> testAppButtonGroup = new RadioButtonGroupEnumAdapter<TestApp>(TestApp.class);
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
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
		urlLabel.setBounds(86, 84, 50, 20);
		urlLabel.setText("URL");
		urlField = new JTextField("http://localhost:8080/archiva/");
		urlField.setBounds(154, 85, 600, 20);
		
		nextButton = new JButton("Next");
		nextButton.setBounds(299, 289, 200, 30);
		nextButton.setAction(new FirstCrawlRun("Start crawl"));
		
		getContentPane().add(urlLabel);
		getContentPane().add(urlField);
		getContentPane().add(nextButton);
		
		securedUrlField = new JTextField("http://");
		securedUrlField.setEnabled(false);
		securedUrlField.setBounds(153, 207, 600, 20);
		getContentPane().add(securedUrlField);
		
		JLabel securedUrlLabel = new JLabel("Secured URL");
		securedUrlLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		securedUrlLabel.setBounds(32, 209, 103, 15);
		getContentPane().add(securedUrlLabel);
		
		JRadioButton rdbtnArchiva = new JRadioButton("Archiva");
		buttonGroup.add(rdbtnArchiva);
		rdbtnArchiva.setSelected(true);
		testAppButtonGroup.associate(TestApp.ARCHIVA, rdbtnArchiva);
		rdbtnArchiva.setBounds(154, 54, 79, 23);
		getContentPane().add(rdbtnArchiva);
		
		JRadioButton rdbtnOpenkm = new JRadioButton("OpenKM");
		buttonGroup.add(rdbtnOpenkm);
		testAppButtonGroup.associate(TestApp.OPEN_KM, rdbtnOpenkm);
		rdbtnOpenkm.setBounds(245, 54, 84, 23);
		getContentPane().add(rdbtnOpenkm);
		
		JLabel lblTestApplication = new JLabel("Test application");
		lblTestApplication.setBounds(154, 36, 103, 16);
		getContentPane().add(lblTestApplication);
		
		JLabel lblCsrfSecurityMeasure = new JLabel("CSRF Security measure");
		lblCsrfSecurityMeasure.setHorizontalAlignment(SwingConstants.LEFT);
		lblCsrfSecurityMeasure.setBounds(153, 157, 148, 16);
		getContentPane().add(lblCsrfSecurityMeasure);
		
		JCheckBox chckbxTokenBased = new JCheckBox("Token based");
		chckbxTokenBased.setSelected(true);
		chckbxTokenBased.setEnabled(false);
		chckbxTokenBased.setBounds(153, 172, 128, 23);
		getContentPane().add(chckbxTokenBased);
		
		JCheckBox chckbxExplicitConfirmation = new JCheckBox("Explicit confirmation");
		chckbxExplicitConfirmation.setEnabled(false);
		chckbxExplicitConfirmation.setBounds(293, 172, 164, 23);
		getContentPane().add(chckbxExplicitConfirmation);
	}
	
	private void startCrawl(IndicatorThread indicatorThread, String crawlUrl, boolean secured){
		Crawler crawler = new Crawler(crawlUrl, secured, testAppButtonGroup.getValue());
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
			unsecuredUrl = crawlUrl;
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
			securedUrl = crawlUrl;
			startCrawl(indicatorThread, crawlUrl, true);		
		}
	}
	
	/*
	 * created by Jason S. http://stackoverflow.com/questions/2059122/how-to-use-jradiobutton-groups-with-a-model
	 */
	class RadioButtonGroupEnumAdapter<E extends Enum<E>> {
	    final private Map<E, JRadioButton> buttonMap;

	    public RadioButtonGroupEnumAdapter(Class<E> enumClass)
	    {
	        this.buttonMap = new EnumMap<E, JRadioButton>(enumClass);
	    }
	    public void importMap(Map<E, JRadioButton> map)
	    {
	        for (E e : map.keySet())
	        {
	            this.buttonMap.put(e, map.get(e));
	        }
	    }
	    public void associate(E e, JRadioButton btn)
	    {
	        this.buttonMap.put(e, btn);
	    }
	    public E getValue()
	    {
	        for (E e : this.buttonMap.keySet())
	        {
	            JRadioButton btn = this.buttonMap.get(e);
	            if (btn.isSelected())
	            {
	                return e;
	            }
	        }
	        return null;
	    }
	    public void setValue(E e)
	    {
	        JRadioButton btn = (e == null) ? null : this.buttonMap.get(e);
	        if (btn == null)
	        {
	            // the following doesn't seem efficient...
	                    // but since when do we have more than say 10 radiobuttons?
	            for (JRadioButton b : this.buttonMap.values())
	            {
	                b.setSelected(false);
	            }

	        }
	        else
	        {
	            btn.setSelected(true);
	        }
	    }
	}
	
}

