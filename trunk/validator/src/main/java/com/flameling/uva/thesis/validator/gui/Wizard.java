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
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.flameling.uva.thesis.validator.ArchivaConfig;
import com.flameling.uva.thesis.validator.Config;
import com.flameling.uva.thesis.validator.NoSecurity;
import com.flameling.uva.thesis.validator.OpenKMConfig;
import com.flameling.uva.thesis.validator.TestApp;
import com.flameling.uva.thesis.validator.TokenSecurity;
import com.flameling.uva.thesis.validator.crawler.Crawler;
import com.flameling.uva.thesis.validator.diff.BasicDiff;
import com.flameling.uva.thesis.validator.parser.Parser;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Wizard extends JFrame{
	
	JTextField urlField;
	JButton nextButton;
	private JTextField securedUrlField;
	private String unsecuredUrl;
	private String securedUrl;
	private final RadioButtonGroupEnumAdapter<TestApp> testAppButtonGroup = new RadioButtonGroupEnumAdapter<TestApp>(TestApp.class);
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JCheckBox chckbxTokenBased;
	private JCheckBox chckbxExplicitConfirmation;
	
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
		
		chckbxTokenBased = new JCheckBox("Token based");
		chckbxTokenBased.setSelected(false);
		chckbxTokenBased.setEnabled(true);
		chckbxTokenBased.setBounds(153, 172, 128, 23);
		getContentPane().add(chckbxTokenBased);
		
		chckbxExplicitConfirmation = new JCheckBox("Explicit confirmation");
		chckbxExplicitConfirmation.setEnabled(false);
		chckbxExplicitConfirmation.setBounds(293, 172, 164, 23);
		getContentPane().add(chckbxExplicitConfirmation);
		
		JButton btnAnalysis = new JButton("Analysis");
		btnAnalysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startAnalysis();
			}
		});
		btnAnalysis.setBounds(321, 373, 117, 29);
		getContentPane().add(btnAnalysis);
	}
	
	private void startCrawl(IndicatorThread indicatorThread, String crawlUrl, boolean secured){
		initConfig();
		Crawler crawler = new Crawler(crawlUrl, testAppButtonGroup.getValue());
		AssistingThread crawlThread = new CrawlThread(indicatorThread, crawler);
		indicatorThread.start();
		crawlThread.start();
	}
	
	private void startAnalysis(){
		initConfig();
		Parser parser = new Parser();
		parser.parse();
		afterAnalysis();
	}
	
	private void initConfig(){
		Config config;
		switch(testAppButtonGroup.getValue()){
		case ARCHIVA:	
			config = new ArchivaConfig();
			break;
		case OPEN_KM:
			config = new OpenKMConfig();
			break;
		default:
			config = null;
		}
		Config.setInstance(config);
		if(chckbxTokenBased.isSelected()){
			config.getSecurityMeasures().add(new TokenSecurity());
		} else {
			config.getSecurityMeasures().add(new NoSecurity());
		}
	}
	
	private void afterFirstCrawl(){
		nextButton.setAction(new SecondCrawlRun("start crawling secured url"));
		securedUrlField.setText(urlField.getText());
		securedUrlField.setEnabled(true);
		chckbxExplicitConfirmation.setEnabled(true);
		chckbxTokenBased.setEnabled(true);
	}
	
	private void afterSecondCrawl(){
		nextButton.setAction(new Analysis("start analysis"));
		chckbxExplicitConfirmation.setEnabled(false);
		chckbxTokenBased.setEnabled(true);
	}
	
	private void afterAnalysis(){
		nextButton.setEnabled(false);
		String message = "Number of missing tokens: " + TokenSecurity.missingTokens
				+ "\n Number of diffs in findArtifact.action.html: " + BasicDiff.diffCounter;
		JOptionPane.showMessageDialog(this, message);
	}
	
	@SuppressWarnings("serial")
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
	
	@SuppressWarnings("serial")
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
	
	@SuppressWarnings("serial")
	class Analysis extends AbstractAction{
		
		public Analysis(String buttonText){
			super(buttonText);
		}

		public void actionPerformed(ActionEvent e) {
//			IndicatorThread indicatorThread = new IndicatorThread(nextButton, "Analyzing"){
//
//				@Override
//				public void after() {
//				afterAnalysis();
//				}
//				
//			};
			nextButton.setEnabled(false);
			startAnalysis();
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

