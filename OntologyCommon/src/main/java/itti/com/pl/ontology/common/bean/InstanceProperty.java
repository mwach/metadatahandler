package itti.com.pl.ontology.common.bean;

import java.util.ArrayList;
import java.util.List;

public class InstanceProperty<T> {

	private String name;
	private Class<T> type;
	private List<T> values = new ArrayList<>();

	public InstanceProperty(){}

	public InstanceProperty(String name, Class<T> type, T value){
		this.name = name;
		this.type = type;
		this.values.add(value);
	}

	public InstanceProperty(String name, Class<T> type, List<T> values){
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
	public Class<T> getType() {
		return type;
	}
	
}
