package com.flameling.uva.thesis.validator.gui;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.SwingConstants;

abstract class IndicatorThread extends Thread {
	
	JButton indicatorButton;
	String title;
	
	public IndicatorThread(JButton indicatorButton, String title){
		this.indicatorButton = indicatorButton;
		this.title = title;
		
	}
	public void run(){
		int orgAlignment = indicatorButton.getHorizontalAlignment();
		indicatorButton.setHorizontalAlignment(SwingConstants.LEFT);
		Vector<String> waitIndicator = new Vector<String>();
		StringBuffer indicatorText = new StringBuffer(title);
		for(int counter = 0; counter < 16; counter++){
			indicatorText.append(".");
			waitIndicator.add(indicatorText.toString());
		}
		try{
			while (true) {
				for (String waitText : waitIndicator) {
					indicatorButton.setText(waitText);
					Thread.sleep(500);
				}
			}
		} catch(InterruptedException ie){
			// break from waiting loop.
		}
		indicatorButton.setHorizontalAlignment(orgAlignment);
		indicatorButton.setText("Done");
		after();
	}
	
	public abstract void after();
}
