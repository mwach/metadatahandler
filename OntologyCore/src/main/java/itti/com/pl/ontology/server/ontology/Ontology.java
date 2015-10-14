package itti.com.pl.ontology.server.ontology;

import java.util.List;
import java.util.Map;

import edu.stanford.smi.protegex.owl.model.OWLIndividual;

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
    public List<String> getInstances(String className);

    /**
     * Returns list of direct (first level) instances of given ontology class
     * 
     * @param className
     *            name of the class
     * @return list of instances names. If there are no instances, empty list will be returned
     */
    public List<String> getDirectInstances(String className);

    /**
     * Returns list of non-direct (second and further levels) instances of given ontology class
     * 
     * @param className
     *            name of the class
     * @return list of instances names. If there are no instances, empty list will be returned
     */
    public List<String> getNonDirectInstances(String className);

    /**
     * Returns parent class of given instance
     * 
     * @param instanceName
     *            name of the instance
     * @return parent class name.
     */
    public String getInstanceClass(String instanceName);

    /**
     * creates simple instance in the ontology model
     * 
     * @param className
     *            name of the ontology class
     * @param instanceName
     *            name of the instance
     * @param properties
     *            optional list of instance properties
     * @return reference to the newly created instance
     */
    public OWLIndividual createSimpleInstance(String className, String instanceName, Map<String, String[]> properties);

    /**
     * Checks, if instance with given name exists in the ontology
     * 
     * @param instanceName
     *            name of the instance
     * @return true if instance with given name was found in the ontology, false otherwise
     */
    public boolean hasInstance(String instanceName);


    /**
     * Creates a new class in the ontology model
     * 
     * @param className
     *            name of the class
     */
    public void createOwlClass(String className);

    /**
     * Removes instance identified by its name from ontology
     * 
     * @param instanceName
     *            name of the instance to remove
     */
    public void remove(String instanceName);

    /**
     * Adds a new rule to the ontology
     * 
     * @param ruleName
     *            name of the rule
     * @param ruleContent
     *            content of the rule
     */
    public void addSwrlRule(String ruleName, String ruleContent);

    /**
     * Returns rule identified by its name from the ontology
     * 
     * @param ruleName
     *            name of the rule
     * @return rule definition
     */
    public String getSwrlRule(String ruleName);

    /**
     * Removes rule identified by its name from the ontology
     * 
     * @param ruleName
     *            name of the rule
     */
    public void removeSwrlRule(String ruleName);

    /**
     * Runs SWRL engine on existing model
     */
    public void runSwrlEngine();

    /**
     * Returns list of SWRL rules defined in the ontology
     * 
     * @return list of SWRL rules. If there are no rules, empty list will be returned
     */
    public List<String> getSwrlRules();

    /**
     * Saves current ontology model to file
     * 
     * @param fileName
     *            location of the file, where ontology should be saved
     */
    public void saveOntology(String fileName);

    /**
     * Loads ontology model from the file
     * 
     * @param fileName
     *            location of the file containing ontology
     */
    public void loadOntology(String fileName);

    /**
     * Returns a list of all ontologies from the repository
     * @return list of strings representing ontology names
     */
    public List<String> getListOfOntologies();

    /**
     * Returns name of the currently loaded ontology
     * @return name of the ontology
     */
    public String getCurrentOntology();
}
