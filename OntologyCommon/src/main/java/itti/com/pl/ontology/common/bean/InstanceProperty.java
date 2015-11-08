package itti.com.pl.ontology.common.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class InstanceProperty<T> {

	private String name;
	private OntologyType type;
	private List<T> values = new ArrayList<>();

	public InstanceProperty(){}

	public InstanceProperty(String name, OntologyType type, T value){
		this.name = name;
		this.type = type;
		this.values.add(value);
	}

	public InstanceProperty(String name, OntologyType type, Collection<T> values){
		this.name = name;
		this.type = type;
		this.values.addAll(values);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<T> getValues() {
		return values;
	}
	public OntologyType getType() {
		return type;
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
