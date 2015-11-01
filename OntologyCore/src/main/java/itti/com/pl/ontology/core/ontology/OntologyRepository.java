package itti.com.pl.ontology.core.ontology;

import java.util.List;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * Interface defining Arena-specific ontology operations
 * 
 * @author cm-admin
 * 
 */
public interface OntologyRepository {

	/**
	 * Saves current ontology model to file
	 *
	 * @param model model to be saved
	 * @param fileName
	 *            location of the file, where ontology should be saved
	 */
	public void saveOntology(JenaOWLModel model, String fileName);

	/**
	 * Loads ontology model from the file
	 * 
	 * @param fileName
	 *            location of the file containing ontology
	 * @return {@link JenaOWLModel}
	 */
	public JenaOWLModel loadOntology(String fileName);

	/**
	 * Returns a list of all ontologies from the repository
	 * 
	 * @return list of strings representing ontology names
	 */
	public List<String> getListOfOntologies();
}
