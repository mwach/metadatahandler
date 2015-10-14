package itti.com.pl.ontology.common.bean;

import java.util.List;

public class OntologyClass {

	private String name;
	private List<OntologyProperty<?>> properties;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<OntologyProperty<?>> getProperties() {
		return properties;
	}
	public void setProperties(List<OntologyProperty<?>> properties) {
		this.properties = properties;
	}
	
	
	
}
