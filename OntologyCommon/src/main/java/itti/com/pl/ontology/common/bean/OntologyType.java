package itti.com.pl.ontology.common.bean;

import org.apache.commons.lang3.StringUtils;

public enum OntologyType {

	Boolean,
	Float,
	Int,
	String,
	Date,
	DateTime,
	Time;

	public static OntologyType getType(String name) {
		for (OntologyType type : OntologyType.values()) {
			if(StringUtils.equalsIgnoreCase(type.name(), name)){
				return type;
			}
		}
		return null;
	}
}
