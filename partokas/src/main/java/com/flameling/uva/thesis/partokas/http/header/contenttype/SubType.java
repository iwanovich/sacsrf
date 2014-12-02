package com.flameling.uva.thesis.partokas.http.header.contenttype;

enum SubType{
	PNG("png"),
	JPEG("jpeg"),
	BMP("bmp"),
	GIF("gif"),
	CSS("css"),
	JAVASCRIPT("javascript"),
	HTML("HTML"),
	NULL("null");
	
	private String text;
	
	private SubType(String text){
		this.text = text;
	}
	
	String getText(){
		return this.text;
	}
	
	static SubType get(String subNotation){
		SubType st;
		if(SubType.PNG.getText().equals(subNotation)){
			st = SubType.PNG;
		} else if(SubType.JPEG.getText().equals(subNotation)){
			st = SubType.JPEG;
		} else{
			st = SubType.NULL;
		}
		return st;
	}
}
