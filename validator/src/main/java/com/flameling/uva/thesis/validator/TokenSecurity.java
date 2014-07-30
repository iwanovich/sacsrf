package com.flameling.uva.thesis.validator;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.StringLiteral;

public class TokenSecurity implements SecurityMeasure {
	static public int missingTokens = 0;

	public String cleanUrl(String url) {
		return Util.stripToken(url);
	}
	
	public void parseDOM(Document doc){
		Elements els = doc.select("*");
		els.traverse(new URLTokenRemover());
	}
	
	public void parseJsAst(AstRoot ast){
		org.mozilla.javascript.ast.NodeVisitor visitor = new JSTokenRemover();
		ast.visit(visitor);
	}

	public boolean hasSecurityMeasures() {
		return true;
	}

	private class URLTokenRemover implements NodeVisitor {
		
		private static final String csrfTokenKey = Constants.TOKEN_KEY;
		private Set<Node> modifiedNodes = new HashSet<Node>();

		public URLTokenRemover() {
			// TODO Auto-generated constructor stub
		}

		public void head(Node node, int depth) {
			alterAttr(node, "src");
			alterAttr(node, "href");
			alterAttr(node, "action");
		}

		public void tail(Node node, int depth) {
			if(node == null)
				System.out.println("KNOR!!");
			removeTokenInput(node);
		}
		
		private void removeTokenInput(Node node){
			if(node instanceof Element){
				Element element = (Element) node;
				String tagName = element.tagName();
				if(tagName == null) System.out.println("BOE!!");
				if(tagName != null && tagName.equals("input")){
					String nameAttr = element.attr("name");
					if(nameAttr == null) System.out.println("BOE-2!!");
					if(nameAttr.equals(csrfTokenKey)){
						if(element.parent() != null){
							element.remove();
						}
						
					}
				}
			}
		}
		
		private void alterAttr(Node node, String attr){
			String srcValue = node.attr(attr);
			if (!srcValue.isEmpty()){
				if(srcValue.contains(csrfTokenKey)) {
					node.attr(attr, Util.stripToken(srcValue));
					modifiedNodes.add(node);
				}
				else if(!modifiedNodes.contains(node) && !srcValue.startsWith("#")
						&& !Util.isOtherDomain(srcValue)){
				node.attr(attr, srcValue + "[[MISSING_TOKEN!!]]");
				modifiedNodes.add(node);
				if(Config.getInstance().currentFile != null && Config.getInstance().currentFile.getName().contains("findArtifact.action.html"))
				missingTokens++;
				}
			}
		}

	}
	
	private class JSTokenRemover implements org.mozilla.javascript.ast.NodeVisitor{

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
