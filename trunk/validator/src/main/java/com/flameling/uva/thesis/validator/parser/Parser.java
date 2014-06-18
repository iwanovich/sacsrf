package com.flameling.uva.thesis.validator.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.flameling.uva.thesis.validator.Constants;
import com.flameling.uva.thesis.validator.diff.BasicDiff;
import com.flameling.uva.thesis.validator.parser.MetaDataEntry;;

public class Parser {
	
	public static void main(String[] args){
		Parser parser = new Parser();
		parser.parse();
	}
	
	private void parse(){
		FileNameModifier fnm = new FileNameModifier();
		List<MetaDataEntry> metaData = fnm.createMetaData(new File(Constants.OUTPUT_FOLDER.getAbsolutePath()
				+ "/secured/localhost_8080/archiva"));
		for(MetaDataEntry mde : metaData){
			mde.newFileData = removeTokenFromDOM(mde.originalFileData);
		}
		diff(metaData);
		// do diff for multiple state files
		// perist diff file to disk
		// persist state files to disk
		
		// do diff for matching filenames
		// if applicable, dump diff to disk
		// report on filename mismatches
		
	}
	
	private void diff(List<MetaDataEntry> metaData){
		for(MetaDataEntry base : metaData){
			List<MetaDataEntry> diffset = new ArrayList<MetaDataEntry>();
			diffset.add(base);
			for(MetaDataEntry mde : metaData){
				if(!base.equals(mde)){
					if (base.strippedFileName.equals(mde.strippedFileName)){
						diffset.add(mde);
					}
				}
			}
			if(diffset.size() > 1){
				String data1 = diffset.get(0).newFileData;
				String data2 = diffset.get(1).newFileData;
				File diffFile = createDiffFile(diffset.get(0).originalFile, diffset.get(0).strippedFileName);
				BasicDiff.diffLineMode(data1, data2, diffFile); // writing the diff to disk
				// still need to write away the state-files
			}
			else if(diffset.size() == 1){
				// write away the state file
			}
		}
	}
	
	private File createDiffFile(File originalFile, String newFileName){
		String path = originalFile.getParent();
		String subdir = path.substring(Constants.OUTPUT_FOLDER.getAbsolutePath().length());
		String diffDir = Constants.OUTPUT_FOLDER.getAbsolutePath() + "/inconsistent" + subdir;
		File diffFile = new File(diffDir+"/"+newFileName);
		return diffFile;
	}
	
	private String removeTokenFromDOM(String dom){
		Document doc = Jsoup.parse(dom);
		Elements els = doc.select("*");
		els.traverse(new SrcVisitor());
		return doc.outerHtml();
	}

}
