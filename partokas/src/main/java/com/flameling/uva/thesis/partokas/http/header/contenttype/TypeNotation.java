package com.flameling.uva.thesis.partokas.http.header.contenttype;

class TypeNotation{
	private GroupType group;
	private SubType sub;
	private final String delimiter = "/";
	
	TypeNotation(GroupType group, SubType sub){
		init(group, sub);
	}
	
	public TypeNotation(String notation){
		init(notation);
	}
	
	private void init(GroupType gt, SubType st){
		this.group = gt;
		this.sub = st;
	}
	
	private void init(String notation){
		String[] splits = notation.split(delimiter);
		if(splits.length != 2)
			throw new IllegalArgumentException("MediaType notation should be in the form of \"image/png\".");
		String groupNotation = splits[0];
		String subNotation = splits[1];
		GroupType gt = GroupType.get(groupNotation);
		SubType st = SubType.get(subNotation);
		init(gt, st);
	}
	
	String getStringNotation(){
		String result = null;
		result = group.getText() + delimiter + sub.getText();
		if(group.equals(GroupType.NULL) || sub.equals(SubType.NULL)){
			result = "null";
		}
		return result;
	}
	
	GroupType getGroup() {
		return group;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((delimiter == null) ? 0 : delimiter.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((sub == null) ? 0 : sub.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeNotation other = (TypeNotation) obj;
		if (delimiter == null) {
			if (other.delimiter != null)
				return false;
		} else if (!delimiter.equals(other.delimiter))
			return false;
		if (group != other.group)
			return false;
		if (sub != other.sub)
			return false;
		return true;
	}

//	@Override
//	public boolean equals(Object obj){
//		boolean result;
//		if(obj instanceof TypeNotation){
//			TypeNotation tn = (TypeNotation) obj;
//			if(this.group.getText().equals(tn.group.getText())
//					&& this.sub.getText().equals(tn.sub.getText()))
//				result = true;
//			else
//				result = false;
//		} else{
//			result = super.equals(obj);
//		}
//		return result;
//	}
	
	
	
	
}
