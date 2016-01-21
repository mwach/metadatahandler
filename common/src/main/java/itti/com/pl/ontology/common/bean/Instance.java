package itti.com.pl.ontology.common.bean;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author mawa
 *
 */
public class Instance {

	private OntologyClass baseClass;
	private String name;
	private Set<InstanceProperty<?>> properties = new HashSet<InstanceProperty<?>>();

	public Instance(){}
	
	public Instance(OntologyClass baseClass, String name){
		this.baseClass = baseClass;
		this.name = name;
	}

	public OntologyClass getBaseClass() {
		return baseClass;
	}
	public void setBaseClass(OntologyClass baseClass) {
		this.baseClass = baseClass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<InstanceProperty<?>> getProperties() {
		return new HashSet<>(properties);
	}
	public void addProperty(InstanceProperty<?> property) {
		this.properties.add(property);
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
