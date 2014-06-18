package com.flameling.uva.thesis.validator.crawler;



import java.io.File;
import java.io.FileWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.crawljax.core.CrawlerContext;
import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.state.StateVertex;
import com.flameling.uva.thesis.validator.Util;
import com.flameling.uva.thesis.validator.crawler.common.MD5;
import com.flameling.uva.thesis.validator.crawler.common.URL2File;

/**
 * HTML output plugin for Crawljax. It's an OnNewStatePlugin, so it's called
 * when the DOM changes in the browser, so you can save every state to file.
 * This plugin provides that each state will be stored only once, equality
 * testing is based on MD5 hashes. Filenames will be generated from URLs,
 * SaveHTML will create the directories just as they are in the URL. Directories
 * and files will be created within the Crawljax output directory, so you should
 * specify this in the Crawljax configuration. With this, you can store a mirror
 * of a site, but links will not be modified, so they won't work in all cases.
 * If Crawljax meets different DOM states on the same URL, SaveHTML will add a
 * counter to the end of the filename.
 * 
 * @author Zsolt Juranyi
 * @version 1.0.0
 * @see URL2File
 * @see MD5
 * 
 */
public class SaveHTMLPlugin implements OnNewStatePlugin {
	
	JFrame frame;
	
	public SaveHTMLPlugin(JFrame frame){
		this.frame = frame;
	}

	public void onNewState(CrawlerContext ctx, StateVertex vtx) {

		try {
			// query info
			//String dom = vtx.getStrippedDom(); // This method gets the dom as a single line.
			String dom = ctx.getBrowser().getStrippedDom(); // This method returns the dom including returns.
			String url = vtx.getUrl();
			String outdir = ctx.getConfig().getOutputDir().getAbsolutePath();

			// build output
			URL2File u2f = new URL2File(Util.stripToken(url), true);
			String dir = u2f.getDirectory();
			String file = u2f.getFile();
			String fn = outdir + "/" + dir + "/" + file;
			String newFilename = fn;

			// check if exists / same
			boolean same = false;
			boolean exists = new File(fn).exists();
			int counter = 0;
			String domHash = MD5.getMD5FromString(dom);

			while (exists && !same) {
				String fileHash = MD5.getMD5FromFile(newFilename);
				if (fileHash.equals(domHash)) {
					same = true;
				} else {
					counter++;
					newFilename = newFilename.substring(0, newFilename.length()-5) + "-" + counter + ".html";
					File newFile = new File(newFilename);
					exists = newFile.exists();
				}
			}
			
			

			if (!same) {
				new File(outdir + "/" + dir).mkdirs(); // create directory
				// TODO UTF-8 ?
				FileWriter fw = new FileWriter(newFilename, false);
				fw.write(dom); // write file
				fw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
