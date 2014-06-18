package com.flameling.uva.thesis.validator.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.flameling.uva.thesis.validator.Constants;
import com.flameling.uva.thesis.validator.Util;
import com.flameling.uva.thesis.validator.diff.BasicDiff;

public class Parser {
	
	private File cleanFolder = new File(Constants.OUTPUT_FOLDER.getAbsolutePath() + "/" + Constants.CLEAN_FOLDER_NAME);
	private File securedFolder = new File(Constants.OUTPUT_FOLDER.getAbsolutePath() + "/" + Constants.SECURED_FOLDER_NAME);
	
	public static void main(String[] args){
		Parser parser = new Parser();
		parser.parse();
	}
	
	private void parseClean(File folder){
		List<File> cleanFiles = Util.getFiles(folder, false);
		for(File file : cleanFiles){
			try {
				String parsedDOM = parseDOM(FileUtils.readFileToString(file));
				FileUtils.write(file, parsedDOM);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<File> cleanDirs = Util.getDirectories(folder, false);
		for(File dir : cleanDirs){
			parseClean(dir);
		}
		
	}
	
	private void parse(){
		parseClean(cleanFolder);
		
		//diff(metaData);
		// do diff for multiple state files
		// perist diff file to disk
		// persist state files to disk
		
		// do diff for matching filenames
		// if applicable, dump diff to disk
		// report on filename mismatches
		
	}
	
	private void diff(File cleanFile, File securedFile) throws IOException{
		String cleanData = FileUtils.readFileToString(cleanFile);
		String securedData = FileUtils.readFileToString(securedFile);
		File diffFile = createDiffFile(cleanFile);
		BasicDiff.diffLineMode(cleanData, securedData, diffFile); // writing the diff to disk
	}
	
	private File createDiffFile(File originalFile){
		String path = originalFile.getParent();
		String subdir = path.substring(Constants.OUTPUT_FOLDER.getAbsolutePath().length());
		String diffDir = Constants.OUTPUT_FOLDER.getAbsolutePath() + "/diff" + subdir;
		File diffFile = new File(diffDir+"/"+originalFile.getName());
		return diffFile;
	}
	
	private String removeTokenFromDOM(String dom){
		Document doc = Jsoup.parse(dom);
		Elements els = doc.select("*");
		els.traverse(new SrcVisitor());
		return doc.outerHtml();
	}
	
	private String parseDOM(String dom){
		Document doc = Jsoup.parse(dom);
		return doc.outerHtml();
	}

}
