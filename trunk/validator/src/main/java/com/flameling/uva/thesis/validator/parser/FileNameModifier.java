package com.flameling.uva.thesis.validator.parser;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileNameModifier {
	
	List<File> files;
	File rootFolder;
	
	FileNameModifier(File rootFolder){
		this.rootFolder = rootFolder.getAbsoluteFile();
	}
	
	public static void main(String[] args){
		FileNameModifier fnm = new FileNameModifier(new File("htmloutput-secured/localhost_8080/archiva"));
		fnm.printRootFiles();
	}
	
	private void printRootFiles(){
		for(File file : getFiles(rootFolder, false)){
			System.out.println(removeTokenFromFileName(file.getName()));
		}
	}
	
	private String removeTokenFromFileName(String fileName){
		String tokenKey = ("com.flameling.SACSRF.CSRF_NONCE");
		String result = fileName;
		if (fileName.contains(tokenKey)) {
			String tokenKeyRegex = tokenKey.replaceAll("\\.", "\\\\.");
			String tokenValueRegex = "[A-Z[0-9]]{32}";
			Character prefix = fileName.charAt(fileName.indexOf(tokenKey) - 1);
			Character postfix = fileName.charAt(fileName.indexOf(tokenKey) + tokenKey.length() + 33);
			String preRegex = "";
			String postRegex = "";
			if (!postfix.toString().equals("&") && !postfix.toString().equals("_")) {
				preRegex = "[_&]";
			} else if (postfix.toString().equals("&") || postfix.toString().equals("_")) {
				// a parameter is following
				if (postfix.toString().equals(prefix)) { //we don't need two delimiters
					preRegex = prefix.toString(); // prefix can be removed
				} else if (postfix.toString().equals("&")) {
					postRegex = postfix.toString();
				} else if (postfix.toString().equals("_")) {
					preRegex = "&";
				}
			}
			String regex = preRegex + tokenKeyRegex + "=" + tokenValueRegex + postRegex;
			result = fileName.replaceFirst(regex, "");
		}
		return result;
	}
	
	private List<File> getFiles(File folder, boolean recursive){
		if(recursive)
			return getFilesRecursively(folder);
		else
			return getFiles(folder);
	}
	
	private List<File> getFilesRecursively(File folder){
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
	
	private List<File> getDirectories(File folder, boolean recursive){
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
	
	private List<File> getFiles(File folder){
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
