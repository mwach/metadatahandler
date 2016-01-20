package itti.com.pl.ontology.core.ontology;

import itti.com.pl.ontology.common.bean.Instance;
import itti.com.pl.ontology.common.bean.InstanceProperty;
import itti.com.pl.ontology.common.bean.OntologyClass;

import java.util.List;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * Interface defining Arena-specific ontology operations
 * 
 * @author cm-admin
 * 
 */
public interface Ontology {

	/**
	 * Returns underlying OWL model
	 * @return {@link JenaOWLModel}
	 */
	public JenaOWLModel getUnderlyingModel();

	/**
	 * Returns namespace of the ontology
	 * @return namespace
	 */
	public String getNamespace();

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
     * Creates simple instance in the ontology model
     * 
     * @param instance new instance
     */
    public void createInstance(Instance instance);

    /**
     * Updates existing instance in the ontology model
     * 
     * @param instance instance to be updated
     */
    public void updateInstance(Instance instance);

    /**
     * Returns an instance from the ontology
     * @param instanceName name of the instance
     * @return instance object
     */
	public Instance getInstance(String instanceName);

    /**
     * Checks, if instance with given name exists in the ontology
     * 
     * @param instanceName
     *            name of the instance
     * @return true if instance with given name was found in the ontology, false otherwise
     */
    public boolean hasInstance(String instanceName);

    /**
     * Removes instance identified by its name from ontology
     * 
     * @param instanceName
     *            name of the instance to remove
     */
    public void removeInstance(String instanceName);

    /**
     * Creates a new class in the ontology model
     * 
     * @param ontologyClass
     *            class object
     */
    public void createClass(OntologyClass ontologyClass);

    /**
     * Returns an ontology class
     * @param className name of the class
     * @return {@link OntologyClass}
     */
	public OntologyClass getOntologyClass(String className);

    /**
     * Removes an existing class from the ontology model
     * 
     * @param ontologyClass name of the ontology class
     */
    public void removeClass(String ontologyClass);

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
     * Returns list of SWRL rules defined in the ontology
     * 
     * @return list of SWRL rules. If there are no rules, empty list will be returned
     */
    public List<String> getSwrlRules();

    /**
     * Runs SWRL engine on existing model
     */
    public void runSwrlEngine();

    /**
     * Returns a list of instances matching provided criteria
     * @param criteria search criteria
     * @return list of instance names matching criteria
     */
	public List<String> query(List<InstanceProperty<?>> criteria);

	/**
	 * Updates value of the property for given instance
	 * @param instanceName name of the instance
	 * @param property property name and its values
	 */
	void updateProperty(String instanceName, InstanceProperty<?> property);

	/**
	 * Updates underlying OWL model
	 * @param ontology model to be used
	 */
	public void updateModel(Ontology ontology);

}
