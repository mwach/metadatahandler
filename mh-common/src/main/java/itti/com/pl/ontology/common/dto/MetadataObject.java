package itti.com.pl.ontology.common.dto;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class MetadataObject {

	// eg laptop
	private String type;
	//eg laptop_1
	private String name;

	private Map<String, String> properties = new HashMap<>();

	public MetadataObject(){
		setType(this.getClass().getSimpleName());
	}

	@XmlElement(required=true, nillable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlElement(required=true, nillable=false)
	public String getType() {
		return type;
	}
	private void setType(String type) {
		this.type = type;
	}

	protected void setProperty(String name, String value){
		properties.put(name, value);
	}

	protected String getProperty(String name){
		return properties.get(name);
	}

	@XmlElement(required=false)
	public void setProperties(Map<String, String> properties){
		this.properties.putAll(properties);
	}

	public Map<String, String> getProperties(){
		return new HashMap<>(properties);
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
