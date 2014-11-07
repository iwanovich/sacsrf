package com.flameling.uva.thesis.paspect;

public enum ResourceType {
	CSS(".css"),
	JS(".js"),
	PNG(".png"),
	JPG(".jpg"),
	JPEG(".jpeg"),
	GIF(".gif"),
	BMP(".bmp"),
	VBS(".vbs"),
	FLV(".flv"),
	SVG(".svg"),
	SWF(".swf"),
	CSV(".csv"),
	AVI(".avi"),
	MPEG(".mpeg");
	
	private String extension;
	
	ResourceType(String extension){
		extension = extension.toLowerCase();
		this.extension = extension;
	}
	
	public String getExtension() {
		return extension;
	}

	static public boolean hasExtension(String string){
		boolean result = false;
		string = string.toLowerCase();
		for(ResourceType type : ResourceType.values()){
			if (string.endsWith(type.getExtension())){
				result = true;
				break;
			}
		}
		return result;
	}
	
}
