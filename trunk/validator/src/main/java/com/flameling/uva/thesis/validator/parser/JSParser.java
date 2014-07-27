package com.flameling.uva.thesis.validator.parser;

import java.io.IOException;
import java.io.StringReader;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.IRFactory;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.StringLiteral;

import com.flameling.uva.thesis.validator.ArchivaConfig;
import com.flameling.uva.thesis.validator.Config;
import com.flameling.uva.thesis.validator.Constants;
import com.flameling.uva.thesis.validator.TokenSecurity;
import com.flameling.uva.thesis.validator.Util;

public class JSParser {
	
	public static void main(String[] args){
		String js = "&lt;!--\n"
				+ "function removeFiletypePattern(filetypeId, pattern)"
			+"  {"
			+"     var f = document.getElementById('filetypeForm');"
			+"    "
			+"     f.action = \"/archiva/admin/repositoryScanning!removeFiletypePattern.action?com.flameling.SACSRF.CSRF_NONCE=83CEA57978CC262373DC6C101B1CC6D1\";"
			+"     f['pattern'].value = pattern;"
			+"     f['fileTypeId'].value = filetypeId;"
			+"     f.submit();"
			+"  }"
			+"  "
			+"  function addFiletypePattern(filetypeId, newPatternId)"
			+"  {"
			+"     var f = document.forms['filetypeForm'];"
			+"          "
			+"     f.action = \"/archiva/admin/repositoryScanning!addFiletypePattern.action?com.flameling.SACSRF.CSRF_NONCE=83CEA57978CC262373DC6C101B1CC6D1\";"     
			+"     f.elements['pattern'].value = document.getElementById(newPatternId).value;"
			+"     f.elements['fileTypeId'].value = filetypeId;"
			+"     f.submit();"
			+"  }"
			+"//--&gt;";
		
		String js2 = "function handleChecksum()"
        +"{"
        +"  if ( f.value )"
        +"  {"
        +"     var f = document.getElementById('filetypeForm');"
        +"    if ( f.value.indexOf(\"/\") &gt;= 0 || f.value.indexOf(\"\\\") &gt;= 0)"
        +"    {"
        +"      var s = document.ChecksumApplet.generateMd5( f.value );"
        +"      // If there is a space, it's an error message, not a checksum"
        +"      if ( s.indexOf(\" \") &gt;= 0 )"
        +"      {"
        +"        alert(s);"
        +"      }"
        +"      else"
        +"      {"
        +"        document.checksumSearch.q.value = s;"
        +"      }"
        +"    }"
        +"    else if ( f.files[0].getAsBinary )"
        +"    {"
        +"      document.checksumSearch.q.value = hex_md5(f.files[0].getAsBinary());"
        +"    }"
        +"    else"
        +"    {"
        +"        alert('This browser is not supported');"
        +"    }"
        +"  }"
        +"}";
		
		JSParser parser = new JSParser();
		Config.setInstance(new ArchivaConfig());
		Config.getInstance().getSecurityMeasures().add(new TokenSecurity());
		String output = parser.parseJSData(js2);
		System.out.println(output);
	}
	
	public String parseJSData(String src){
		AstRoot ast = parse(src);
		Config.getInstance().getSecurityMeasures().parseJsAst(ast);
		String result = ast.toSource();
		return result;
	}
	
	private AstRoot parse(String src){
		return parse(src, 0);
	}
	
	private AstRoot parse(String src, int retries){
		AstRoot ast = null;
		try {
			ast = parseRoot(src, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EvaluatorException e){
			// if the line of source code, causing the exception, is available
			// and it is at the beginning of the script -> we remove a maximum of 2
			// lines of code, hoping to eliminate any fluff preceding the acutal
			// javascript code.
			if(e.lineSource() != null && src.startsWith(e.lineSource()) && retries < 2){
				String regex = "\\Q" + e.lineSource() + "\\E";
				src = src.replaceFirst(regex, "");
				ast = parse(src, retries++);
			} else{
				System.out.println(e.lineSource());
				throw e;
			}
		}
		return ast;
	}
	
	// TODO bronvermelding, anders kan het gezien worden als plagiaat.
	// Ram Kulkarni, http://ramkulkarni.com/blog/understanding-ast-created-by-mozilla-rhino-parser/
	private AstRoot parseRoot(String src, int startLineNum)
			throws IOException {

			CompilerEnvirons env = new CompilerEnvirons();
			env.setRecoverFromErrors(true);
			env.setGenerateDebugInfo(true);
			env.setRecordingComments(true);

			StringReader strReader = new StringReader(src);

			IRFactory factory = new IRFactory(env);
			return factory.parse(strReader, null, startLineNum);

		}

}
