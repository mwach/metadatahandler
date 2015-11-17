package itti.com.pl.ontology.common.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OntologyClass {

	private String name;
	private String parent;

	private List<OntologyProperty> properties = new ArrayList<>();

	public OntologyClass(String className) {
		this.name = className;
	}
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
	public OntologyType getPropertyType(String propertyName) {
		for (OntologyProperty ontologyProperty : properties) {
			if(StringUtils.equals(ontologyProperty.getName(), propertyName)){
				return ontologyProperty.getType();
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	public void setParentClass(String parent) {
		this.parent = parent;
	}
	public String getParent(){
		return parent;
	}

}
