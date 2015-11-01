package itti.com.pl.ontology.core.ontology;

import itti.com.pl.ontology.common.exception.OntologyRuntimeException;
import itti.com.pl.ontology.core.exception.ErrorMessages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.smi.protege.exception.OntologyException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;

/**
 * Base ontology-management class
 * 
 * @author cm-admin
 * 
 */
public class OntologyRepositoryManager implements OntologyRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(OntologyRepositoryManager.class);

	// location, where all the ontologies are stored
	private String ontologyRepository = null;

	/**
	 * Sets ontology repository location (must be done before calling 'init'
	 * method)
	 * 
	 * @param ontologyRepository
	 *            location of the ontology repository
	 */
	public final void setOntologyRepository(String ontologyRepository) {
		this.ontologyRepository = ontologyRepository;
	}

	private final String getOntologyRepository() {
		return ontologyRepository;
	}

	/**
	 * Saves current ontology model to file
	 * 
	 * @param fileName
	 *            location of the file, where ontology should be saved
	 */
	@Override
	public void saveOntology(JenaOWLModel model, String fileName) {

		LOGGER.info("Saving ontology model");

		File outputOntologyFile = new File(new File(getOntologyRepository()),
				fileName);

		try (OutputStream ontologyOutputStream = new FileOutputStream(
				outputOntologyFile, false)) {
			model.getNamespaceManager().setPrefix(SWRLNames.SWRL_NAMESPACE,
					SWRLNames.SWRL_PREFIX);
			model.getNamespaceManager().setPrefix(SWRLNames.SWRLB_NAMESPACE,
					SWRLNames.SWRLB_PREFIX);
			model.getNamespaceManager().setPrefix(SWRLNames.SWRLX_NAMESPACE,
					SWRLNames.SWRLX_PREFIX);

			List<String> errList = new ArrayList<String>();

			LOGGER.debug("Preparing file '{}' to write", fileName);

			URI output = new URI("file:///" + getOntologyRepository() + fileName);
			model.save(output, "RDF/XML-ABBREV", errList);
//			model.save(ontologyOutputStream, "RDF/XML-ABBREV", errList);
			LOGGER.info("Model successfully saved to file: {}", fileName);

		} catch (Exception exc) {
			LOGGER.error("Could not save ontology", exc);
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_CANNOT_SAVE.getMessage(fileName,
							exc.getLocalizedMessage()), exc);
		}

	}

	/**
	 * Loads ontology model from the file
	 * 
	 * @param fileName
	 *            location of the file containing ontology
	 */
	@Override
	public JenaOWLModel loadOntology(String fileName) {

		JenaOWLModel model = null;

		LOGGER.debug("Init model: {} from repository {}", fileName,
				ontologyRepository);

		File ontologyLocation = new File(new File(ontologyRepository), fileName);

		try (InputStream ontologyInputStream = new FileInputStream(
				ontologyLocation.getAbsolutePath())) {
			model = ProtegeOWL
					.createJenaOWLModelFromInputStream(ontologyInputStream);
		} catch (Exception exc) {
			LOGGER.error("Cannot load ontlogy using given location. Details: {}",
					exc.getLocalizedMessage());
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_CANNOT_LOAD.getMessage(exc,
							ontologyLocation), exc);
		}

		LOGGER.info("Ontology successfully loaded");
		return model;
	}


	/**
	 * Returns a list of all ontologies from the repository
	 * 
	 * @return list of strings representing ontology names
	 * @throws OntologyException
	 */
	@Override
	public List<String> getListOfOntologies() {

		if (StringUtils.isEmpty(getOntologyRepository())) {
			throw new OntologyException(
					ErrorMessages.ONTOLOGY_REPO_UNDEFINED.getMessage());
		}
		File repository = new File(getOntologyRepository());
		if (repository.exists() && repository.isDirectory()) {
			String[] files = repository.list(new FilenameFilter() {

				@Override
				public boolean accept(File directory, String fileName) {
					return fileName != null
							&& fileName.toLowerCase().endsWith("owl");
				}
			});
			return Arrays.asList(files);
		} else {
			throw new OntologyException(
					ErrorMessages.ONTOLOGY_REPO_CANNOT_ACCESS
							.getMessage(getOntologyRepository()));
		}
	}
}
