package itti.com.pl.ontology.core.ontology;

import itti.com.pl.ontology.common.exception.OntologyException;

import java.util.List;

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
	 * @param ontology model to be saved
	 * @param fileName
	 *            location of the file, where ontology should be saved
	 * @throws OntologyException 
	 */
	public void saveOntology(Ontology ontology, String fileName) throws OntologyException;

	/**
	 * Loads ontology model from the file
	 * 
	 * @param fileName
	 *            location of the file containing ontology
	 * @return {@link Ontology}
	 * @throws OntologyException 
	 */
	public Ontology loadOntology(String fileName) throws OntologyException;

	/**
	 * Removes ontology from repository
	 * @param fileName location of the ontology file name
	 * @throws OntologyException 
	 */
	public void removeOntology(String fileName) throws OntologyException;

	/**
	 * Returns a list of all ontologies from the repository
	 * 
	 * @return list of strings representing ontology names
	 * @throws OntologyException 
	 */
	public List<String> getListOfOntologies() throws OntologyException;
}
