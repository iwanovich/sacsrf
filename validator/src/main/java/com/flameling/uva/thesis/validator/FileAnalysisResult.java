package com.flameling.uva.thesis.validator;

import java.io.File;

public class FileAnalysisResult {
	
	private String fileId;
	private int missingTokenUrlMutations;
	private int urlMutations;
	private int lineCount;
	private int mutationLineCount;
	
	public FileAnalysisResult(String fileId){
		this.fileId = fileId;
	}
	
	public int getMissingTokenUrlMutations() {
		return missingTokenUrlMutations;
	}
	public void setMissingTokenUrlMutations(int missingTokenUrlMutations) {
		this.missingTokenUrlMutations = missingTokenUrlMutations;
	}
	public int getUrlMutations() {
		return urlMutations;
	}
	public void setUrlMutations(int urlMutations) {
		this.urlMutations = urlMutations;
	}
	public int getLineCount() {
		return lineCount;
	}
	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}
	public int getMutationLineCount() {
		return mutationLineCount;
	}
	public void setMutationLineCount(int mutationLineCount) {
		this.mutationLineCount = mutationLineCount;
	}
	
	

}
