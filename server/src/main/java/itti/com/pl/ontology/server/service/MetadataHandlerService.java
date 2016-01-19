package itti.com.pl.ontology.server.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import itti.com.pl.ontology.common.bean.InstanceProperty;
import itti.com.pl.ontology.common.dto.MetadataObject;
import itti.com.pl.ontology.common.dto.TypeOfObject;
import itti.com.pl.ontology.core.ontology.Ontology;
import itti.com.pl.ontology.server.Constants.Class;
import itti.com.pl.ontology.server.Constants.Property;
import itti.com.pl.ontology.server.exeption.MetadataHandlerException;

public class MetadataHandlerService {

	private Ontology ontology = null;

	public MetadataHandlerService(Ontology ontology){
		this.ontology = ontology;
	}

	public void updateTsiNodeType(String tsiNodeType){
		
		List<String> instancesNames = ontology.getInstances(Class.Service.getOntologyName());
		for (String instanceName : instancesNames) {
			InstanceProperty<String> property = new InstanceProperty<>(Property.TypeOfNode.getOntologyName(), tsiNodeType);
			ontology.updateProperty(instanceName, property);
		}
	}

	public List<MetadataObject> searchMetadata(TypeOfObject typeOfObject, String query) {
		List<InstanceProperty<?>> criteria = parseCriteria(query);
		ontology.query(criteria);
		return null;	
	}

	private List<InstanceProperty<?>> parseCriteria(String query) {
		List<InstanceProperty<?>> criteria = new ArrayList<>();
		if(StringUtils.isNotEmpty(query)){
			for (String criterium : query.split("&&")){
				String[] criteriumSplit = criterium.split("=");
				if(criteriumSplit.length != 2){
					throw new MetadataHandlerException("Invalid");
				}
				String key = criteriumSplit[0];
				String value = criteriumSplit[1];
				criteria.add(new InstanceProperty<String>(key, value));
			}
		}
		return criteria;
	}
}
