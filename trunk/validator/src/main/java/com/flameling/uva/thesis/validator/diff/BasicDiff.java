package com.flameling.uva.thesis.validator.diff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import com.flameling.uva.thesis.validator.diff.DiffMatchPatch.Diff;
import com.flameling.uva.thesis.validator.diff.DiffMatchPatch.LinesToCharsResult;


public class BasicDiff {
    
    public static void diffLineMode(String text1, String text2, File diffOut){
    	DiffMatchPatch dmp = new DiffMatchPatch();
    	LinesToCharsResult ltcr = dmp.diff_linesToChars(text1, text2);
    	LinkedList<Diff> diffs = dmp.diff_main(ltcr.chars1, ltcr.chars2, false);
    	dmp.diff_charsToLines(diffs, ltcr.lineArray);
    	dmp.diff_cleanupSemantic(diffs);
    	String prettyDiff = dmp.diff_prettyHtml(diffs);
    	
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
    
}
