package itti.com.pl.ontology.server.ws;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import itti.com.pl.ontology.common.dto.DependenciesList;
import itti.com.pl.ontology.common.dto.MetadataObject;
import itti.com.pl.ontology.common.dto.TSINodeType;
import itti.com.pl.ontology.common.dto.TypeOfObject;

@WebService
public interface MetadataHandlerWS {

	/**
	 * sets the mode in which TSI node operates e.g. DeployableHQ, Vehicle or
	 * Dismounted. This method should be called by the TSI node when the
	 * operation mode is changed.
	 * 
	 * @param tsiNodeType
	 *            TSINodeType : {@link TSINodeType}
	 */
	public void setTSINodeType(@WebParam(name = "tsiNodeType") TSINodeType tsiNodeType);

	/**
	 * stores metadata about some object which type could be: user facing
	 * service, network, core service, whole system configuration, deployment
	 * details, topic definition etc. This method should be executed when a new
	 * object with associated metadata has been discovered.
	 * 
	 * @param typeOfObject
	 *            {@link TypeOfObject}
	 * @param metadataObject
	 *            {@link MetadataObject}
	 */
	public void registerMetadataObject(
			@WebParam(name = "typeOfObject") @XmlElement(required = true) TypeOfObject typeOfObject,
			@WebParam(name = "metadataObject") @XmlElement(required = true) MetadataObject metadataObject);

	/**
	 * refreshes the collected metadata object and check whether components
	 * represented by them are still available. This method should be called by
	 * the TSI node when it could be anticipated that metadata stored in the TSI
	 * is no longer current and should be refreshed by calling each element
	 * represented by MetadataObject.
	 * 
	 * @param typeOfObject
	 *            {@link TypeOfObject}
	 * @param metadataObject
	 *            {@link MetadataObject}
	 */
	public void updateMetadataObject(
			@WebParam(name = "typeOfObject") @XmlElement(required = true) TypeOfObject typeOfObject,
			@WebParam(name = "metadataObject") @XmlElement(required = true) MetadataObject metadataObject);

	/**
	 * searches for metadata objects which satisfy the given Query (e.g. search
	 * for BFT service having response time less than 500ms). This method should
	 * be used to discover metadata that describe components/services/networks
	 * etc. that are needed by other TSI node components.
	 * 
	 * @param typeOfObject
	 *            {@link TypeOfObject}
	 * @param query
	 *            searcu query
	 * @return list of {@link MetadataObject} matching provided criteria
	 */
	public List<MetadataObject> searchMetadata(
			@WebParam(name = "typeOfObject") @XmlElement(required = true) TypeOfObject typeOfObject,
			@WebParam(name = "query") @XmlElement(required = true) String query);

	/**
	 * gets metadata about particular object specific for the current TSI
	 * operation mode. This method should be called to get the configuration and
	 * description of particular component on the base of its name.
	 * 
	 * @param objectId
	 *            unique ID
	 * @return {@link MetadataObject}
	 */
	public MetadataObject getMetadata(@WebParam(name = "objectId") @XmlElement(required = true) String objectId);

	/**
	 * reads metadata in on-demand manner and perform determined decisions
	 * basing on them. This method should be called to get the suggestion
	 * whether the particular object could be used (fulfill its requirement on
	 * the base of knowledge stored by metadata handler or not). Method could be
	 * also used to get the appropriate version of particular service by sending
	 * the service identifier.
	 * 
	 * @param objectId
	 *            unique ID
	 * @return {@link MetadataObject}
	 */
	public MetadataObject readMetadata(@WebParam(name = "objectId") @XmlElement(required = true) String objectId);

	/**
	 * returns a list of services on which the particular service depends and
	 * which could use it. This method should be called to establish what kind
	 * of services are not used and can be terminated.
	 * 
	 * @param objectId
	 *            unique ID
	 * @return {@link DependenciesList}
	 */
	public DependenciesList getDependencies(@WebParam(name = "objectId") @XmlElement(required = true) String objectId);
}