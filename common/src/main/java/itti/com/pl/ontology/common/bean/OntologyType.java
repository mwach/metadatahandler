package itti.com.pl.ontology.common.bean;

import org.apache.commons.lang3.StringUtils;

public enum OntologyType {

	Boolean("boolean"),
	Float("float"),
	Int("int"),
	String("string"),
	Date("date"),
	DateTime("dateTime"),
	Time("time");

	String rdfType = null;

	public String getRdfType(){
		return rdfType;
	}
	
	private OntologyType(String rdfType){
		this.rdfType = rdfType;
	}

	public static OntologyType getType(String name) {
		for (OntologyType type : OntologyType.values()) {
			if(StringUtils.equalsIgnoreCase(type.getRdfType(), name)){
				return type;
			}
		}
		return null;
	}
}
