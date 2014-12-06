package com.flameling.uva.thesis.partokas.http.header.contenttype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

public class ContentType {

	
	/**
	 * Determines what the MediaType is that is associated with <code>contentType</code>
	 * @param contentType representing the HTTP contentType header. example:
	 * "text/html;charset=UTF-8". The corresponding MediaType would be {@link MediaType#HTML}
	 * @return the corresponding {@link MediaType} or {@link MediaType#NULL} if the
	 * MediaType could not be determined from <code>contentType</code>.
	 */
	public static MediaType getMediaType(String contentType){
		MediaType ct = MediaType.NULL;
		if(contentType != null && !contentType.isEmpty()){
			String[] contentTypes;
			contentTypes = contentType.split(";");
			List<String> types = Arrays.asList(contentTypes);
			MediaTypes allTypes = new MediaTypes(MediaType.values());
			Set<String> knownTypes = allTypes.getStringNotations();
			if(!Collections.disjoint(types, knownTypes)){
				//do parsing
				Collection<String> intersect  = CollectionUtils.intersection(types, knownTypes);
				String type = intersect.iterator().next();
				ct = MediaType.getMediaType(type);
			}
		}
		return ct;
	}
	
}
