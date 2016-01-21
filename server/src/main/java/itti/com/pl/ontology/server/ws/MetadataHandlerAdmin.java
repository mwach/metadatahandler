package itti.com.pl.ontology.server.ws;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.common.exception.OntologyRuntimeException;
import itti.com.pl.ontology.core.ontology.Ontology;
import itti.com.pl.ontology.core.ontology.OntologyRepository;

@WebService(endpointInterface = "itti.com.pl.ontology.server.ws.MetadataHandlerAdminWS")
public class MetadataHandlerAdmin implements MetadataHandlerAdminWS{

	private static final Logger LOGGER = LoggerFactory.getLogger(MetadataHandlerAdmin.class);

	private OntologyRepository repository = null;
	private Ontology ontology = null;
	
	public List<String> listOntologies(){
		try {
			return repository.getListOfOntologies();
		} catch (OntologyException e) {
			LOGGER.warn("Could not retrieve a list of ontologies", e);
			throw new OntologyRuntimeException(e.getLocalizedMessage());
		}
	}
	
	public void loadOntology(@WebParam(name = "ontologyName") String ontologyName){
		try {
			Ontology newOntology = repository.loadOntology(ontologyName);
			ontology.updateModel(newOntology);
		} catch (OntologyException e) {
			LOGGER.warn("Could not load a specified ontology", e);
			throw new OntologyRuntimeException(e.getLocalizedMessage());
		}
	}

	public void saveOntology(@WebParam(name = "ontologyName") String fileName){
		try {
			repository.saveOntology(ontology, fileName);
		} catch (OntologyException e) {
			LOGGER.warn("Could not save a specified ontology", e);
			throw new OntologyRuntimeException(e.getLocalizedMessage());
		}
		
	}

	public void setRepository(OntologyRepository ontologyRepository) {
		this.repository = ontologyRepository;
	}

	public void setOntology(Ontology ontology) {
		this.ontology = ontology;
	}
}