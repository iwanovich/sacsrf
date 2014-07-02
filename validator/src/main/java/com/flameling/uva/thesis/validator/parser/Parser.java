package com.flameling.uva.thesis.validator.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.flameling.uva.thesis.validator.Constants;
import com.flameling.uva.thesis.validator.Util;
import com.flameling.uva.thesis.validator.diff.BasicDiff;

public class Parser {
	
	private File cleanFolder = new File(Constants.OUTPUT_FOLDER.getAbsolutePath() + "/" + Constants.CLEAN_FOLDER_NAME);
	private File securedFolder = new File(Constants.OUTPUT_FOLDER.getAbsolutePath() + "/" + Constants.SECURED_FOLDER_NAME);
	private String unsecuredUrl = "http://localhost:8080/archiva/";
	private String securedUrl = "http://localhost:8080/archiva/";
	
	public static void main(String[] args){
		Parser parser = new Parser();
		parser.parse();
	}
	
	private void parse(){
		Util.setUrlOracle(unsecuredUrl);
		parseFilesRecursively(cleanFolder, false);
		Util.setUrlOracle(securedUrl);
		parseFilesRecursively(securedFolder, true);
		List<File> cleanFiles = Util.getFiles(cleanFolder, true);
		List<File> securedFiles = Util.getFiles(securedFolder, true);
		
		if(cleanFiles.size() != securedFiles.size()){
			System.out.println("Alert! non-secured version has "
					+ cleanFiles.size() + " captured files. The secured verions has "
					+ securedFiles.size() + " captured files");
		}
		
		diffEqualFiles(cleanFiles, securedFiles);
		
		//diff(metaData);
		// do diff for multiple state files
		// perist diff file to disk
		// persist state files to disk
		
		// do diff for matching filenames
		// if applicable, dump diff to disk
		// report on filename mismatches
		
	}
	
	private void parseFilesRecursively(File folder, boolean removeToken){
		List<File> cleanFiles = Util.getFiles(folder, false);
		for(File file : cleanFiles){
			try {
				String parsedDOM;
				if(removeToken){
					System.out.println(file.getName());
					parsedDOM = removeTokenFromDOM(FileUtils.readFileToString(file));
				} else{
					parsedDOM = parseDOM(FileUtils.readFileToString(file));
				}
				FileUtils.write(file, parsedDOM);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<File> cleanDirs = Util.getDirectories(folder, false);
		for(File dir : cleanDirs){
			parseFilesRecursively(dir, removeToken);
		}
		
	}
	
	private void diffEqualFiles(List<File> cleanFiles, List<File> securedFiles){
		List<Vector<File>> tuples = new ArrayList<Vector<File>>();
		for(File cleanFile : cleanFiles){
			for(File securedFile : securedFiles){
				String relativeCleanPath = cleanFile.getAbsolutePath().substring((int)cleanFolder.getAbsolutePath().length());
				String relativeSecuredPath = securedFile.getAbsolutePath().substring((int)securedFolder.getAbsolutePath().length());
				if (relativeCleanPath.equals(relativeSecuredPath)){
					Vector<File> tuple = new Vector<File>();
					tuple.add(cleanFile);
					tuple.add(securedFile);
					tuples.add(tuple);
				}
			}
		}
		for(Vector<File> tuple : tuples){
			diff(tuple.get(0), tuple.get(1));
		}
	}
	
	private void diff(File cleanFile, File securedFile){
		try {
			String cleanData = FileUtils.readFileToString(cleanFile);
			String securedData = FileUtils.readFileToString(securedFile);
			File diffFile = createDiffFile(cleanFile);
			BasicDiff.diffLineMode(cleanData, securedData, diffFile); // writing the diff to disk
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private File createDiffFile(File originalFile){
		String path = originalFile.getParent();
		String subdir = path.substring(Constants.OUTPUT_FOLDER.getAbsolutePath().length());
		String diffDir = Constants.OUTPUT_FOLDER.getAbsolutePath() + "/diff" + subdir;
		File diffFile = new File(diffDir+"/"+originalFile.getName());
		return diffFile;
	}
	
	private String removeTokenFromDOM(String dom){
		Document doc = parseStrippedDOM(dom);
		removeTokenFromHTML(doc, true);
		parseJS(doc, true);
		return doc.outerHtml();
	}
	
	private void removeTokenFromHTML(Document doc, boolean tokenRemoval){
		Elements els = doc.select("*");
		els.traverse(new SrcVisitor());
	}
	
	private void parseJS(Document doc, boolean tokenRemoval){
		JSParser jsParser = new JSParser();
		Elements scripts = doc.getElementsByTag("script");
		for(Element script : scripts){
			if (script.hasAttr("type") && script.attr("type").contains("javascript")
					&& !script.dataNodes().isEmpty()){
				DataNode data = script.dataNodes().get(0);
				String actualData = data.attr("data");
				// convert "&lt" to "<" and such.
				// This is needed because the ChromeDriver does character escaping.
				actualData = StringEscapeUtils.unescapeHtml4(actualData);
				actualData = removeHTMLComments(actualData);
				String parsedData;
				if(tokenRemoval){
					parsedData = jsParser.removeTokens(actualData);
				} else{
					parsedData = jsParser.cleanParse(actualData);
				}
				data.attr("data", parsedData);
			}
		}
	}
	
	private Document parseStrippedDOM(String dom){
		Document doc = Jsoup.parse(dom);
		doc.getElementsByTag("head").first().getElementsByAttributeValue("src", "/archiva/struts/dojo/src/browser_debug.js").remove();
		doc.getElementsByTag("head").first().getElementsByAttributeValue("src", "/archiva/struts/dojo/src/debug.js").remove();
		doc.getElementsByTag("head").first().getElementsByAttributeValue("href", "/archiva/struts/xhtml/styles.css").remove();
		doc.getElementsByTag("head").first().getElementsByAttributeValue("src", "/archiva/struts/dojo/dojo.js").remove();
		doc.getElementsByTag("head").first().getElementsByAttributeValue("src", "/archiva/struts/simple/dojoRequire.js").remove();
		doc.getElementsByTag("body").first().getElementById("topSearchBox").getElementsByAttributeValue("src", "/archiva/struts/xhtml/validation.js").remove();
		//doc.getElementsByTag("body").first().getElementsByAttributeValue("href", "http://www.apache.org/").remove();
		//doc.getElementsByTag("body").first().getElementsByAttributeValue("href", "http://archiva.apache.org/").remove();
		Elements elements = doc.getElementsByTag("head").first().getElementsByTag("script");
		for(Element element : elements){
			if(!element.dataNodes().isEmpty() && element.dataNodes().get(0).attr("data").contains("dojo.hostenv._global_omit_module_check = false;")){
				element.remove();
			}
		}
		return doc;
	}
	
	private String removeHTMLComments(String src){
		return src.replaceAll("<!--[\\s\\S]*?-->", "");
	}
	
	private String parseDOM(String dom){
		Document doc = parseStrippedDOM(dom);
		parseJS(doc, false);
		return doc.outerHtml();
	}
	

}
