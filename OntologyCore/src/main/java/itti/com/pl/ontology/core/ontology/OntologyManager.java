package itti.com.pl.ontology.core.ontology;

import itti.com.pl.ontology.common.bean.InstanceProperty;
import itti.com.pl.ontology.common.bean.OntologyClass;
import itti.com.pl.ontology.common.bean.OntologyProperty;
import itti.com.pl.ontology.common.bean.OntologyType;
import itti.com.pl.ontology.common.exception.OntologyRuntimeException;
import itti.com.pl.ontology.core.exception.ErrorMessages;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.smi.protege.exception.OntologyException;
import edu.stanford.smi.protege.model.DefaultInstance;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.SimpleInstance;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLNamedClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;
import edu.stanford.smi.protegex.owl.model.query.QueryResults;
import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRuleEngineFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.jess.JessSWRLRuleEngineCreator;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;

/**
 * Base ontology-management class
 * 
 * @author cm-admin
 * 
 */
public class OntologyManager implements Ontology {

	private static final Logger LOGGER = LoggerFactory.getLogger(OntologyManager.class);

	private static final String QUERY_GET_DIRECT_INSTANCES = "PREFIX ns:<%s> SELECT ?%s WHERE { ?%s rdf:type ns:%s }";
	private static final String QUERY_GET_INSTANCES = "PREFIX ns:<%s> SELECT ?%s WHERE { ?%s rdf:type ?subclass. "
			+ "?subclass rdfs:subClassOf ns:%s }";
	private static final String QUERY_GET_INSTANCE_CLASS = "PREFIX ns:<%s> SELECT ?%s WHERE { ns:%s rdf:type ?%s }";

	private JenaOWLModel model = null;

	private List<String> ignoredProperties = new ArrayList<>();

	protected synchronized final JenaOWLModel getModel() {
		return model;
	}

	protected synchronized final void setModel(JenaOWLModel model) {
		this.model = model;
	}

	// namespace of the loaded ontology
	private String ontologyNamespace = null;

