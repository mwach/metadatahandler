package itti.com.pl.ontology.server.utils;

public enum OntologyClass{
	Service("Service"), 
	Device("Device"),
	Network("Network");
	
	private String ontologyName;

	private OntologyClass(String ontologyName){
		this.ontologyName = ontologyName;
	}
	public String getOntologyName(){
		return ontologyName;
	}
}
