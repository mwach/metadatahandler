package itti.com.pl.ontology.common.dto;

public enum TSINodeType {

	TN_D("Dismounted"), 
	TN_M("Mobile"), 
	TN_C("TSI custom"), 
	TN_H("Deployable HQ");

	private String description;
	
	private TSINodeType(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}
}
