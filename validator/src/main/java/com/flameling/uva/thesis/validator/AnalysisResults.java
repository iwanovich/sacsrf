package com.flameling.uva.thesis.validator;

import java.util.HashMap;
import java.util.Map;

public class AnalysisResults {
	
	private Map<String, FileAnalysisResult> files = new HashMap<String, FileAnalysisResult>();
	private FileAnalysisResult currentFileAnalysis;

	public FileAnalysisResult getCurrentFileAnalysis(){
		return this.currentFileAnalysis;
	}
	
	public void setCurrentFileAnalysis(FileAnalysisResult currentFileAnalysis) {
		this.currentFileAnalysis = currentFileAnalysis;
	}

	public FileAnalysisResult put(String key, FileAnalysisResult value) {
		return files.put(key, value);
	}
	
	public FileAnalysisResult get(String key) {
		FileAnalysisResult far = files.get(key);
		if(far == null)
			far = new FileAnalysisResult(key);
		return far;
	}
	
	public int getMutationLineCount(){
		int total = 0;
		for(FileAnalysisResult result : files.values()){
			total += result.getMutationLineCount();
		}
		return total;
	}

	public int getMissingTokenUrlMutations(){
		int total = 0;
		for(FileAnalysisResult result : files.values())
			total += result.getMissingTokenUrlMutations();
		return total;
	}

}
