package itti.com.pl.ontology.server.ontology;

import itti.com.pl.ontology.common.exception.OntologyRuntimeException;
import itti.com.pl.ontology.server.exception.ErrorMessages;
import itti.com.pl.ontology.server.utils.LogHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import edu.stanford.smi.protege.exception.OntologyException;
import edu.stanford.smi.protege.model.DefaultInstance;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.SimpleInstance;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;
import edu.stanford.smi.protegex.owl.model.query.QueryResults;
import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRuleEngineFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.jess.JessSWRLRuleEngineCreator;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;

/**
 * Base ontology-management class
 * 
 * @author cm-admin
 * 
 */
public class OntologyManager implements Ontology{

    private static final String QUERY_GET_DIRECT_INSTANCES = "PREFIX ns:<%s> SELECT ?%s WHERE { ?%s rdf:type ns:%s }";
    private static final String QUERY_GET_INSTANCES = "PREFIX ns:<%s> SELECT ?%s WHERE { ?%s rdf:type ?subclass. "
            + "?subclass rdfs:subClassOf ns:%s }";
    private static final String QUERY_GET_INSTANCE_CLASS = "PREFIX ns:<%s> SELECT ?%s WHERE { ns:%s rdf:type ?%s }";

    // in-memory ontology model
    private String modelName = null;
    private JenaOWLModel model = null;

    private List<String> ignoredProperties = new ArrayList<>();

    protected synchronized final JenaOWLModel getModel() {
        return model;
    }

    protected synchronized final void setModel(JenaOWLModel model) {
        this.model = model;
    }

    //location, where all the ontologies are stored
    private String ontologyRepository = null;

    /**
     * Sets ontology repository location (must be done before calling 'init' method)
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

    // namespace of the loaded ontology
    private String ontologyNamespace = null;

    /**
     * Sets namespace of the ontology (must be done before calling 'init' method)
     * 
     * @param ontologyNamespace
     *            namespace used by the ontology
     */
    public final void setOntologyNamespace(String ontologyNamespace) {
        this.ontologyNamespace = ontologyNamespace;
    }

    protected final String getOntologyNamespace() {
        return ontologyNamespace;
    }

    // variable used for querying ontology
    protected final String VAR = "instance";

    /**
     * {@link Constructor}
     */
    public OntologyManager() {
        ignoredProperties.add("rdf:type");
    }

    /**
     * Closes ontology model
     */
    public void shutdown() {
        if (getModel() != null) {
            getModel().close();
        }
    }

    /* (non-Javadoc)
     * @see itti.com.pl.ontology.server.ontology.Ontology#getDirectInstances(java.lang.String)
     */
    @Override
    public final List<String> getDirectInstances(String className) {
        return getInstances(className);
    }

    /* (non-Javadoc)
     * @see itti.com.pl.ontology.server.ontology.Ontology#getInstances(java.lang.String)
     */
    @Override
    public final List<String> getInstances(String className) {

        LogHelper.debug(OntologyManager.class, "getInstances", "Query for instances of '%s'", className);

        if (StringUtils.isEmpty(className)) {
            return new ArrayList<>();
        }
        String query = String.format(QUERY_GET_DIRECT_INSTANCES, getOntologyNamespace(), VAR, VAR, className);
        return executeSparqlQuery(query, VAR);
    }


