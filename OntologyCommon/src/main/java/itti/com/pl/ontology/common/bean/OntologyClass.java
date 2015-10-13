package itti.com.pl.ontology.common.bean;

import java.util.List;

public class OntologyClass {

	private String name;
	private List<OntologyClass> properties;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<OntologyClass> getProperties() {
		return properties;
	}
	public void setProperties(List<OntologyClass> properties) {
		this.properties = properties;
	}
	
	
	
}
