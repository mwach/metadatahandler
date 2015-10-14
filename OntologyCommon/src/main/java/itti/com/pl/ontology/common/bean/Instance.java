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
	public void setProperties(List<InstanceProperty<?>> properties) {
		this.properties = properties;
	}
}