    /* (non-Javadoc)
     * @see itti.com.pl.ontology.server.ontology.Ontology#getNonDirectInstances(java.lang.String)
     */
    @Override
    public List<String> getNonDirectInstances(String className) {

        LogHelper.debug(OntologyManager.class, "getNonDirectInstances", "Query for instances of '%s'", className);
        if (StringUtils.isEmpty(className)) {
            return new ArrayList<>();
        }
        String query = String.format(QUERY_GET_INSTANCES, getOntologyNamespace(), VAR, VAR, className);

        return (executeSparqlQuery(query, VAR));
    }

    
    /* (non-Javadoc)
     * @see itti.com.pl.ontology.server.ontology.Ontology#getInstanceClass(java.lang.String)
     */
    @Override
    public String getInstanceClass(String instanceName) {

        LogHelper
                .debug(OntologyManager.class, "getInstanceClass", "Query for parent class of '%s'", String.valueOf(instanceName));

        if (StringUtils.isEmpty(instanceName)) {
            LogHelper.error(OntologyManager.class, "getInstanceClass", "Null instance name provided");
            throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_EMPTY_INSTANCE_NAME.getMessage());
        }
        String query = String.format(QUERY_GET_INSTANCE_CLASS, getOntologyNamespace(), VAR, instanceName, VAR);
        List<String> results = executeSparqlQuery(query, VAR);
        if (results.isEmpty()) {
            LogHelper.warning(OntologyManager.class, "getInstanceClass", "No results were found for instance '%s'", instanceName);
            throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND.getMessage(instanceName));
        }
        return results.get(0);
    }


    /**
     * Executes SPARQL query on ontology model
     * 
     * @param query
     *            query to execute
     * @param variable
     *            variable to be returned
     * @return list of string returned by the query
     */
    private List<String> executeSparqlQuery(String query, String variable) {

        LogHelper.debug(OntologyManager.class, "executeSparqlQuery", "Executing '%s' query using '%s' var", query, variable);

        List<String> resultList = new ArrayList<String>();
        try {
            QueryResults results = getModel().executeSPARQLQuery(query);
            while (results.hasNext()) {
                Object value = results.next().get(variable);
                String result = null;
                if (value instanceof DefaultInstance) {
                    result = ((DefaultInstance) value).getBrowserText();
                } else if (value instanceof DefaultRDFSLiteral) {
                    result = ((DefaultRDFSLiteral) value).getBrowserText();
                } else {
                    LogHelper.warning(OntologyManager.class, "executeSparqlQuery", "Unrecognized query results class: '%s'",
                            value);
                }
                resultList.add(result);

            }

        } catch (Exception exc) {
            LogHelper.exception(OntologyManager.class, "executeSparqlQuery",
                    String.format("Failed to execute query '%s'", query), exc);
            resultList.clear();
        }
        return resultList;
    }


    /* (non-Javadoc)
     * @see itti.com.pl.ontology.server.ontology.Ontology#createSimpleInstance(java.lang.String, java.lang.String, java.util.Map)
     */
    @Override
    public OWLIndividual createSimpleInstance(String className, String instanceName, Map<String, String[]> properties) {

        LogHelper.debug(OntologyManager.class, "createSimpleInstance", "Creating instance '%s' for class '%s'", instanceName,
                className);

        // create individual, or get the existing one
        OWLIndividual individual = null;
        if (getInstances(className).contains(instanceName)) {
            individual = getInstance(instanceName);
        } else {
            individual = createInstanceOnly(className, instanceName);
        }

        // individual created, now add all the properties
        if (properties != null) {
            for (Entry<String, String[]> entry : properties.entrySet()) {
                updateProperty(individual, entry.getKey(), entry.getValue());
            }
        }
        return individual;
    }

    /**
     * Creates a new class in the ontology model
     * 
     * @param instanceName
     *            name of the instance
     * @return ontology object with given name
     * @throws OntologyException
     */
    private OWLIndividual getInstance(String instanceName) {

        LogHelper.debug(OntologyManager.class, "getInstance", "Searching for instance '%s'", instanceName);
        return getModel().getOWLIndividual(instanceName);
    }

    private OWLIndividual createInstanceOnly(String className, String instanceName) throws OntologyException {

        OWLNamedClass parentClass = getModel().getOWLNamedClass(className);
        OWLIndividual individual = null;
        if (parentClass == null) {
            LogHelper
                    .warning(OntologyManager.class, "createInstanceOnly", "Base class '%s' not found in the ontology", className);
            throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_CLASS_DOESNT_EXIST.getMessage(className));
        }
        try {
            individual = parentClass.createOWLIndividual(instanceName);
        } catch (RuntimeException exc) {
            LogHelper.exception(OntologyManager.class, "createInstanceOnly",
                    String.format("Cannot create instance '%s' of class '%s'. Probably a duplicate", instanceName, className),
                    exc);
            throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_COULD_NOT_ADD_INSTANCE.getMessage(instanceName, className));
        }
        return individual;
    }

    private void updateProperty(OWLIndividual individual, String propertyName, String[] values) {

        LogHelper.debug(OntologyManager.class, "createPropertyValue", "Creting property '%s' for instance", propertyName,
                individual.getName());

        // get the property
        RDFProperty property = getModel().getRDFProperty(propertyName);
        if (property == null) {
            LogHelper.warning(OntologyManager.class, "createSimpleInstance", "Property '%s' not found for type %s", propertyName,
                    individual.getBrowserText());
        } else {

            removePropertyValues(individual, property);

            // now, set property value
            for (String value : values) {
                // find value as an instance
                OWLIndividual valueInd = getModel().getOWLIndividual(value);
                if (valueInd != null) {
                    addPropertyValue(individual, property, valueInd);
                    // not an instance - try numbers first
                    // try to add value as a string
                } else if (StringUtils.isNumeric(value)) {
                    Integer valueInt = Integer.parseInt(value);
                    addPropertyValue(individual, property, valueInt);
                    // try to add value as a string
                } else if (isFloat(value)) {
                    Float valueFloat = Float.parseFloat(value);
                    addPropertyValue(individual, property, valueFloat);
                } else {
                    addPropertyValue(individual, property, value);
                }
            }
        }
    }

    private boolean isFloat(String value) {

    	boolean isFloat = false;
    	try{
    		Float.parseFloat(value);
    		isFloat = true;
    	}catch(RuntimeException exc){
    		
    	}
		return isFloat;
	}

	/**
     * returns list of instance properties from the ontology model
     * 
     * @param instanceName
     *            name of the instance
     * @return list of non-empty properties
     * @throws OntologyException
     */
    public Map<String, String[]> getInstanceProperties(String instanceName) throws OntologyException {

        LogHelper.debug(OntologyManager.class, "getInstanceProperties", "Collecting properties for instance '%s'", instanceName);

        Map<String, String[]> properties = new HashMap<>();

        OWLIndividual individual = getModel().getOWLIndividual(instanceName);
        if (individual == null) {
            LogHelper.warning(OntologyManager.class, "getInstanceProperties", "Instance '%s' was not found in the ontology",
                    instanceName);
            throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND.getMessage(instanceName));
        }

        @SuppressWarnings("unchecked")
        Collection<RDFProperty> instanceProperties = individual.getRDFProperties();

        for (RDFProperty rdfProperty : instanceProperties) {
            String propertyName = rdfProperty.getName();

            // check, if property is ignored
            if (ignoredProperties.contains(propertyName)) {
                continue;
            }
            @SuppressWarnings("rawtypes")
            Collection propertyValues = individual.getPropertyValues(rdfProperty);
            String[] values = new String[propertyValues.size()];
            int propertyNo = 0;
            for (Object propertyValue : propertyValues) {
                if (propertyValue instanceof Instance) {
                    values[propertyNo++] = ((Instance) propertyValue).getName();
                } else {
                    values[propertyNo++] = String.valueOf(propertyValue);
                }
            }
            LogHelper.debug(OntologyManager.class, "getInstanceProperties", "Collected %d values for property '%s'",
                    values.length, propertyName);
            properties.put(propertyName, values);
        }
        return properties;
    }


    /* (non-Javadoc)
     * @see itti.com.pl.ontology.server.ontology.Ontology#hasInstance(java.lang.String)
     */
    @Override
    public boolean hasInstance(String instanceName) {
        // check, if name was provided
        if (StringUtils.isNotEmpty(instanceName)) {
            // check if instance exist in the model
            return getModel().getOWLIndividual(instanceName) != null;
        }
        return false;
    }


    /* (non-Javadoc)
     * @see itti.com.pl.ontology.server.ontology.Ontology#remove(java.lang.String)
     */
    @Override
    public void remove(String instanceName) throws OntologyException {
        if (StringUtils.isNotEmpty(instanceName)) {
            Instance instance = getInstance(instanceName);
            if (instance == null) {
                throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND.getMessage(instanceName));
            }
            getModel().deleteInstance(instance);
        } else {
            throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_EMPTY_INSTANCE_NAME.getMessage());
        }
    }

    /**
     * returns values for single property of an individual stored in the ontology model
     * 
     * @param instanceName
     *            name of the instance
     * @param propertyName
     *            name of the property
     * @return list of property values
     * @throws OntologyException
     */
    private String[] getInstanceProperties(String instanceName, String propertyName) throws OntologyException {
        return getInstanceProperties(instanceName).get(propertyName);
    }

    @Override
    public void createOwlClass(String className) {

        LogHelper.debug(OntologyManager.class, "createOwlClass", "Creating class '%s'", className);

        // check, if class existed in the ontology
        if (getModel().getOWLIndividual(className) == null) {
            // if not, try to create it
            OWLNamedClass individual = getModel().createOWLNamedClass(className);
        }else{
        	throw new OntologyRuntimeException("ALREady exist");
        }
    }

    /**
     * Adds a value of the property to the instance
     * 
     * @param instance
     *            OWL instance
     * @param property
     *            property of the instance
     * @param value
     *            value to be set
     */
    private void addPropertyValue(OWLIndividual instance, RDFProperty property, Object value) {
        // set new value of the property
        instance.addPropertyValue(property, value);
    }

    /**
     * Adds a value of the property to the instance
     * 
     * @param instanceName
     *            name of the OWL instance
     * @param propertyName
     *            name of the property
     * @param value
     *            value to be set
     * @throws OntologyException
     */
    private void addPropertyValue(String instanceName, String propertyName, Object value) throws OntologyException {
        addPropertyValue(getInstance(instanceName), getModel().getRDFProperty(propertyName), value);
    }

    /**
     * Updates value of the property for given instance. Before update, removes all existing values of that instance
     * 
     * @param instanceName
     *            name of the instance
     * @param propertyName
     *            name of the property
     * @param propertyValue
     *            new value for the property
     * @throws OntologyException
     *             could not update property value
     */
    private void updatePropertyValue(String instanceName, String propertyName, Object propertyValue) throws OntologyException {
        updatePropertyValues(instanceName, propertyName, new Object[] { propertyValue });
    }

    /**
     * Updates value of the property for given instance. Before update, removes all existing values of that instance
     * 
     * @param instanceName
     *            name of the instance
     * @param propertyName
     *            name of the property
     * @param propertyValues
     *            list of values for property
     * @throws OntologyException
     *             could not update property value
     */
    private void updatePropertyValues(String instanceName, String propertyName, Object[] propertyValues) throws OntologyException {
        OWLIndividual instance = getInstance(instanceName);
        RDFProperty property = getModel().getRDFProperty(propertyName);
        removePropertyValues(instance, property);
        for (Object propertyValue : propertyValues) {
            addPropertyValue(instance, property, propertyValue);
        }
    }

    /**
     * Removes all values of the property for given instance
     * 
     * @param individual
     *            instance
     * @param property
     *            property
     */
    private void removePropertyValues(OWLIndividual individual, RDFProperty property) {
        // remove current value of the property
        int propsCount = individual.getPropertyValueCount(property);
        for (int i = 0; i < propsCount; i++) {
            Object currentOntValue = individual.getPropertyValue(property);
            individual.removePropertyValue(property, currentOntValue);
        }

    }

    /**
     * Saves current ontology model to file
     * 
     * @param fileName
     *            location of the file, where ontology should be saved
     */
    @Override
    public void saveOntology(String fileName) {

        LogHelper.info(OntologyManager.class, "saveModel", "Saving ontology model");

        File outputOntologyFile = new File(new File(getOntologyRepository()), fileName);

        try (OutputStream ontologyOutputStream = new FileOutputStream(outputOntologyFile, false)){
            model.getNamespaceManager().setPrefix(SWRLNames.SWRL_NAMESPACE, SWRLNames.SWRL_PREFIX);
            model.getNamespaceManager().setPrefix(SWRLNames.SWRLB_NAMESPACE, SWRLNames.SWRLB_PREFIX);
            model.getNamespaceManager().setPrefix(SWRLNames.SWRLX_NAMESPACE, SWRLNames.SWRLX_PREFIX);

            List<String> errList = new ArrayList<String>();

            LogHelper.debug(OntologyManager.class, "saveModel", "Preparing file '%s' to write", fileName);
            

            model.save(ontologyOutputStream, "RDF/XML-ABBREV", errList);
            LogHelper.info(OntologyManager.class, "saveModel", "Model successfully saved to file: %s", fileName);

        } catch (Exception exc) {
            LogHelper.exception(OntologyManager.class, "saveModel", "Could not save ontology", exc);
            throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_CANNOT_SAVE.getMessage(fileName, exc.getLocalizedMessage()), exc);
        }

    
    
    }

    /**
     * Loads ontology model from the file
     * 
     * @param fileName
     *            location of the file containing ontology
     */
    @Override
    public void loadOntology(String fileName) {

           LogHelper.debug(OntologyManager.class, "loadModel", "Init model: %s from repository %s", fileName, ontologyRepository);

            File ontologyLocation = 
                    new File(new File(ontologyRepository), fileName);

            try(InputStream ontologyInputStream = new FileInputStream(ontologyLocation.getAbsolutePath())) {
                model = ProtegeOWL.createJenaOWLModelFromInputStream(ontologyInputStream);
            } catch (Exception exc) {
                LogHelper.error(OntologyManager.class, "loadModel", "Cannot load ontlogy using given location. Details: %s",
                        exc.getLocalizedMessage());
                throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_CANNOT_LOAD.getMessage(exc, ontologyLocation), exc);
            } 

            LogHelper.info(OntologyManager.class, "loadModel", "Ontology successfully loaded");

            this.modelName = fileName;
    }

    /**
     * Adds a new rule to the ontology
     * 
     * @param ruleName
     *            name of the rule
     * @param ruleContent
     *            content of the rule
     * @throws OntologyException
     */
    @Override
    public void addSwrlRule(String ruleName, String ruleContent) throws OntologyException {

        LogHelper.info(OntologyManager.class, "addSwrlRule", "Upading models with SWRL rule '%s'", ruleName);

        if (StringUtils.isNotEmpty(ruleName) && StringUtils.isNotEmpty(ruleContent)) {

            // create rule factory
            SWRLFactory factory = new SWRLFactory(model);

            try {

                // add rule to the model
                factory.createImp(ruleName, ruleContent);
                LogHelper.debug(OntologyManager.class, "addSwrlRule", "Rule '%s' added suffessfully. Content of the rule: %s",
                        ruleName, ruleContent);
            } catch (SWRLParseException | RuntimeException exc) {
                LogHelper.exception(OntologyManager.class, "addSwrlRules", "Failed to add rule", exc);
                throw new OntologyRuntimeException(ErrorMessages.SWRL_CANNOT_ADD_RULE.getMessage(exc.getLocalizedMessage()), exc);
            }

        } else {
            LogHelper.warning(OntologyManager.class, "addSwrlRule", "Cannot update model with swrl rule. Empty rule provided");
            throw new OntologyRuntimeException(ErrorMessages.SWRL_EMPTY_RULE.getMessage());
        }
    }

    /**
     * Returns rule identified by its name from the ontology
     * 
     * @param ruleName
     *            name of the rule
     * @return rule definition
     * @throws OntologyException
     */
    @Override
    public String getSwrlRule(String ruleName) throws OntologyException {

        LogHelper.info(OntologyManager.class, "getSwrlRule", "Trying to retrieve rule with ID: %s", ruleName);
        String ruleContent = null;

        // create rule factory
        SWRLFactory factory = new SWRLFactory(model);

        try {

            // get list of imps
            for (Object imp : factory.getImps()) {
                if ((imp instanceof SimpleInstance) && StringUtils.equalsIgnoreCase(ruleName, 
                        ((SimpleInstance)imp).getName())) {
                    SimpleInstance si = (SimpleInstance)imp;
                    ruleContent = si.getBrowserText();
                    break;
                }
            }
            if (StringUtils.isEmpty(ruleContent)) {
                throw new OntologyRuntimeException(ErrorMessages.SWRL_CANNOT_GET_RULE.getMessage(ruleName));
            }
            LogHelper.debug(OntologyManager.class, "getSwrlRules", "Suffessfully colleted rule. Rule content: %s", ruleContent);
        } catch (RuntimeException exc) {
            LogHelper.exception(OntologyManager.class, "getSwrlRule", "Failed to collect rule", exc);
            throw new OntologyRuntimeException(ErrorMessages.SWRL_CANNOT_COLLECT_RULES.getMessage(exc.getLocalizedMessage()), exc);
        }
        return ruleContent;
    }

    /**
     * Removes rule identified by its name from the ontology
     * 
     * @param ruleName
     *            name of the rule
     * @throws OntologyException
     */
    @Override
    public void removeSwrlRule(String ruleName) throws OntologyException {

        LogHelper.info(OntologyManager.class, "removeSwrlRule", "Trying to remove rule with ID: %s", ruleName);
        boolean found = false;

        // create rule factory
        SWRLFactory factory = new SWRLFactory(model);

        try {

            // get list of imps
            @SuppressWarnings("rawtypes")
			Collection imps = factory.getImps();
            for (Object imp : imps) {
                if (StringUtils.equals(ruleName, imp.toString())) {
                    factory.deleteImps();
                    factory.createImp();
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new OntologyRuntimeException(ErrorMessages.SWRL_CANNOT_GET_RULE.getMessage(ruleName));
            }
            LogHelper.debug(OntologyManager.class, "removeSwrlRule", "Suffessfully removed rule. Rule name: %s", ruleName);
        } catch (RuntimeException exc) {
            LogHelper.exception(OntologyManager.class, "removeSwrlRule", "Failed to collect rule", exc);
            throw new OntologyRuntimeException(ErrorMessages.SWRL_CANNOT_GET_RULE.getMessage(exc.getLocalizedMessage()), exc);
        }
    }

    /**
     * Returns a list of rules defined in the ontology
     * 
     * @return list of rules
     * 
     * @throws OntologyException
     */
    @Override
    public List<String> getSwrlRules() throws OntologyException {

        LogHelper.info(OntologyManager.class, "getSwrlRules", "Collecting list of rules defined for given model");
        List<String> rules = new ArrayList<>();

        // create rule factory
        SWRLFactory factory = new SWRLFactory(model);

        try {

            // get list of imps
            for (Object imp : factory.getImps()) {
                if(imp instanceof SimpleInstance)
                {
                    rules.add(((SimpleInstance)imp).getName());
                }
            }
            LogHelper.debug(OntologyManager.class, "getSwrlRules", "Suffessfully colleted rule names. Defined rules: %s", rules);
        } catch (RuntimeException exc) {
            LogHelper.exception(OntologyManager.class, "getSwrlRules", "Failed to collect rule names", exc);
            throw new OntologyRuntimeException(ErrorMessages.SWRL_CANNOT_COLLECT_RULES.getMessage(exc.getLocalizedMessage()), exc);
        }
        return rules;
    }

    /**
     * Runs SWRL engine on existing model
     */
    @Override
    public void runSwrlEngine() throws OntologyException {

        LogHelper.info(OntologyManager.class, "runSwrlEngine", "Swrl Rule bridge will be run now");

        try {
            // SWRLFactory factory = new SWRLFactory(getModel());
            // factory.createImp("Man(?x) ∧ Object_is_in_parking_zone(?x, ?y) ∧ Parking_zone_gives_properties(?y, ?z) →  Man_has_properties(?x, ?z)");
            //SWRLRuleEngineBridge bridge = BridgeFactory.createBridge("SWRLJessBridge", getModel());
            SWRLRuleEngineFactory.registerRuleEngine("Jess", new JessSWRLRuleEngineCreator());
            SWRLRuleEngine bridge = SWRLRuleEngineFactory.create(getModel());

            LogHelper.debug(OntologyManager.class, "runSwrlEngine", "Bridge infer");

            bridge.infer();

            LogHelper.debug(OntologyManager.class, "runSwrlEngine", "Getting updated model from Swrl ridge");

            //model = (JenaOWLModel) bridge.getOWLModel();
        } catch (Throwable exc) {
            LogHelper.exception(OntologyManager.class, "runSwrlEngine",
                    String.format("Error during using SWRL bridge: %s", exc.toString()), exc);
            throw new OntologyRuntimeException(ErrorMessages.SWRL_ENGINE_FAILED.getMessage(exc.getLocalizedMessage()), exc);
        }
    }


    /**
     * Returns a list of all ontologies from the repository
     * @return list of strings representing ontology names
     * @throws OntologyException
     */
    @Override
    public List<String> getListOfOntologies() {

        if(StringUtils.isEmpty(getOntologyRepository())){
            throw new OntologyException(ErrorMessages.ONTOLOGY_REPO_UNDEFINED.getMessage());
        }
        File repository = new File(getOntologyRepository());
        if(repository.exists() && repository.isDirectory()){
            String[] files = repository.list(new FilenameFilter() {
                
                @Override
                public boolean accept(File directory, String fileName) {
                    return fileName != null && fileName.toLowerCase().endsWith("owl");
                }
            });
            return Arrays.asList(files);
        }else{
            throw new OntologyException(ErrorMessages.ONTOLOGY_REPO_CANNOT_ACCESS.getMessage(getOntologyRepository()));
        }
    }

    /**
     * Returns name of the currently loaded ontology
     * @return name of the ontology
     * @throws OntologyException
     */
    @Override
    public String getCurrentOntology() {
        return modelName;
    }

}
