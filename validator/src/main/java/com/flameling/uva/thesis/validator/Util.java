package com.flameling.uva.thesis.validator;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class Util {
	
	private static final String csrfTokenKey = Constants.TOKEN_KEY;
	private static final String queryPathDelimiter = "?";
	private static final String queryPathParameterDelimiter = "&";
	
	public static String stripToken(String url){
		String result = url;
		String csrfToken = "";
		String queryPath = StringUtils.substringAfter(url, queryPathDelimiter);
		String[] splits = StringUtils.split(queryPath, queryPathParameterDelimiter);
		for(String part : splits){
			if(part.contains(csrfTokenKey)){
				csrfToken = part;
			}
		}
		if(!csrfToken.isEmpty()){
			splits = ArrayUtils.remove(splits, ArrayUtils.indexOf(splits, csrfToken));
		}
		String urlBase = StringUtils.substringBefore(url, queryPathDelimiter);
		String newQueryPath = StringUtils.join(splits, queryPathParameterDelimiter);
		if(newQueryPath.isEmpty()){
			result = urlBase;
		} else{
			result = urlBase + queryPathDelimiter + newQueryPath;
		}
		
		return result;
	}
	
	public static List<File> getFiles(File folder, boolean recursive){
		if(recursive)
			return getFilesRecursively(folder);
		else
			return getFiles(folder);
	}
	
	private static List<File> getFilesRecursively(File folder){
		List<File> allFiles = new ArrayList<File>();
		for(File file : folder.listFiles()){
			if(file.isDirectory()){
				allFiles.addAll(getFilesRecursively(file));
			}
			else if(file.isFile()) {
				allFiles.add(file);
			}
		}
		return allFiles;
	}
	
	public static List<File> getDirectories(File folder, boolean recursive){
		List<File> dirs = new ArrayList<File>();
		for(File file : folder.listFiles(new FileFilter() {
			
			public boolean accept(File pathname) {
				if (pathname.isDirectory())
					return true;
					else return false;
			}
		})){
			dirs.add(file);
			if(recursive)
				dirs.addAll(getDirectories(file, recursive));
		}
		return dirs;
	}
	
	private static List<File> getFiles(File folder){
		File[] fileArray = folder.listFiles(new FileFilter() {
			
			public boolean accept(File pathname) {
				if (pathname.isFile())
					return true;
					else return false;
			}
		});
		return new ArrayList<File>(Arrays.asList(fileArray));
	}
	
}
