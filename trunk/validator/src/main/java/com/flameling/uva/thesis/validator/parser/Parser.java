package com.flameling.uva.thesis.validator.parser;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.flameling.uva.thesis.validator.Constants;
import com.flameling.uva.thesis.validator.parser.MetaDataEntry;;

public class Parser {
	
	private void parse(){
		FileNameModifier fnm = new FileNameModifier(Constants.OUTPUT_FOLDER.getAbsoluteFile());
		List<MetaDataEntry> metaData = fnm.createMetaData();
		for(MetaDataEntry mde : metaData){
			mde.newFileData = removeTokenFromDOM(mde.originalFileData);
		}
		// do diff for multiple state files
		// perist diff file to disk
		// persist state files to disk
		
		// do diff for matching filenames
		// if applicable, dump diff to disk
		// report on filename mismatches
		
	}
	
	private String removeTokenFromDOM(String dom){
		Document doc = Jsoup.parse(dom);
		Elements els = doc.select("*");
		els.traverse(new SrcVisitor());
		return doc.outerHtml();
	}

}
