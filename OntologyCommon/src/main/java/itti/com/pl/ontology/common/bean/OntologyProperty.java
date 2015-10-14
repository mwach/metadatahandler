package itti.com.pl.ontology.common.bean;

public class OntologyProperty<T> {

	private String name;
	private T type;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public T getType() {
		return type;
	}
	public void setType(T type) {
		this.type = type;
	}

	
}
