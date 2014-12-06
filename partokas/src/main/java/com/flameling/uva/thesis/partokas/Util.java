package com.flameling.uva.thesis.partokas;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	/**
	 * The first occurence in <code>oracles</code> that matches as a path substring of <code>file</code>
	 * will be removed from <code>file</code>'s path and returned. If not one occurrence in <code>oracles</code>
	 * matches as a substring of <code>file</code>'s path, the full path of <code>file</code> is returned.
	 * @param file
	 * @param oracles
	 * @return
	 * @see #getRelativePath(String, Set)
	 */
	public static String getRelativePath(File file, Set<File> oracles){
		String filePath = file.getAbsolutePath();
		Set<String> stringOracles = new HashSet<String>();
		for(File fileOracle : oracles){
			stringOracles.add(fileOracle.getAbsolutePath());
		}
		return getRelativePath(filePath, stringOracles);
	}
	
	/**
	 * The first occurence in <code>oracles</code> that matches as a substring of <code>filePath</code>
	 * will be removed from <code>filePath</code>. If not one occurrence in <code>oracles</code>
	 * matches as a substring of <code>filePath</code>, the full <code>filePath</code> is returned.
	 * @param filePath
	 * @param oracles
	 * @return
	 */
	public static String getRelativePath(String filePath, Set<String> oracles){
		String key = filePath;
		for(String oracle : oracles){
			if(filePath.contains(oracle)){
				key = filePath.replace(oracle, "");
				break;
			}
		}
		return key;
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
	
//	public static boolean isOtherDomain(String urlString){
//		return isOtherDomain(urlString, urlOracle);
//	}
	
	public static boolean isOtherDomain(String urlString, String oracleString){
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
