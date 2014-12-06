package com.flameling.uva.thesis.partokas.http.header.contenttype;

import java.util.HashSet;
import java.util.Set;

public enum MediaType {
	
	PNG(new TypeNotation(GroupType.IMAGE,SubType.PNG)),
	JPEG(new TypeNotation(GroupType.IMAGE,SubType.JPEG)),
	BMP(new TypeNotation(GroupType.IMAGE,SubType.BMP)),
	GIF(new TypeNotation(GroupType.IMAGE,SubType.GIF)),
	CSS(new TypeNotation(GroupType.TEXT,SubType.CSS)),
	JS(new TypeNotation(GroupType.TEXT,SubType.JAVASCRIPT),
			new TypeNotation(GroupType.APPLICATION,SubType.JAVASCRIPT)),
	HTML(new TypeNotation(GroupType.TEXT,SubType.HTML)),
	NULL(new TypeNotation(GroupType.NULL,SubType.NULL));
	
	private Set<TypeNotation> typeNotations = new HashSet<TypeNotation>();
	
	private MediaType(TypeNotation... notations){
		for(TypeNotation notation : notations){
			typeNotations.add(notation);
		}
	}
	
	private Set<TypeNotation> getTypeNotations(){
		return this.typeNotations;
	}
	
	Set<String> getStringNotations(){
		Set<String> result = new HashSet<String>();
		for(TypeNotation notation : getTypeNotations()){
			result.add(notation.getStringNotation());
		}
		return result;
	}
	
	public static Set<MediaType> getMediaTypes(GroupType group){
		Set<MediaType> types = new HashSet<MediaType>();
		for(MediaType ct : MediaType.values()){
			if(ct.getGroups().contains(group)){
				types.add(ct);
			}
		}
		return types;
	}
	
	public Set<GroupType> getGroups(){
		Set<GroupType> groups = new HashSet<GroupType>();
		for(TypeNotation tn : getTypeNotations()){
			groups.add(tn.getGroup());
		}
		return groups;
	}
	
	static public boolean supportsTypeNotation(String typeNotation){
		boolean result = false;
		if(getMediaType(typeNotation) != MediaType.NULL)
			result = true;
		return result;
	}
	
	static public MediaType getMediaType(String typeNotation){
		MediaType result = MediaType.NULL;
		if(typeNotation == null)
			return MediaType.NULL;
		typeNotation = typeNotation.toLowerCase();
		MediaType[] supportedTypes = MediaType.values();
		for(MediaType supportedType : supportedTypes){
			if(supportedType.getStringNotations().contains(typeNotation)){
				result = supportedType;
				break;
			}
		}
		return result;
	}
	
	
	

}