	/**
	 * Sets namespace of the ontology (must be done before calling 'init'
	 * method)
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
	 * @param model 
	 */
	public OntologyManager(JenaOWLModel model) {
		this.model = model;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * itti.com.pl.ontology.server.ontology.Ontology#getDirectInstances(java
	 * .lang.String)
	 */
	@Override
	public final List<String> getDirectInstances(String className) {
		return getInstances(className);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * itti.com.pl.ontology.server.ontology.Ontology#getInstances(java.lang.
	 * String)
	 */
	@Override
	public final List<String> getInstances(String className) {

		LOGGER.debug("Query for instances of '{}'", className);

		if (StringUtils.isEmpty(className)) {
			return new ArrayList<>();
		}
		String query = String.format(QUERY_GET_DIRECT_INSTANCES,
				getOntologyNamespace(), VAR, VAR, className);
		return executeSparqlQuery(query, VAR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * itti.com.pl.ontology.server.ontology.Ontology#getNonDirectInstances(java
	 * .lang.String)
	 */
	@Override
	public List<String> getNonDirectInstances(String className) {

		LOGGER.debug("Query for instances of '{}'", className);
		if (StringUtils.isEmpty(className)) {
			return new ArrayList<>();
		}
		String query = String.format(QUERY_GET_INSTANCES,
				getOntologyNamespace(), VAR, VAR, className);

		return (executeSparqlQuery(query, VAR));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * itti.com.pl.ontology.server.ontology.Ontology#getInstanceClass(java.lang
	 * .String)
	 */
	@Override
	public String getInstanceClass(String instanceName) {

		LOGGER.debug("Query for parent class of '{}'", String.valueOf(instanceName));

		if (StringUtils.isEmpty(instanceName)) {
			LOGGER.error("Null instance name provided");
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_EMPTY_INSTANCE_NAME.getMessage());
		}
		String query = String.format(QUERY_GET_INSTANCE_CLASS,
				getOntologyNamespace(), VAR, instanceName, VAR);
		List<String> results = executeSparqlQuery(query, VAR);
		if (results.isEmpty()) {
			LOGGER.warn("No results were found for instance '{}'", instanceName);
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND
							.getMessage(instanceName));
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

		LOGGER.debug("Executing '{}' query using '{}' var", query, variable);

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
					LOGGER.warn(
							"Unrecognized query results class: '{}'", value);
				}
				resultList.add(result);

			}

		} catch (Exception exc) {
			LOGGER.error(String.format("Failed to execute query '{}'", query), exc);
			resultList.clear();
		}
		return resultList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * itti.com.pl.ontology.server.ontology.Ontology#createSimpleInstance(java
	 * .lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public void createInstance(OntologyClass baseClass,
			itti.com.pl.ontology.common.bean.Instance instance) {

		LOGGER.debug("Creating instance '{}' for class '{}'", baseClass, instance);

		// create individual, or get the existing one
		OWLIndividual individual = null;
		if (getInstances(baseClass.getName()).contains(instance.getName())) {
			individual = getOwlInstance(instance.getName());
		} else {
			individual = createInstanceOnly(baseClass.getName(),
					instance.getName());
		}

		// individual created, now add all the properties
		for (InstanceProperty<?> instanceProperty : instance.getProperties()) {

			updateProperty(individual, instanceProperty);
		}
	}

	/**
	 * Creates a new class in the ontology model
	 * 
	 * @param instanceName
	 *            name of the instance
	 * @return ontology object with given name
	 * @throws OntologyException
	 */
	private OWLIndividual getOwlInstance(String instanceName) {

		LOGGER.debug("Searching for instance '{}'", instanceName);
		return getModel().getOWLIndividual(instanceName);
	}

	private OWLIndividual createInstanceOnly(String className,
			String instanceName) throws OntologyException {

		OWLNamedClass parentClass = getModel().getOWLNamedClass(className);
		OWLIndividual individual = null;
		if (parentClass == null) {
			LOGGER.warn("Base class '{}' not found in the ontology", className);
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_CLASS_DOESNT_EXIST
							.getMessage(className));
		}
		try {
			individual = parentClass.createOWLIndividual(instanceName);
		} catch (RuntimeException exc) {
			LOGGER
					.error(String.format(
									"Cannot create instance '{}' of class '{}'. Probably a duplicate",
									instanceName, className), exc);
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_COULD_NOT_ADD_INSTANCE.getMessage(
							instanceName, className));
		}
		return individual;
	}

	private void updateProperty(OWLIndividual individual,
			InstanceProperty<?> property) {

		LOGGER.debug("Creting property '{}' for instance {}", property.getName(),
				individual.getName());

		// get the property
		RDFProperty rdfProperty = getModel().getRDFProperty(property.getName());

		removePropertyValues(individual, rdfProperty);

		// now, set property value
		for (Object value : property.getValues()) {
			// find value as an instance
			OWLIndividual valueInd = getModel().getOWLIndividual(
					value.toString());
			if (valueInd != null) {
				addPropertyValue(individual, rdfProperty, valueInd);
				// not an instance - try numbers first
				// try to add value as a string
			} else if (StringUtils.isNumeric(value.toString())) {
				Integer valueInt = Integer.parseInt(value.toString());
				addPropertyValue(individual, rdfProperty, valueInt);
				// try to add value as a string
			} else if (isFloat(value.toString())) {
				Float valueFloat = Float.parseFloat(value.toString());
				addPropertyValue(individual, rdfProperty, valueFloat);
			} else {
				addPropertyValue(individual, rdfProperty, value);
			}
		}
	}

	private boolean isFloat(String value) {

		boolean isFloat = false;
		try {
			Float.parseFloat(value);
			isFloat = true;
		} catch (RuntimeException exc) {

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
	public itti.com.pl.ontology.common.bean.Instance getInstance(String instanceName)
			throws OntologyException {

		LOGGER.debug("Collecting properties for instance '{}'", instanceName);
		OWLIndividual individual = getModel().getOWLIndividual(instanceName);
		if (individual == null) {
			LOGGER
					.warn("Instance '{}' was not found in the ontology",
							instanceName);
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND
							.getMessage(instanceName));
		}

		itti.com.pl.ontology.common.bean.Instance instance = new itti.com.pl.ontology.common.bean.Instance();
		instance.setName(instanceName);
		String className = individual.getRDFType().getLocalName();
		OntologyClass ontologyClass = getOntologyClass(className);
		instance.setBaseClass(ontologyClass);

		@SuppressWarnings("unchecked")
		Collection<RDFProperty> instanceProperties = individual
				.getRDFProperties();

		for (RDFProperty rdfProperty : instanceProperties) {
			String propertyPrefix = rdfProperty.getNamespacePrefix();
			String propertyName = rdfProperty.getLocalName();

			// check, if property is ignored
			if (ignoredProperties.contains(String.format("%s:%s", propertyPrefix, propertyName))) {
				continue;
			}
			OntologyType propertyClass = ontologyClass.getPropertyType(propertyName);
			@SuppressWarnings("rawtypes")
			Collection propertyValues = individual
					.getPropertyValues(rdfProperty);
			LOGGER.debug("Collected {} values for property '{}'", propertyValues,
					propertyName);
			instance.addProperty(new InstanceProperty(propertyName, propertyClass, propertyValues));
		}
		return instance;
	}

	private OntologyClass getOntologyClass(String className) {
		OntologyClass ontologyClass = new OntologyClass(className);
		DefaultOWLNamedClass owlClass = (DefaultOWLNamedClass) getModel().getOWLNamedClass(className);
		@SuppressWarnings("unchecked")
		Collection<RDFProperty> classProperties = owlClass.getUnionDomainProperties();

		for (RDFProperty rdfProperty : classProperties) {
			RDFResource range = rdfProperty.getRange();
			if(range != null){
				ontologyClass.add(new OntologyProperty(rdfProperty.getLocalName(), OntologyType.getType(range.getLocalName())));
			}else{
				
			}
		}
		return ontologyClass;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * itti.com.pl.ontology.server.ontology.Ontology#hasInstance(java.lang.String
	 * )
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * itti.com.pl.ontology.server.ontology.Ontology#remove(java.lang.String)
	 */
	@Override
	public void removeInstance(String instanceName) throws OntologyException {
		if (StringUtils.isNotEmpty(instanceName)) {
			Instance instance = getOwlInstance(instanceName);
			if (instance == null) {
				throw new OntologyRuntimeException(
						ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND
								.getMessage(instanceName));
			}
			getModel().deleteInstance(instance);
		} else {
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_EMPTY_INSTANCE_NAME.getMessage());
		}
	}

	@Override
	public void createClass(OntologyClass ontologyClass) {

		LOGGER.debug("Creating class '{}'", ontologyClass.getName());

		// check, if class existed in the ontology
		if (getModel().getOWLIndividual(ontologyClass.getName()) == null) {
			// if not, try to create it
			OWLNamedClass individual = getModel().createOWLNamedClass(
					ontologyNamespace + ontologyClass.getName());
			for (OntologyProperty ontologyProperty : ontologyClass
					.getProperties()) {
				RDFProperty property = null;
					property = getModel().createOWLDatatypeProperty(
							ontologyProperty.getName());
					property.setRange(getDatatypeRange(ontologyProperty.getType()));
				property.setDomain(individual);
			}
		} else {
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_CLASS_ALREADY_EXIST
							.getMessage(ontologyClass));
		}
	}

	private RDFResource getDatatypeRange(OntologyType type) {
		String typeName = type.name().toLowerCase();
		String methodName = "getXSD" + typeName;
		try {
			Method method = getModel().getClass().getMethod(methodName);
			RDFResource resp = (RDFResource) method.invoke(getModel());
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getModel().getXSDstring();
	}

	@Override
	public void removeClass(String ontologyClass) {
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
	private void addPropertyValue(OWLIndividual instance, RDFProperty property,
			Object value) {
		// set new value of the property
		instance.addPropertyValue(property, value);
	}

	/**
	 * Removes all values of the property for given instance
	 * 
	 * @param individual
	 *            instance
	 * @param property
	 *            property
	 */
	private void removePropertyValues(OWLIndividual individual,
			RDFProperty property) {
		// remove current value of the property
		int propsCount = individual.getPropertyValueCount(property);
		for (int i = 0; i < propsCount; i++) {
			Object currentOntValue = individual.getPropertyValue(property);
			individual.removePropertyValue(property, currentOntValue);
		}

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
	public void addSwrlRule(String ruleName, String ruleContent)
			throws OntologyException {

		LOGGER.info("Upading models with SWRL rule '{}'", ruleName);

		if (StringUtils.isNotEmpty(ruleName)
				&& StringUtils.isNotEmpty(ruleContent)) {

			// create rule factory
			SWRLFactory factory = new SWRLFactory(model);

			try {

				// add rule to the model
				factory.createImp(ruleName, ruleContent);
				LOGGER
						.debug("Rule '{}' added suffessfully. Content of the rule: {}",
								ruleName, ruleContent);
			} catch (SWRLParseException | RuntimeException exc) {
				LOGGER.error("Failed to add rule", exc);
				throw new OntologyRuntimeException(
						ErrorMessages.SWRL_CANNOT_ADD_RULE.getMessage(exc
								.getLocalizedMessage()), exc);
			}

		} else {
			LOGGER.warn("Cannot update model with swrl rule. Empty rule provided");
			throw new OntologyRuntimeException(
					ErrorMessages.SWRL_EMPTY_RULE.getMessage());
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

		LOGGER.info("Trying to retrieve rule with ID: {}", ruleName);
		String ruleContent = null;

		// create rule factory
		SWRLFactory factory = new SWRLFactory(model);

		try {

			// get list of imps
			for (Object imp : factory.getImps()) {
				if ((imp instanceof SimpleInstance)
						&& StringUtils.equalsIgnoreCase(ruleName,
								((SimpleInstance) imp).getName())) {
					SimpleInstance si = (SimpleInstance) imp;
					ruleContent = si.getBrowserText();
					break;
				}
			}
			if (StringUtils.isEmpty(ruleContent)) {
				throw new OntologyRuntimeException(
						ErrorMessages.SWRL_CANNOT_GET_RULE.getMessage(ruleName));
			}
			LOGGER
					.debug("Suffessfully colleted rule. Rule content: {}",
							ruleContent);
		} catch (RuntimeException exc) {
			LOGGER.error("Failed to collect rule", exc);
			throw new OntologyRuntimeException(
					ErrorMessages.SWRL_CANNOT_COLLECT_RULES.getMessage(exc
							.getLocalizedMessage()), exc);
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

		LOGGER.info("Trying to remove rule with ID: {}", ruleName);
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
				throw new OntologyRuntimeException(
						ErrorMessages.SWRL_CANNOT_GET_RULE.getMessage(ruleName));
			}
			LOGGER.debug("Suffessfully removed rule. Rule name: {}", ruleName);
		} catch (RuntimeException exc) {
			LOGGER.error("Failed to collect rule", exc);
			throw new OntologyRuntimeException(
					ErrorMessages.SWRL_CANNOT_GET_RULE.getMessage(exc
							.getLocalizedMessage()), exc);
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

		LOGGER.info("Collecting list of rules defined for given model");
		List<String> rules = new ArrayList<>();

		// create rule factory
		SWRLFactory factory = new SWRLFactory(model);

		try {

			// get list of imps
			for (Object imp : factory.getImps()) {
				if (imp instanceof SimpleInstance) {
					rules.add(((SimpleInstance) imp).getName());
				}
			}
			LOGGER.debug("Suffessfully colleted rule names. Defined rules: {}",
					rules);
		} catch (RuntimeException exc) {
			LOGGER.error("Failed to collect rule names", exc);
			throw new OntologyRuntimeException(
					ErrorMessages.SWRL_CANNOT_COLLECT_RULES.getMessage(exc
							.getLocalizedMessage()), exc);
		}
		return rules;
	}

	/**
	 * Runs SWRL engine on existing model
	 */
	@Override
	public void runSwrlEngine() throws OntologyException {

		LOGGER.info("Swrl Rule bridge will be run now");

		try {
			// SWRLFactory factory = new SWRLFactory(getModel());
			// factory.createImp("Man(?x) ∧ Object_is_in_parking_zone(?x, ?y) ∧ Parking_zone_gives_properties(?y, ?z) →  Man_has_properties(?x, ?z)");
			// SWRLRuleEngineBridge bridge =
			// BridgeFactory.createBridge("SWRLJessBridge", getModel());
			SWRLRuleEngineFactory.registerRuleEngine("Jess",
					new JessSWRLRuleEngineCreator());
			SWRLRuleEngine bridge = SWRLRuleEngineFactory.create(getModel());

			LOGGER.debug("Bridge infer");

			bridge.infer();

			LOGGER.debug("Getting updated model from Swrl ridge");

			// model = (JenaOWLModel) bridge.getOWLModel();
		} catch (Throwable exc) {
			LOGGER.error(String.format("Error during using SWRL bridge: {}",
							exc.toString()), exc);
			throw new OntologyRuntimeException(
					ErrorMessages.SWRL_ENGINE_FAILED.getMessage(exc
							.getLocalizedMessage()), exc);
		}
	}

	@Override
	public JenaOWLModel getUnderlyingModel() {
		return model;
	}
}
