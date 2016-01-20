package itti.com.pl.ontology.server.service;

import java.util.ArrayList;
import java.util.List;

import itti.com.pl.ontology.common.bean.Instance;
import itti.com.pl.ontology.common.bean.InstanceProperty;
import itti.com.pl.ontology.common.bean.OntologyClass;
import itti.com.pl.ontology.common.dto.MetadataObject;
import itti.com.pl.ontology.common.dto.TypeOfObject;
import itti.com.pl.ontology.core.ontology.Ontology;
import itti.com.pl.ontology.server.Constants;
import itti.com.pl.ontology.server.Constants.Property;
import itti.com.pl.ontology.server.utils.CriteriaUtils;
import itti.com.pl.ontology.server.utils.ReflectionUtils;

public class MetadataHandlerService {

	private Ontology ontology = null;

	public MetadataHandlerService(Ontology ontology){
		this.ontology = ontology;
	}

	public void updateTsiNodeType(String tsiNodeType){
		
		List<String> instancesNames = ontology.getInstances(Constants.TSI_NODE_CLASS_NAME);
		for (String instanceName : instancesNames) {
			InstanceProperty<String> property = new InstanceProperty<>(Property.TypeOfNode.getOntologyName(), tsiNodeType);
			ontology.updateProperty(instanceName, property);
		}
	}

	public List<MetadataObject> searchMetadata(TypeOfObject typeOfObject, String query) {
		List<InstanceProperty<?>> criteria = parseCriteria(query);
		List<String> instances = ontology.query(criteria);
		List<MetadataObject> metadataObjects = new ArrayList<>();
		for (String instance : instances) {
			metadataObjects.add(getMetadataObject(instance));
		}
		return metadataObjects;
	}

	private List<InstanceProperty<?>> parseCriteria(String query) {
		List<InstanceProperty<?>> criteria = CriteriaUtils.parseCriteria(query);
		return criteria;
	}

	public MetadataObject getMetadataObject(String objectId) {
		Instance metatada = ontology.getInstance(objectId);
		OntologyClass ontologyClass = metatada.getBaseClass();
		MetadataObject object = ReflectionUtils.createInstance(ontologyClass.getName());
			object.setName(objectId);
			object.setType(ontologyClass.getName());
			ReflectionUtils.populateObject(object, metatada);
			return object;
	}
}
