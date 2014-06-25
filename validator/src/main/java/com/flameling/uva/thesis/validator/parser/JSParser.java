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

import com.flameling.uva.thesis.validator.Constants;
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
		
		JSParser parser = new JSParser();
		parser.something(js);
	}
	
	private void something(String src){
		AstRoot ast = parse(src);
		NodeVisitor visitor = new JSTokenRemover();
		ast.visit(visitor);
		System.out.println(ast.toSource());
	}
	
	private static AstRoot parse(String src){
		return parse(src, 0);
	}
	
	private static AstRoot parse(String src, int retries){
		AstRoot ast = null;
		try {
			ast = parseRoot(src, 0);
			String output = ast.toSource();
			System.out.println(output);
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
			}
		}
		return ast;
	}
	
	// TODO bronvermelding, anders kan het gezien worden als plagiaat.
	// Ram Kulkarni, http://ramkulkarni.com/blog/understanding-ast-created-by-mozilla-rhino-parser/
	private static AstRoot parseRoot(String src, int startLineNum)
			throws IOException {

			CompilerEnvirons env = new CompilerEnvirons();
			env.setRecoverFromErrors(true);
			env.setGenerateDebugInfo(true);
			env.setRecordingComments(true);

			StringReader strReader = new StringReader(src);

			IRFactory factory = new IRFactory(env);
			return factory.parse(strReader, null, startLineNum);

		}
	
	private class JSTokenRemover implements NodeVisitor{

		public boolean visit(AstNode node) {
			if(node instanceof StringLiteral){
				StringLiteral stringNode = (StringLiteral) node;
				String value = stringNode.getValue();
				if(value != null && value.contains(Constants.TOKEN_KEY)){
					value = Util.stripToken(value);
					stringNode.setValue(value);
				}
			}
			return true;
		}
		
	}

}
