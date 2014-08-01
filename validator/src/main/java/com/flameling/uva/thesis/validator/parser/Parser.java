package com.flameling.uva.thesis.validator.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.flameling.uva.thesis.validator.AnalysisResults;
import com.flameling.uva.thesis.validator.ArchivaConfig;
import com.flameling.uva.thesis.validator.Config;
import com.flameling.uva.thesis.validator.Constants;
import com.flameling.uva.thesis.validator.FileAnalysisResult;
import com.flameling.uva.thesis.validator.TokenSecurity;
import com.flameling.uva.thesis.validator.Util;
import com.flameling.uva.thesis.validator.diff.DiffLineCounter;

public class Parser {
	
	public static Set<File> srcFolders;
	private File cleanFolder = new File(Config.getInstance().getOutputFolder().getAbsolutePath() + "/" + Constants.CLEAN_FOLDER_NAME);
	private File securedFolder = new File(Config.getInstance().getOutputFolder().getAbsolutePath() + "/" + Constants.SECURED_FOLDER_NAME);
	private String unsecuredUrl = "http://localhost:8080/archiva/";
	private String securedUrl = "http://localhost:8080/archiva/";
	
	public static void main(String[] args){
		Config.setInstance(new ArchivaConfig());
		Config.getInstance().getSecurityMeasures().add(new TokenSecurity());
		Parser parser = new Parser();
		parser.parse();
	}
	
	public void parse(){
		srcFolders = new HashSet<File>();
		srcFolders.add(cleanFolder);
		srcFolders.add(securedFolder);
		Config.getInstance().setSecurityMeasureBlock(true);
		Util.setUrlOracle(unsecuredUrl);
		parseFilesRecursively(cleanFolder);
		Config.getInstance().setSecurityMeasureBlock(false);
		Util.setUrlOracle(securedUrl);
		parseFilesRecursively(securedFolder);
		List<File> cleanFiles = Util.getFiles(cleanFolder, true);
		List<File> securedFiles = Util.getFiles(securedFolder, true);
		checkFileCount(cleanFiles, securedFiles);
		diffEqualFiles(cleanFiles, securedFiles);
		
	}
	
	private void checkFileCount(List<File> cleanFiles, List<File> securedFiles){
		if(cleanFiles.size() != securedFiles.size()){
			System.out.println("Alert! non-secured version has "
					+ cleanFiles.size() + " captured files. The secured verions has "
					+ securedFiles.size() + " captured files");
		}
	}
	
	private void parseFilesRecursively(File folder){
		List<File> files = Util.getFiles(folder, false);
		for(File file : files){
			try {
				String source = FileUtils.readFileToString(file);
				setAnalysisEntry(file);
				Config.getInstance().currentFile = file;
				String parsedDOM = parseDOM(source, file);
				FileUtils.write(file, parsedDOM);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<File> dirs = Util.getDirectories(folder, false);
		for(File dir : dirs){
			parseFilesRecursively(dir);
		}
		
	}
	
	private void setAnalysisEntry(File file){
		AnalysisResults ar = Config.getInstance().getAnalysisResults();
		String key = Util.getRelativePath(file, srcFolders);
		FileAnalysisResult far = ar.get(key);
		ar.put(key, far);
		ar.setCurrentFileAnalysis(far);
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
			setAnalysisEntry(cleanFile);
			String cleanData = FileUtils.readFileToString(cleanFile);
			String securedData = FileUtils.readFileToString(securedFile);
			File diffFile = createDiffFile(cleanFile);
			DiffLineCounter dlc = new DiffLineCounter(cleanData, securedData);
			dlc.writeDiffToFile(diffFile); // writing the diff to disk
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private File createDiffFile(File originalFile){
		String path = originalFile.getParent();
		String subdir = path.substring(Config.getInstance().getOutputFolder().getAbsolutePath().length());
		String diffDir = Config.getInstance().getOutputFolder().getAbsolutePath() + "/diff" + subdir;
		File diffFile = new File(diffDir+"/"+originalFile.getName());
		return diffFile;
	}
	
	private void parseJS(Document doc){
		Elements scripts = doc.getElementsByTag("script");
		JSParser jsParser = new JSParser();
		for(Element script : scripts){
			if (script.hasAttr("type") && script.attr("type").contains("javascript")
					&& !script.dataNodes().isEmpty()){
				DataNode data = script.dataNodes().get(0);
				String actualData = data.attr("data");
				// convert "&lt" to "<" and such.
				// This is needed because the ChromeDriver does character escaping.
				actualData = StringEscapeUtils.unescapeHtml4(actualData);
				actualData = removeHTMLComments(actualData);
				String parsedData = jsParser.parseJSData(actualData);
				data.attr("data", parsedData);
			}
		}
	}
	
	private Document parseStrippedDOM(String dom){
		Document doc = Jsoup.parse(dom);
		Config.getInstance().stripDom(doc);
		return doc;
	}
	
	private String removeHTMLComments(String src){
		return src.replaceAll("<!--[\\s\\S]*?-->", "");
	}
	
	private String parseDOM(String dom, File currentFile){
		Document doc = parseStrippedDOM(dom);
		Config.getInstance().getSecurityMeasures().parseDOM(doc, currentFile);
		parseJS(doc);
		doc.outputSettings().prettyPrint(false);
		return doc.outerHtml();
	}
	

}
