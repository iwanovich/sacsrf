package com.flameling.uva.thesis.validator.gui;


abstract class AssistingThread extends Thread {
	
	private Thread masterThread;
	
	public AssistingThread(){
		super();
	}
	
	public AssistingThread(Thread masterThread){
		setMasterThread(masterThread);
	}

	public void run() {
		assist();
		notifyMaster();
	}
	
	private void notifyMaster(){
		if(masterThread != null)
			masterThread.interrupt();
	}
	
	abstract public void assist();

	public Thread getMasterThread() {
		return masterThread;
	}

	public void setMasterThread(Thread masterThread) {
		this.masterThread = masterThread;
	}
	
}
