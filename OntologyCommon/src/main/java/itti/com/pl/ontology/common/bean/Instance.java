package itti.com.pl.ontology.common.bean;

import java.util.List;

/**
 * @author mawa
 *
 */
public class Instance {

	private OntologyClass baseClass;
	private String name;
	private List<InstanceProperty<?>> properties;

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
	public List<InstanceProperty<?>> getProperties() {
		return properties;
	}
	public void addProperty(InstanceProperty<?> property) {
		this.properties.add(property);
	}
}
