package com.flameling.uva.thesis.validator;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class Util {
	
	private static final String csrfTokenKey = Constants.TOKEN_KEY;
	private static final String queryPathDelimiter = "?";
	private static final String queryPathParameterDelimiter = "&";
	// be aware that one single changing urlOracle will give invalid results with concurrent usage!
	private static String urlOracle;
	
	public static String getUrlOracle() {
		return urlOracle;
	}

	public static void setUrlOracle(String urlOracle) {
		Util.urlOracle = urlOracle;
	}

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
	
	public static boolean isOtherDomain(String urlString){
		return isOtherDomain(urlString, urlOracle);
	}
	
	private static boolean isOtherDomain(String urlString, String oracleString){
		boolean result = false;
		URL url = null;
		URL oracle = null;
		try {
			url = new URL(urlString);
			oracle = new URL(oracleString);
		} catch (MalformedURLException e) {
			// A malformed leaves only the option left for a relative url
			// (or a true malformed url :p), so it is definitely not another
			// domain.
			return result;
		}
		String urlHostname = url.getHost() + ":" + url.getPort();
		String oracleHostname = oracle.getHost() + ":" + oracle.getPort();
		result = !urlHostname.equals(oracleHostname);
		
		return result;
	}
	
}
