package itti.com.pl.ontology.common.bean;

import java.util.ArrayList;
import java.util.List;

public class OntologyClass {

	private String name;
	private List<OntologyProperty> properties = new ArrayList<>();

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<OntologyProperty> getProperties() {
		return new ArrayList<>(properties);
	}
	public void add(OntologyProperty property) {
		this.properties.add(property);
	}
	
	
	
}
