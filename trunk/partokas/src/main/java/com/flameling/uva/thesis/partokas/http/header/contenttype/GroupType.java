package com.flameling.uva.thesis.partokas.http.header.contenttype;


public enum GroupType{
	IMAGE("image"),
	TEXT("text"),
	APPLICATION("application"),
	NULL("null");
	
	private String text;
	
	private GroupType(String text){
		this.text = text;
	}
	
	String getText(){
		return this.text;
	}
	
//	static GroupType get(String groupNotation){
//		GroupType gt;
//		groupNotation = groupNotation.toLowerCase();
//		if(GroupType.IMAGE.getText().equals(groupNotation)){
//			gt = GroupType.IMAGE;
//		} else if(GroupType.TEXT.getText().equals(groupNotation)){
//			gt = GroupType.TEXT;
//		} else if(GroupType.APPLICATION.getText().equals(groupNotation)){
//			gt = GroupType.APPLICATION;
//		} else{
//			gt = GroupType.NULL;
//		}
//		return gt;
//	}
}
