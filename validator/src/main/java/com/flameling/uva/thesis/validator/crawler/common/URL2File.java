package com.flameling.uva.thesis.validator.crawler.common;

/**
 * Useful tool for converting an URL to a filename. It builds up a directory
 * path and a filename, so you can create the directories first, then write the
 * file. Special characters will be replaced with '_'. URLs ending with '/' will
 * get an 'index.html' suffix. URL2File can force '.html' or '.htm' extension if
 * you wish, use the two-parameter constructor, and pass a true as the 2nd
 * argument. See URL2FileTest for examples, you can find it on GitHub:
 * http://juzraai.github.io/Crawljax-Plugins
 * 
 * @author Zsolt Juranyi
 * 
 */
public class URL2File {

	/*
	 * scheme://domain:port/path?query_string#fragment_id
	 * (http://en.wikipedia.org/wiki/Uniform_resource_locator)
	 */

	// input:
	private String url;
	private boolean forceHtmlExtension;
	// output:
	private String directory;
	private String file;

	/**
	 * Creates the directory name and the filename from the given URL. By
	 * default, '.html' extension will not be forced.
	 * 
	 * @param url
	 *            URL to create the path and filename from.
	 */
	public URL2File(String url) {
		this(url, false);
	}

	/**
	 * Creates the directory name and the filename from the given URL. If second
	 * argument is true, the filename will surely have '.html' or '.htm' suffix.
	 * 
	 * @param url
	 *            URL to create the path and filename from.
	 * @param forceHtmlExtension
	 *            Whether to add '.html' suffix to the filename or not.
	 */
	public URL2File(String url, boolean forceHtmlExtension) {
		this.url = url;
		this.forceHtmlExtension = forceHtmlExtension;
		build();
	}

	private void build() {
		String u = url;

		u = "/" + u.replaceFirst("^.*?:/", ""); // remove protocol

		u = u.replaceFirst("#.*$", ""); // remove fragment_id

		u = u.replaceAll("/+", "/"); // clear

		if (u.indexOf('?') >= 0) { // if there is a query string
			String[] p = u.split("\\?", 2); // make sure it won't
			u = p[0] + "_" + p[1].replaceAll("/", "_"); // create deeper folders
		}

		u = u.replaceAll("[?*:]", "_").replaceAll("_+", "_"); // clear

		int pos = u.lastIndexOf('/'); // divide the URL now

		directory = u.substring(0, pos + 1);
		// directory = directory.replaceAll("[/]", File.separator));
		// throws exception, but why?
		// it works well anyway, but it's annoying :)

		file = u.substring(pos + 1);

		if (file.isEmpty()) { // if URL is like http://domain/path/
			file = "index.html"; // generate filename: domain/path/index.html
		}

		if (forceHtmlExtension) {
			if (!file.endsWith(".html") && !file.endsWith(".htm")) {
				file = file + ".html";
			}
		}
	}

	public String getDirectory() {
		return directory;
	}

	public String getFile() {
		return file;
	}
}
