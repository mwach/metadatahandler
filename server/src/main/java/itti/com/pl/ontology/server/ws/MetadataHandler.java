package itti.com.pl.ontology.server.ws;

import itti.com.pl.ontology.common.bean.Instance;
import itti.com.pl.ontology.common.bean.OntologyClass;
import itti.com.pl.ontology.common.dto.DependenciesList;
import itti.com.pl.ontology.common.dto.MetadataObject;
import itti.com.pl.ontology.common.dto.TSINodeType;
import itti.com.pl.ontology.common.dto.TypeOfObject;
import itti.com.pl.ontology.core.ontology.Ontology;
import itti.com.pl.ontology.server.service.MetadataHandlerService;
import itti.com.pl.ontology.server.utils.ReflectionUtils;

import java.util.List;

import javax.jws.WebService;

@WebService(endpointInterface = "itti.com.pl.ontology.server.ws.MetadataHandlerWS")
public class MetadataHandler implements MetadataHandlerWS {

	private MetadataHandlerService metadataHandlerService = null;
	private Ontology ontology = null;

	/* (non-Javadoc)
	 * @see itti.com.pl.ontology.server.ws.MetadataHandlerWS#setTSINodeType(itti.com.pl.ontology.server.ws.bean.TSINodeType)
	 */
	@Override
	public void setTSINodeType(TSINodeType tsiNodeType) {
		metadataHandlerService.updateTsiNodeType(tsiNodeType.getDescription());
	}

	/* (non-Javadoc)
	 * @see itti.com.pl.ontology.server.ws.MetadataHandlerWS#registerMetadataObject(itti.com.pl.ontology.server.ws.bean.TypeOfObject, itti.com.pl.ontology.server.ws.bean.MetadataObject)
	 */
	@Override
	public void registerMetadataObject(TypeOfObject typeOfObject,
			MetadataObject metadataObject) {

		OntologyClass type = ontology.getOntologyClass(metadataObject.getType());
		Instance metadataInsance = new Instance(type, metadataObject.getName());

		ReflectionUtils.populateInstanceFromMap(metadataInsance, metadataObject.getProperties());
		ontology.createInstance(metadataInsance);
	}

	/* (non-Javadoc)
	 * @see itti.com.pl.ontology.server.ws.MetadataHandlerWS#updateMetadataObject(itti.com.pl.ontology.server.ws.bean.TypeOfObject, itti.com.pl.ontology.server.ws.bean.MetadataObject)
	 */
	@Override
	public void updateMetadataObject(TypeOfObject typeOfObject,
			MetadataObject metadataObject) {
		OntologyClass deviceClass = ontology.getOntologyClass(metadataObject.getType());
		Instance metadataInstance = new Instance(deviceClass, metadataObject.getName());

		ReflectionUtils.populateInstanceFromMap(metadataInstance, metadataObject.getProperties());
		ontology.updateInstance(metadataInstance);
	}

	/* (non-Javadoc)
	 * @see itti.com.pl.ontology.server.ws.MetadataHandlerWS#searchMetadata(itti.com.pl.ontology.server.ws.bean.TypeOfObject, java.lang.String)
	 */
	//TODO: to be implemented
	@Override
	public List<MetadataObject> searchMetadata(TypeOfObject typeOfObject, String query) {
		return metadataHandlerService.searchMetadata(typeOfObject, query);
	}

	/* (non-Javadoc)
	 * @see itti.com.pl.ontology.server.ws.MetadataHandlerWS#getMetadata(java.lang.String)
	 */
	@Override
	public MetadataObject getMetadata(String objectId) {
		return metadataHandlerService.getMetadataObject(objectId);
	}

	/* (non-Javadoc)
	 * @see itti.com.pl.ontology.server.ws.MetadataHandlerWS#readMetadata(java.lang.String)
	 */
	@Override
	public MetadataObject readMetadata(String objectId) {
		ontology.runSwrlEngine();
		return getMetadata(objectId);
	}

	/* (non-Javadoc)
	 * @see itti.com.pl.ontology.server.ws.MetadataHandlerWS#getDependencies(java.lang.String)
	 */
	//TODO: to be implemented
	@Override
	public DependenciesList getDependencies(String objectId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param manager
	 */
	public void setOntology(Ontology manager) {
		this.ontology = manager;
		this.metadataHandlerService = new MetadataHandlerService(manager);
	}


}