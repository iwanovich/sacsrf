package com.flameling.uva.thesis.validator.parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

public class TrRunner {
	
	public static void main(String[] args){
		File originalHtml = new File("/home/iwan/workspace/corcrawl/htmloutput/localhost_8080/archiva/index.action.html");
		File dirtyHtml = new File("/home/iwan/workspace/corcrawl/htmloutput/localhost_8080/archiva/index.action_dirty.html");
		File cleanHtml = new File("/home/iwan/workspace/corcrawl/htmloutput/localhost_8080/archiva/index.action_clean.html");
		try {
			Document doc = Jsoup.parse(originalHtml, "UTF-8");
			FileWriter dirtyWriter = new FileWriter(dirtyHtml);
			dirtyWriter.write(doc.outerHtml());
			dirtyWriter.close();
			
			Elements els = doc.select("*");
			els.traverse(new TokenURLCleaner());
			//el.attr("href", "Ik ben Iwan :)");
			
			System.out.println(doc.outerHtml());
			FileWriter cleanWriter = new FileWriter(cleanHtml);
			cleanWriter.write(doc.outerHtml());
			cleanWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public TrRunner() {
		// TODO Auto-generated constructor stub
	}

}
