package itti.com.pl.ontology.core.ontology;

import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.common.exception.OntologyRuntimeException;
import itti.com.pl.ontology.core.exception.ErrorMessages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;

/**
 * Local ontology repository
 * 
 * @author cm-admin
 * 
 */
public class LocalOntologyRepository implements OntologyRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalOntologyRepository.class);
	private static final String ONTOLOGY_SUFFIX = "owl";

	// location, where all the ontologies are stored
	private String repositoryLocation = null;

	/**
	 * Sets ontology repository location
	 * 
	 * @param repositoryLocation
	 *            location of the ontology repository
	 */
	public void setRepositoryLocation(String repositoryLocation) {
		this.repositoryLocation = repositoryLocation;
	}

	private String getRepositoryLocation() {
		return repositoryLocation;
	}

	/* (non-Javadoc)
	 * @see itti.com.pl.ontology.core.ontology.OntologyRepository#saveOntology(edu.stanford.smi.protegex.owl.jena.JenaOWLModel, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void saveOntology(Ontology model, String fileName) throws OntologyException {

		LOGGER.info("Saving ontology model to file: {}", fileName);
		verifyRepository();
		verifyFileName(fileName);

		File outputOntologyFile = new File(new File(getRepositoryLocation()),
				fileName);

		try (OutputStream ontologyOutputStream = new FileOutputStream(
				outputOntologyFile, false)) {
			model.getUnderlyingModel().getNamespaceManager().setPrefix(SWRLNames.SWRL_NAMESPACE,
					SWRLNames.SWRL_PREFIX);
			model.getUnderlyingModel().getNamespaceManager().setPrefix(SWRLNames.SWRLB_NAMESPACE,
					SWRLNames.SWRLB_PREFIX);
			model.getUnderlyingModel().getNamespaceManager().setPrefix(SWRLNames.SWRLX_NAMESPACE,
					SWRLNames.SWRLX_PREFIX);

			List<String> errList = new ArrayList<String>();

			LOGGER.debug("Preparing file '{}' to write", fileName);

			model.getUnderlyingModel().save(ontologyOutputStream, "RDF/XML-ABBREV", errList);
			if(!errList.isEmpty()){
				throw new OntologyException(errList.get(0));
			}
			LOGGER.info("Model successfully saved to file: {}", fileName);

		} catch (Exception exc) {
			LOGGER.error("Could not save ontology", exc);
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_CANNOT_SAVE.getMessage(fileName,
							exc.getLocalizedMessage()), exc);
		}

	}

	/* (non-Javadoc)
	 * @see itti.com.pl.ontology.core.ontology.OntologyRepository#removeOntology(java.lang.String)
	 */
	@Override
	public void removeOntology(String fileName) throws OntologyException {

		LOGGER.info("Removinf ontology file: {}", fileName);
		verifyFileName(fileName);
		verifyRepository();

		File ontologyFile = new File(new File(getRepositoryLocation()),
				fileName);
		if(!ontologyFile.delete()){
			throw new OntologyException(ErrorMessages.ONTOLOGY_CANNOT_REMOVE.getMessage(fileName));
		}
		LOGGER.info("Onotology successfully removed");
	}

	/* (non-Javadoc)
	 * @see itti.com.pl.ontology.core.ontology.OntologyRepository#loadOntology(java.lang.String)
	 */
	@Override
	public Ontology loadOntology(String fileName) throws OntologyException {

		JenaOWLModel model = null;

		LOGGER.debug("Init model: {} from repository {}", fileName,
				getRepositoryLocation());

		verifyRepository();
		verifyFileName(fileName);

		File ontologyLocation = new File(new File(getRepositoryLocation()), fileName);

		try (InputStream ontologyInputStream = new FileInputStream(ontologyLocation)) {
			model = ProtegeOWL
					.createJenaOWLModelFromInputStream(ontologyInputStream);
		} catch (Exception exc) {
			LOGGER.error("Cannot load ontlogy using given location.", exc);
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_CANNOT_LOAD.getMessage(ontologyLocation), exc);
		}

		LOGGER.info("Ontology successfully loaded");
		return new OntologyManager(model);
	}

	/* (non-Javadoc)
	 * @see itti.com.pl.ontology.core.ontology.OntologyRepository#getListOfOntologies()
	 */
	@Override
	public List<String> getListOfOntologies() throws OntologyException {

		LOGGER.debug("Retriving ontologies from {}",
				getRepositoryLocation());

		verifyRepository();

		File repository = new File(getRepositoryLocation());
		if (repository.exists() && repository.isDirectory()) {
			String[] files = repository.list(new FilenameFilter() {

				@Override
				public boolean accept(File directory, String fileName) {
					return fileName != null
							&& fileName.toLowerCase().endsWith(ONTOLOGY_SUFFIX);
				}
			});
			return Arrays.asList(files);
		} else {
			LOGGER.warn("Could not read repository folder: {}", getRepositoryLocation());
			throw new OntologyException(
					ErrorMessages.ONTOLOGY_REPO_CANNOT_ACCESS
							.getMessage(getRepositoryLocation()));
		}
	}

	private void verifyFileName(String fileName) throws OntologyException {
		if(StringUtils.isEmpty(getRepositoryLocation())){
			throw new OntologyException(
					ErrorMessages.ONTOLOGY_EMPTY_FILE_NAME_PROVIDED.getMessage());			
		}
	}

	private void verifyRepository() throws OntologyException {
		if(StringUtils.isEmpty(getRepositoryLocation())){
			throw new OntologyException(
					ErrorMessages.ONTOLOGY_REPO_UNDEFINED.getMessage());			
		}
	}
}
