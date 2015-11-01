package itti.com.pl.ontology.core.ontology;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import itti.com.pl.ontology.common.bean.Instance;
import itti.com.pl.ontology.common.bean.OntologyClass;
import itti.com.pl.ontology.common.bean.OntologyProperty;
import itti.com.pl.ontology.common.exception.OntologyRuntimeException;
import itti.com.pl.ontology.core.exception.ErrorMessages;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

public class OntologyManagerTest {

    public static final String ONTOLOGY_REPOSITORY = "src/test/resources";
    public static final String ONTOLOGY_LOCATION = "TestOntology.owl";
    public static final String ONTOLOGY_NAMESPACE = "http://www.owl-ontologies.com/Ontology1350654591.owl#";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static OntologyRepositoryManager ontologyRepository = null;
    private static OntologyManager ontologyManager;
    private static JenaOWLModel model = null;

    @BeforeClass
    public static void beforeClass() throws OntologyRuntimeException {

    	ontologyRepository = new OntologyRepositoryManager();
    	ontologyRepository.setOntologyRepository(ONTOLOGY_REPOSITORY);
    	model = ontologyRepository.loadOntology(ONTOLOGY_LOCATION);
    	
        // to speed up tests, load ontology and then reuse it among tests
        ontologyManager = new OntologyManager(model);
        ontologyManager.setOntologyNamespace(ONTOLOGY_NAMESPACE);
    }

    @AfterClass
    public static void afterClass() {
        ontologyManager.shutdown();
    }

    @Test
    public void testInitInvalidLocation() throws OntologyRuntimeException {
        // check exception, when no location was provided
        expectedException.expect(OntologyRuntimeException.class);
        expectedException.expectMessage(String.format(ErrorMessages.ONTOLOGY_CANNOT_LOAD.getMessage(), "null"));
        new OntologyRepositoryManager().loadOntology("");
    }

    @Test
    public void testCreateClass() throws OntologyRuntimeException {

    	String className = "dummyClass";
    	String instanceName = "dummyInstance";

    	OntologyClass ontologyClass = new OntologyClass();
    	ontologyClass.setName(className);
    	OntologyProperty property = new OntologyProperty("dummyProperty", Integer.class);
    	ontologyClass.add(property);

    	Instance instance = new Instance(ontologyClass, instanceName);
    	
        ontologyManager.createClass(ontologyClass);
        ontologyManager.createInstance(ontologyClass, instance);
        
        // now add some basic instance to newly create class
//        ontologyManager.createSimpleInstance(className, instanceName, null);

//        List<String> instances = ontologyManager.getInstances(className);
        // verify, instance was added to given class
//        Assert.assertEquals(1, instances.size());
//        Assert.assertEquals(instanceName, instances.get(0));
    }

    @Test
    public void testAddInstanceWithProperties() throws OntologyRuntimeException {

        // select existing class having at least one known property
        String propertyName = "SomeProperty";
        String propertyValue = String.valueOf(123.456);
        Map<String, String[]> properties = new HashMap<>();
        properties.put(propertyName, new String[] { propertyValue });

        // try to add instance of parking
        String instanceName = "classs";
        // now add some basic instance to newly create class
//        ontologyManager.createSimpleInstance(className, instanceName, properties);

        // verify, instance was correctly added
        Map<String, String[]> ontologyProperties = ontologyManager.getInstanceProperties(instanceName);
        // verify, instance was added to given class
        // two properties should be returned - added in this test and ontology-related one (rdf:type)
        Assert.assertEquals(2, ontologyProperties.size());
        // verify value of added property
        Assert.assertEquals(propertyValue, ontologyProperties.get("http://www.owl-ontologies.com/Ontology1350654591.owl#" + propertyName)[0]);
    }

    @Test
    public void testRemoveInstance() throws OntologyRuntimeException {
        // create a new instance in ontology
        String instanceName = "dummyBuilding_" + System.currentTimeMillis();
//        ontologyManager.createSimpleInstance("ParentClass", instanceName, null);
        // verify, instance was added to ontology
        assertTrue(ontologyManager.hasInstance(instanceName));
        // remove it
        ontologyManager.removeInstance(instanceName);
        // verify, instance was removed from ontology
        assertFalse(ontologyManager.hasInstance(instanceName));
    }

    @Test
    public void testRemoveNonExistingInstance() throws OntologyRuntimeException {

        // try to delete an non-existing instance
        String instanceName = "dummyBuilding_tt_" + System.currentTimeMillis();

        expectedException.expect(OntologyRuntimeException.class);
        expectedException.expectMessage(String.format(ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND.getMessage(), instanceName));
        ontologyManager.removeInstance(instanceName);
    }

}
