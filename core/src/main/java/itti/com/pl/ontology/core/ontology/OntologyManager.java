package itti.com.pl.ontology.core.ontology;

import itti.com.pl.ontology.common.bean.InstanceProperty;
import itti.com.pl.ontology.common.bean.OntologyClass;
import itti.com.pl.ontology.common.bean.OntologyProperty;
import itti.com.pl.ontology.common.bean.OntologyType;
import itti.com.pl.ontology.common.exception.OntologyRuntimeException;
import itti.com.pl.ontology.core.exception.ErrorMessages;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLDataRange;
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

	private static final String QUERY_CRITERIA = "PREFIX ns:<%s> SELECT ?instance WHERE { %s }";
	private static final String QUERY_CRITERIA_SUBQUERY = "?instance ns:%s ?val%d FILTER (?val%d = %s)";

	// variable used for querying ontology
	private static final String VAR = "instance";

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

	/**
	 * {@link Constructor}
	 * 
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
		String query = String.format(QUERY_GET_DIRECT_INSTANCES, getOntologyNamespace(), VAR, VAR, className);
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
		String query = String.format(QUERY_GET_INSTANCES, getOntologyNamespace(), VAR, VAR, className);

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
			throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_EMPTY_INSTANCE_NAME.getMessage());
		}
		String query = String.format(QUERY_GET_INSTANCE_CLASS, getOntologyNamespace(), VAR, instanceName, VAR);
		List<String> results = executeSparqlQuery(query, VAR);
		if (results.isEmpty()) {
			LOGGER.warn("No results were found for instance '{}'", instanceName);
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
					LOGGER.warn("Unrecognized query results class: '{}'", value);
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
	public void createInstance(itti.com.pl.ontology.common.bean.Instance instance) {

		LOGGER.debug("Creating instance '{}' for class '{}'", instance, instance.getBaseClass());

		// create individual, or get the existing one
		OWLIndividual individual = null;
		if (getInstances(instance.getBaseClass().getName()).contains(instance.getName())) {
			LOGGER.warn("Instance '{}' already exists in the ontology", instance.getName());
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_INSTANCE_ALREADY_EXIST.getMessage(instance.getName()));
		} else {

			validateProperties(instance);
			individual = createInstanceOnly(instance.getBaseClass().getName(), instance.getName());
		}

		// individual created, now add all the properties
		for (InstanceProperty<?> instanceProperty : instance.getProperties()) {
			updateProperty(individual, instanceProperty);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * itti.com.pl.ontology.server.ontology.Ontology#createSimpleInstance(java
	 * .lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public void updateInstance(itti.com.pl.ontology.common.bean.Instance instance) {

		LOGGER.debug("Updating instance '{}' for class '{}'", instance, instance.getBaseClass());

		// create individual, or get the existing one
		OWLIndividual individual = null;
		if (getInstances(instance.getBaseClass().getName()).contains(instance.getName())) {
			individual = getOwlInstance(instance.getName());
		} else {
			LOGGER.warn("Instance '{}' not found in the ontology", instance.getName());
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND.getMessage(instance.getName()));
		}
		validateProperties(instance);

		// individual created, now add all the properties
		for (InstanceProperty<?> instanceProperty : instance.getProperties()) {

			updateProperty(individual, instanceProperty);
		}
	}

	/* (non-Javadoc)
	 * @see itti.com.pl.ontology.core.ontology.Ontology#updateProperty(java.lang.String, itti.com.pl.ontology.common.bean.InstanceProperty)
	 */
	@Override
	public void updateProperty(String instanceName, InstanceProperty<?> property) {
		OWLIndividual individual = getOwlInstance(instanceName);
		updateProperty(individual, property);
	}

	/**
	 * Validates if properties defined for given instance are defined in the
	 * ontology
	 * 
	 * @param instance
	 *            instance to be validated
	 */
	@SuppressWarnings("deprecation")
	private void validateProperties(itti.com.pl.ontology.common.bean.Instance instance) {

		OntologyClass ontologyClass = getOntologyClass(instance.getBaseClass().getName());
		for (InstanceProperty<?> property : instance.getProperties()) {
			OntologyProperty ontologyProperty = ontologyClass.getProperty(property.getName());

			// property not found in ontology
			if (ontologyProperty == null) {
				LOGGER.warn("Property '{}' is invalid for instance {}", property.getName(), instance.getName());
				throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_PROPERTY_NOT_FOUND_FOR_INSTANCE
						.getMessage(property.getName(), instance.getName()));

			}
			// object property
			if (ontologyProperty.getType() == null) {
				OWLProperty owlProperty = getModel().getOWLProperty(property.getName());
				RDFResource range = owlProperty.getRange();

				Collection<?> rangeInstances = null;
				if (range instanceof DefaultOWLDataRange) {
					//range
					rangeInstances = ((DefaultOWLDataRange) range).getOneOfValueLiterals();
				} else {
					// objects/instances
					rangeInstances = getModel().getInstances((OWLNamedClass) range);
				}
				for (Object propertyValue : property.getValues()) {
					if (!instanceExists(propertyValue, rangeInstances)) {
						LOGGER.warn("Property '{}' is invalid for instance {}", property.getName(),
								instance.getName());
						throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_PROPERTY_NOT_FOUND_FOR_INSTANCE
								.getMessage(property.getName(), instance.getName()));
					}
				}
			}
		}
	}

	/**
	 * Checks if given instanceName exists in provided set of instances
	 * 
	 * @param instanceName
	 *            name of the instance
	 * @param instances
	 *            collection of instances
	 * @return true, if instanceName exists in the collection, false otherwise
	 */
	private boolean instanceExists(Object instanceName, Collection<?> instances) {
		String innerQueryInstanceName = instanceName.toString();
		if(instanceName instanceof itti.com.pl.ontology.common.bean.Instance){
			innerQueryInstanceName = ((itti.com.pl.ontology.common.bean.Instance)instanceName).getName();
		}
		for (Object instance : instances) {
			String innerInstanceName = instance.toString();
			if (instance instanceof OWLIndividual) {
				innerInstanceName = ((OWLIndividual) instance).getLocalName();
			}
			if (innerInstanceName.equals(innerQueryInstanceName)) {
				return true;
			}
		}
		return false;
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

	private OWLIndividual createInstanceOnly(String className, String instanceName) throws OntologyException {

		OWLNamedClass parentClass = getModel().getOWLNamedClass(className);
		OWLIndividual individual = null;
		if (parentClass == null) {
			LOGGER.warn("Base class '{}' not found in the ontology", className);
			throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_CLASS_DOESNT_EXIST.getMessage(className));
		}
		try {
			individual = parentClass.createOWLIndividual(instanceName);
		} catch (RuntimeException exc) {
			LOGGER.error(String.format("Cannot create instance '{}' of class '{}'. Probably a duplicate", instanceName,
					className), exc);
			throw new OntologyRuntimeException(
					ErrorMessages.ONTOLOGY_COULD_NOT_ADD_INSTANCE.getMessage(instanceName, className));
		}
		return individual;
	}

	private void updateProperty(OWLIndividual individual, InstanceProperty<?> property) {

		LOGGER.debug("Creting property '{}' for instance {}", property.getName(), individual.getName());

		// get the property
		RDFProperty rdfProperty = getModel().getRDFProperty(property.getName());
		OntologyType type = OntologyType.getType(rdfProperty.getRange().getLocalName());

		removePropertyValues(individual, rdfProperty);

		// now, set property value
		for (Object value : property.getValues()) {
			if(value instanceof itti.com.pl.ontology.common.bean.Instance){
				value = ((itti.com.pl.ontology.common.bean.Instance)value).getName();
			}
			// find value as an instance
			OWLIndividual valueInd = getModel().getOWLIndividual(value.toString());
			if (valueInd != null) {
				addPropertyValue(individual, rdfProperty, valueInd);
			} else {
				value = formatValue(value, type);
				addPropertyValue(individual, rdfProperty, value);
			}
		}
	}

	private Object formatValue(Object value, OntologyType type) {
		if (type == OntologyType.Date) {
			return new SimpleDateFormat("yyyy-MM-dd").format(((Date) value));
		} else if (type == OntologyType.DateTime) {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(((Date) value));
		} else if (type == OntologyType.Time) {
			return new SimpleDateFormat("HH:mm:ss").format(((Date) value));
		}
		return value;
	}

	private Object unformatValue(Object value, OntologyType type) throws ParseException {
		if (type == OntologyType.Date) {
			return new SimpleDateFormat("yyyy-MM-dd").parse(value.toString());
		} else if (type == OntologyType.DateTime) {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(value.toString());
		} else if (type == OntologyType.Time) {
			return new SimpleDateFormat("HH:mm:ss").parse(value.toString());
		}
		return value;
	}

	/**
	 * returns list of instance properties from the ontology model
	 * 
	 * @param instanceName
	 *            name of the instance
	 * @return list of non-empty properties
	 * @throws OntologyException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public itti.com.pl.ontology.common.bean.Instance getInstance(String instanceName) throws OntologyException {

		LOGGER.debug("Collecting properties for instance '{}'", instanceName);
		OWLIndividual individual = getModel().getOWLIndividual(instanceName);
		if (individual == null) {
			LOGGER.warn("Instance '{}' was not found in the ontology", instanceName);
			throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND.getMessage(instanceName));
		}

		itti.com.pl.ontology.common.bean.Instance instance = new itti.com.pl.ontology.common.bean.Instance();
		instance.setName(instanceName);
		String className = individual.getRDFType().getLocalName();
		OntologyClass ontologyClass = getOntologyClass(className);
		instance.setBaseClass(ontologyClass);

		Collection<RDFProperty> instanceProperties = individual.getRDFProperties();

		for (RDFProperty rdfProperty : instanceProperties) {
			String propertyPrefix = rdfProperty.getNamespacePrefix();
			String propertyName = rdfProperty.getLocalName();

			if (isPropertyIgnored(propertyPrefix, propertyName)) {
				continue;
			}
			OntologyType propertyClass = ontologyClass.getPropertyType(propertyName);
			Collection rawPropertyValues = individual.getPropertyValues(rdfProperty);
			Collection formattedProperties = new ArrayList<>();
			for (Object object : rawPropertyValues) {
				try {
					formattedProperties.add(unformatValue(object, propertyClass));
				} catch (ParseException e) {
					LOGGER.warn("Could not parse value {}. Exception: {}", object, e.getLocalizedMessage());
				}
			}
			LOGGER.debug("Collected {} values for property '{}'", rawPropertyValues, propertyName);
			instance.addProperty(new InstanceProperty(propertyName, formattedProperties));
		}
		return instance;
	}

	private boolean isPropertyIgnored(String propertyPrefix, String propertyName) {
		return ignoredProperties.contains(String.format("%s:%s", propertyPrefix, propertyName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * itti.com.pl.ontology.core.ontology.Ontology#getOntologyClass(java.lang.
	 * String)
	 */
	@Override
	public OntologyClass getOntologyClass(String className) {
		OWLNamedClass owlClass = getModel().getOWLNamedClass(className);
		if (owlClass == null) {
			throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_CLASS_DOESNT_EXIST.getMessage(className));
		}
		OntologyClass ontologyClass = new OntologyClass(className);

		ontologyClass.setParentClass(getParentClassName(owlClass));

		@SuppressWarnings("unchecked")
		Collection<RDFProperty> classProperties = owlClass.getAssociatedProperties();

		for (RDFProperty rdfProperty : classProperties) {
			RDFResource range = rdfProperty.getRange();
			if (range != null) {
				ontologyClass.add(
						new OntologyProperty(rdfProperty.getLocalName(), OntologyType.getType(range.getLocalName())));
			} else {

			}
		}
		return ontologyClass;
	}

	/**
	 * Returns name of the parent for given class
	 * 
	 * @param owlClass
	 *            input class
	 * @return name of the parent for input class
	 */
	private String getParentClassName(OWLNamedClass owlClass) {
		if (owlClass.hasNamedSuperclass()) {
			for (Object parentClassObj : owlClass.getNamedSuperclasses()) {
				OWLNamedClass parentClass = (OWLNamedClass) parentClassObj;
				if (parentClass.getNamespace().equals(getOntologyNamespace())) {
					return parentClass.getLocalName();
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see itti.com.pl.ontology.server.ontology.Ontology#hasInstance(java.lang.
	 * String )
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
				throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND.getMessage(instanceName));
			}
			getModel().deleteInstance(instance);
		} else {
			throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_EMPTY_INSTANCE_NAME.getMessage());
		}
	}

	@Override
	public void createClass(OntologyClass ontologyClass) {

		LOGGER.debug("Creating class '{}'", ontologyClass.getName());

		// check for parent class
		OWLNamedClass parent = null;
		if (ontologyClass.getParent() != null) {
			parent = getModel().getOWLNamedClass(ontologyClass.getParent());
		}
		// check, if class existed in the ontology
		if (getModel().getOWLIndividual(ontologyClass.getName()) == null) {

			// if not, try to create it
			OWLNamedClass individual = (parent != null)
					? getModel().createOWLNamedSubclass(ontologyNamespace + ontologyClass.getName(), parent)
					: getModel().createOWLNamedClass(ontologyNamespace + ontologyClass.getName());
			for (OntologyProperty ontologyProperty : ontologyClass.getProperties()) {
				if(ontologyProperty.getType() == OntologyType.Class){
					RDFProperty property = getModel().createOWLObjectProperty(ontologyProperty.getName());
					property.setRange(getModel().getOWLNamedClass(ontologyProperty.getRange()));
					property.setDomain(individual);
				}else{
					RDFProperty property = getModel().createOWLDatatypeProperty(ontologyProperty.getName());
					property.setRange(getDatatypeRange(ontologyProperty.getType()));
					property.setDomain(individual);
				}
			}
		} else {
			throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_CLASS_ALREADY_EXIST.getMessage(ontologyClass));
		}
	}

	private RDFResource getDatatypeRange(OntologyType type) {
		String methodName = "getXSD" + type.getRdfType();
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
	public void removeClass(String className) {
		OWLNamedClass classToDelete = getModel().getOWLNamedClass(className);
		if (classToDelete == null) {
			throw new OntologyRuntimeException(ErrorMessages.ONTOLOGY_CLASS_DOESNT_EXIST.getMessage(className));
		}

		getModel().deleteCls(classToDelete);
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

		LOGGER.info("Upading models with SWRL rule '{}'", ruleName);

		if (StringUtils.isNotEmpty(ruleName) && StringUtils.isNotEmpty(ruleContent)) {

			// create rule factory
			SWRLFactory factory = new SWRLFactory(model);

			try {

				// add rule to the model
				factory.createImp(ruleName, ruleContent);
				LOGGER.debug("Rule '{}' added suffessfully. Content of the rule: {}", ruleName, ruleContent);
			} catch (SWRLParseException | RuntimeException exc) {
				LOGGER.error("Failed to add rule", exc);
				throw new OntologyRuntimeException(
						ErrorMessages.SWRL_CANNOT_ADD_RULE.getMessage(exc.getLocalizedMessage()), exc);
			}

		} else {
			LOGGER.warn("Cannot update model with swrl rule. Empty rule provided");
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

		LOGGER.info("Trying to retrieve rule with ID: {}", ruleName);
		String ruleContent = null;

		// create rule factory
		SWRLFactory factory = new SWRLFactory(model);

		try {

			// get list of imps
			for (Object imp : factory.getImps()) {
				if ((imp instanceof SimpleInstance)
						&& StringUtils.equalsIgnoreCase(ruleName, ((SimpleInstance) imp).getName())) {
					SimpleInstance si = (SimpleInstance) imp;
					ruleContent = si.getBrowserText();
					break;
				}
			}
			if (StringUtils.isEmpty(ruleContent)) {
				throw new OntologyRuntimeException(ErrorMessages.SWRL_CANNOT_GET_RULE.getMessage(ruleName));
			}
			LOGGER.debug("Suffessfully colleted rule. Rule content: {}", ruleContent);
		} catch (RuntimeException exc) {
			LOGGER.error("Failed to collect rule", exc);
			throw new OntologyRuntimeException(
					ErrorMessages.SWRL_CANNOT_COLLECT_RULES.getMessage(exc.getLocalizedMessage()), exc);
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
				throw new OntologyRuntimeException(ErrorMessages.SWRL_CANNOT_GET_RULE.getMessage(ruleName));
			}
			LOGGER.debug("Suffessfully removed rule. Rule name: {}", ruleName);
		} catch (RuntimeException exc) {
			LOGGER.error("Failed to collect rule", exc);
			throw new OntologyRuntimeException(ErrorMessages.SWRL_CANNOT_GET_RULE.getMessage(exc.getLocalizedMessage()),
					exc);
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
			LOGGER.debug("Suffessfully colleted rule names. Defined rules: {}", rules);
		} catch (RuntimeException exc) {
			LOGGER.error("Failed to collect rule names", exc);
			throw new OntologyRuntimeException(
					ErrorMessages.SWRL_CANNOT_COLLECT_RULES.getMessage(exc.getLocalizedMessage()), exc);
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
//			 SWRLFactory factory = new SWRLFactory(getModel());
			// factory.createImp("Man(?x) ∧ Object_is_in_parking_zone(?x, ?y) ∧
			// Parking_zone_gives_properties(?y, ?z) → Man_has_properties(?x,
			// ?z)");
//			 SWRLRuleEngineBridge bridge =
//			 BridgeFactory.createBridge("SWRLJessBridge", getModel());
			SWRLRuleEngineFactory.registerRuleEngine("SWRLJessBridge", new JessSWRLRuleEngineCreator());
			SWRLRuleEngine bridge = SWRLRuleEngineFactory.create(getModel());

			LOGGER.debug("Bridge infer");

			bridge.infer();

			LOGGER.debug("Getting updated model from Swrl ridge");

			// model = (JenaOWLModel) bridge.getOWLModel();
		} catch (Throwable exc) {
			LOGGER.error(String.format("Error during using SWRL bridge: {}", exc.toString()), exc);
			throw new OntologyRuntimeException(ErrorMessages.SWRL_ENGINE_FAILED.getMessage(exc.getLocalizedMessage()),
					exc);
		}
	}

	@Override
	public JenaOWLModel getUnderlyingModel() {
		return model;
	}

	/* (non-Javadoc)
	 * @see itti.com.pl.ontology.core.ontology.Ontology#query(java.util.List)
	 */
	@Override
	public List<String> query(List<InstanceProperty<?>> criteria) {
		StringBuilder criteriaBuilder = new StringBuilder();
		for(int pos = 0 ; pos<criteria.size() ; pos++){
			criteriaBuilder.append(String.format(QUERY_CRITERIA_SUBQUERY, criteria.get(pos).getName(), pos, pos, criteria.get(pos).getValues().get(0)));
			criteriaBuilder.append(". ");
		}
		//PREFIX ns:<http://www.owl-ontologies.com/Ontology1350654591.owl#>
		//SELECT ?subject
		//		WHERE { ?subject ns:queryBoolean ?val FILTER (?val = false)}
		String query = String.format(QUERY_CRITERIA, getOntologyNamespace(), criteriaBuilder.toString());
		List<String> result = executeSparqlQuery(query, VAR);
		return result;
	}
}
