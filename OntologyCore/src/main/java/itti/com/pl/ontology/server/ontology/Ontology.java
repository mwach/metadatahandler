package itti.com.pl.ontology.server.ontology;

import java.util.List;

/**
 * Interface defining Arena-specific ontology operations
 * 
 * @author cm-admin
 * 
 */
public interface Ontology {


    /**
     * Returns list of instances of given ontology class
     * 
     * @param className
     *            name of the class
     * @return list of instances names. If there are no instances, empty list will be returned
     */
    public List<String> getInstances(String className) throws OntologyException;

    /**
     * Returns list of non-direct (second and further levels) instances of given ontology class
     * 
     * @param className
     *            name of the class
     * @return list of instances names. If there are no instances, empty list will be returned
     */
    public List<String> getNonDirectInstances(String className) throws OntologyException;

    /**
     * Returns parent class of given instance
     * 
     * @param instanceName
     *            name of the instance
     * @return parent class name.
     * @exception OntologyExceptiont
     *                could not find object
     */
    public String getInstanceClass(String instanceName) throws OntologyException;

    /**
     * Removes instance identified by its name from ontology
     * 
     * @param instanceName
     *            name of the instance to remove
     */
    public void remove(String instanceName) throws OntologyException;

    /**
     * Adds a new rule to the ontology
     * 
     * @param ruleName
     *            name of the rule
     * @param ruleContent
     *            content of the rule
     * @throws OntologyException
     */
    public void addSwrlRule(String ruleName, String ruleContent) throws OntologyException;

    /**
     * Returns rule identified by its name from the ontology
     * 
     * @param ruleName
     *            name of the rule
     * @return rule definition
     * @throws OntologyException
     */
    public String getSwrlRule(String ruleName) throws OntologyException;

    /**
     * Removes rule identified by its name from the ontology
     * 
     * @param ruleName
     *            name of the rule
     * @throws OntologyException
     */
    public void removeSwrlRule(String ruleName) throws OntologyException;

    /**
     * Runs SWRL engine on existing model
     */
    public void runSwrlEngine() throws OntologyException;

    /**
     * Returns list of SWRL rules defined in the ontology
     * 
     * @return list of SWRL rules. If there are no rules, empty list will be returned
     */
    public List<String> getSwrlRules() throws OntologyException;

    /**
     * Saves current ontology model to file
     * 
     * @param fileName
     *            location of the file, where ontology should be saved
     */
    public void saveOntology(String fileName) throws OntologyException;

    /**
     * Loads ontology model from the file
     * 
     * @param fileName
     *            location of the file containing ontology
     */
    public void loadOntology(String fileName) throws OntologyException;

    /**
     * Returns a list of all ontologies from the repository
     * @return list of strings representing ontology names
     * @throws OntologyException
     */
    public List<String> getListOfOntologies() throws OntologyException;

    /**
     * Returns name of the currently loaded ontology
     * @return name of the ontology
     * @throws OntologyException
     */
    public String getCurrentOntology() throws OntologyException;
}
