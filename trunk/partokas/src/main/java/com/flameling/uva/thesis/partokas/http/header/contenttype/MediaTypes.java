package com.flameling.uva.thesis.partokas.http.header.contenttype;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MediaTypes implements Set<MediaType> {
	
	private Set<MediaType> types;
	
//	public MediaTypes(MediaType[] types){
//		init(types);
//	}
	
	public MediaTypes(Set<MediaType> types){
		init(types);
	}
	
	public MediaTypes(MediaType... types){
		init(types);
	}
	
	private void init(MediaType[] types){
		Set<MediaType> typesSet = new HashSet<MediaType>(Arrays.asList(types));
		init(typesSet);
	}
	
	private void init(Set<MediaType> types){
		this.types = types;
	}

	public Set<String> getStringNotations(){
		Set<String> notations = new HashSet<String>();
		for(MediaType type : types){
			try {
				notations.addAll(type.getStringNotations());
			} catch (IllegalArgumentException e) {
				System.err.println("Something went wrong with collecting MediaTypes: " + e);
			}
		}
		return notations;
	}
	
	public int size() {
		return types.size();
	}

	public boolean isEmpty() {
		return types.isEmpty();
	}

	public boolean contains(Object o) {
		return types.contains(o);
	}

	public Iterator<MediaType> iterator() {
		return types.iterator();
	}

	public Object[] toArray() {
		return types.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return types.toArray(a);
	}

	public boolean add(MediaType e) {
		return types.add(e);
	}

	public boolean remove(Object o) {
		return types.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return types.containsAll(c);
	}

	public boolean addAll(Collection<? extends MediaType> c) {
		return types.addAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return types.retainAll(c);
	}

	public boolean removeAll(Collection<?> c) {
		return types.removeAll(c);
	}

	public void clear() {
		types.clear();
	}

	public boolean equals(Object o) {
		return types.equals(o);
	}

	public int hashCode() {
		return types.hashCode();
	}

}
