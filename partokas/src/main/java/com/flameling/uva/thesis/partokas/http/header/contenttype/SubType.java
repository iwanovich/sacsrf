package com.flameling.uva.thesis.partokas.http.header.contenttype;

enum SubType{
	PNG("png"),
	JPEG("jpeg"),
	BMP("bmp"),
	GIF("gif"),
	CSS("css"),
	JAVASCRIPT("javascript"),
	HTML("html"),
	NULL("null");
	
	private String text;
	
	private SubType(String text){
		this.text = text;
	}
	
	String getText(){
		return this.text;
	}

}
