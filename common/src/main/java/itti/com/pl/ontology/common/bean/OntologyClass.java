package itti.com.pl.ontology.common.bean;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OntologyClass {

	private String name;
	private String parent;

	private Set<OntologyProperty> properties = new HashSet<>();

	public OntologyClass(String className) {
		this.name = className;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<OntologyProperty> getProperties() {
		return new HashSet<>(properties);
	}
	public void add(OntologyProperty property) {
		this.properties.add(property);
	}

	public boolean hasPropertyType(String propertyName) {
		for (OntologyProperty ontologyProperty : properties) {
			if(StringUtils.equals(ontologyProperty.getName(), propertyName)){
				return true;
			}
		}
		return false;
	}

	public OntologyType getPropertyType(String propertyName) {
		for (OntologyProperty ontologyProperty : properties) {
			if(StringUtils.equals(ontologyProperty.getName(), propertyName)){
				return ontologyProperty.getType();
			}
		}
		return null;
	}

	public OntologyProperty getProperty(String propertyName) {
		for (OntologyProperty ontologyProperty : properties) {
			if(StringUtils.equals(ontologyProperty.getName(), propertyName)){
				return ontologyProperty;
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
