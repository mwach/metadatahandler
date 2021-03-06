package itti.com.pl.ontology.common.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OntologyProperty {

	private String name;
	private OntologyType type;
	private String range;

	public OntologyProperty(){}

	public OntologyProperty(String name, OntologyType type){
		this.name = name;
		this.type = type;
	}

	public OntologyProperty(String name, OntologyType type, String range){
		this.name = name;
		this.type = type;
		this.range = range;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public OntologyType getType() {
		return type;
	}

	public void setType(OntologyType type) {
		this.type = type;
	}

	public String getRange() {
		return range;
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

}
