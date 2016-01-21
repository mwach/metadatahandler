package itti.com.pl.ontology.server.service;

import java.util.ArrayList;
import java.util.List;

import itti.com.pl.ontology.common.bean.Instance;
import itti.com.pl.ontology.common.bean.InstanceProperty;
import itti.com.pl.ontology.common.bean.OntologyClass;
import itti.com.pl.ontology.common.dto.MetadataObject;
import itti.com.pl.ontology.common.dto.Service;
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

	/**
	 * Updates TSI property of instances of the {@link Service} class with new value
	 * @param tsiNodeType new TSI node type
	 */
	public void updateTsiNodeType(String tsiNodeType){
		
		List<String> instancesNames = ontology.getInstances(Constants.TSI_NODE_CLASS_NAME);
		for (String instanceName : instancesNames) {
			InstanceProperty<String> property = new InstanceProperty<>(Property.TypeOfNode.getOntologyName(), tsiNodeType);
			ontology.updateProperty(instanceName, property);
		}
	}

	/**
	 * Search for {@link MetadataObject} based on provided criteria
	 * @param typeOfObject {@link TypeOfObject}
	 * @param query search criteria
	 * @return list of objects matching search criteria
	 */
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

	/**
	 * Returns a {@link MetadataObject} with given Id
	 * @param objectId Id of the object
	 * @return {@link MetadataObject}
	 */
	public MetadataObject getMetadataObject(String objectId) {
		Instance metatada = ontology.getInstance(objectId);
		OntologyClass ontologyClass = metatada.getBaseClass();
		MetadataObject object = ReflectionUtils.createInstance(ontologyClass.getName());
			object.setName(objectId);
			ReflectionUtils.populateObject(object, metatada);
			return object;
	}
}
