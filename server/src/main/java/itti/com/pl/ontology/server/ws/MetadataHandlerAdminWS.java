package itti.com.pl.ontology.server.ws;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface MetadataHandlerAdminWS {

	/**
	 * Returns list of all ontologies stored in the repository
	 * @return list of ontologies
	 */
	public List<String> listOntologies();
	
	/**
	 * Loads an ontology from repository and makes it active
	 * @param ontologyName name of the ontology
	 */
	public void loadOntology(@WebParam(name = "ontologyName") String ontologyName);

	/**
	 * Saves active ontology into repository
	 * @param ontologyName name of the ontology
	 */
	public void saveOntology(@WebParam(name = "ontologyName") String ontologyName);

}