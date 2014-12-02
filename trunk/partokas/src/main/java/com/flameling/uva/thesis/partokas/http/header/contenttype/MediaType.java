package com.flameling.uva.thesis.partokas.http.header.contenttype;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

public enum MediaType {
	
	PNG("image/png"),
	JPEG("image/jpeg"),
	BMP("image/bmp"),
	GIF("image/gif"),
	CSS("text/css"),
	JS("text/javascript", "application/javascript"),
	HTML("text/html"),
	NULL("null");
	
	private Set<TypeNotation> typeNotations = new HashSet<TypeNotation>();
	
	private MediaType(String... notations){
		for(String notation : notations){
			addTypeNotation(notation);
		}
	}
	
	private void addTypeNotation(String notation){
		Validate.notNull(notation, "type notation can't be null");
		notation = notation.toLowerCase();
		notation = notation.equals("null") ? "null/null" : notation;
		typeNotations.add(new TypeNotation(notation));
	}
	
	private void addTypeNotation(GroupType group, SubType sub){
		typeNotations.add(new TypeNotation(group, sub));
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
	
	public static Set<MediaType> getMediaTypes(String group){
		Validate.notNull(group, "group can't be null");
		GroupType gt = GroupType.get(group);
		return getMediaTypes(gt);
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
		for(MediaType supportedType : MediaType.values()){
			TypeNotation tn = new TypeNotation(typeNotation);
			if (supportedType.getTypeNotations().contains(tn)){
				result = supportedType;
				break;
			}
		}
		return result;
	}
	
	
	

}
