package com.flameling.uva.thesis.validator.diff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.apache.commons.lang3.StringUtils;

import com.flameling.uva.thesis.validator.Config;
import com.flameling.uva.thesis.validator.FileAnalysisResult;
import com.flameling.uva.thesis.validator.diff.DiffMatchPatch.Diff;
import com.flameling.uva.thesis.validator.diff.DiffMatchPatch.LinesToCharsResult;
import com.flameling.uva.thesis.validator.diff.DiffMatchPatch.Operation;


public class DiffLineCounter {
	private DiffMatchPatch dmp;
	LinesToCharsResult ltcr;
	LinkedList<Diff> diffs;
	private String text1;
	private String text2;
	
	public DiffLineCounter(String text1, String text2){
		this.dmp = new DiffMatchPatch();
		this.text1 = text1;
		this.text2 = text2;
    	LinesToCharsResult ltcr = dmp.diff_linesToChars(text1, text2);
    	diffs = dmp.diff_main(ltcr.chars1, ltcr.chars2, false);
    	dmp.diff_charsToLines(diffs, ltcr.lineArray);
	}
    
    public void writeDiffToFile(File diffOut){
    	if(diffs.isEmpty() || (diffs.size() == 1 && diffs.getFirst().operation.equals(Operation.EQUAL))){
    		return;
    	}
    	String prettyDiff = dmp.diff_prettyHtml(diffs);
    	
    	FileAnalysisResult far = Config.getInstance().getAnalysisResults().getCurrentFileAnalysis();
    	far.setMutationLineCount(getNumberOfDifferingLines());
    	
    	FileWriter out = null;
    	try {
    		diffOut.getParentFile().mkdirs();
			out = new FileWriter(diffOut);
			out.write(prettyDiff);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public int getNumberOfDifferingLines(){
    	return getNumberOfDifferingLines(diffs);
    }
    
    private int getNumberOfDifferingLines(LinkedList<Diff> diffs){
    	int differingLinesCount = 0;
    	for(ListIterator<Diff> iter = diffs.listIterator(); iter.hasNext();){
    		Diff currentDiff = iter.next();
    		switch(currentDiff.operation){
    		case EQUAL: // not interested in counting lines that don't differ.
    			break;
    		case INSERT:
    		case DELETE:
    			differingLinesCount += countUnequivalentLines(iter, diffs, currentDiff);
    		}
    		
    	}
    	return differingLinesCount;
    }
    
    private int countUnequivalentLines(ListIterator<Diff> iter, LinkedList<Diff> diffs, Diff currentDiff){
    	int lineCount = 0;
    	if(iter.hasNext()){
			Diff nextDiff = diffs.get(iter.nextIndex());
			switch(nextDiff.operation){
			case EQUAL: // next Diff is an Equal operation, so we just count the current Diff's number of new line characters
				lineCount = countNewLineChars(currentDiff);
			case INSERT:
			case DELETE: // Since the next Diff is an Insert or Delete operation as well, the operations relate to each other and we
				// want to count the highest number of newline characters in order to register the highest number of lines that have
				// changed.
				int currentLineCount = countNewLineChars(currentDiff);
				// if next diff is the last one, representing the last part of the text, add one to the count of new line characters.
				int nextLineCount = nextDiff == diffs.getLast() ? countNewLineChars(nextDiff) + 1 : countNewLineChars(nextDiff);
				lineCount = Math.max(currentLineCount, nextLineCount);
				// since a sequential Insert/Delete relate to each other and we counted the highest number of changed lines, the
				// next diff is now skipped in processing.
				iter.next();
			}
		} else{
			// currentDiff represents the end of the text, so we need to count one newline extra representing the last line.
			lineCount = countNewLineChars(currentDiff) + 1;
		}
    	return lineCount;
    }
    
    public static int countNewLineChars(Diff diff){
    	String text = diff.text;
    	int matchCount = StringUtils.countMatches(text, "\n");
    	return matchCount;
    }
    
}
