package itti.com.pl.ontology.core.ontology;

import itti.com.pl.ontology.common.bean.Instance;
import itti.com.pl.ontology.common.bean.OntologyClass;

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
     * @param baseClass
     *            ontology class
     * @param instance
     *            instance
     */
    public void createInstance(OntologyClass baseClass, Instance instance);

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
}
