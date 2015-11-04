package itti.com.pl.ontology.core.ontology;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import itti.com.pl.ontology.common.bean.Instance;
import itti.com.pl.ontology.common.bean.InstanceProperty;
import itti.com.pl.ontology.common.bean.OntologyClass;
import itti.com.pl.ontology.common.bean.OntologyProperty;
import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.common.exception.OntologyRuntimeException;
import itti.com.pl.ontology.core.exception.ErrorMessages;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class OntologyManagerTest {

    public static final String ONTOLOGY_REPOSITORY = "src/test/resources";
    public static final String ONTOLOGY_LOCATION = "TestOntology.owl";
    public static final String ONTOLOGY_NAMESPACE = "http://www.owl-ontologies.com/Ontology1350654591.owl#";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static LocalOntologyRepository ontologyRepository = null;
    private static OntologyManager ontologyManager;

    @BeforeClass
    public static void beforeClass() throws OntologyRuntimeException, OntologyException {

    	ontologyRepository = new LocalOntologyRepository();
    	ontologyRepository.setRepositoryLocation(ONTOLOGY_REPOSITORY);
    	ontologyManager = (OntologyManager) ontologyRepository.loadOntology(ONTOLOGY_LOCATION);
    	ontologyManager.setOntologyNamespace(ONTOLOGY_NAMESPACE);
    }

    @AfterClass
    public static void afterClass() {
        ontologyManager.shutdown();
    }

    @Test
    public void testCreateClass() throws OntologyRuntimeException {

    	String className = "dummyClass";
    	String instanceName = "dummyInstance";

    	OntologyClass ontologyClass = new OntologyClass(className);
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

    	OntologyClass newClass = new OntologyClass("testAddInstanceWithProperties");
    	newClass.add(new OntologyProperty("testAddInstanceWithPropertiesProperty", Integer.class));
    	ontologyManager.createClass(newClass);

    	Instance instance = new Instance(newClass, "instance");
    	instance.addProperty(new InstanceProperty<Integer>("testAddInstanceWithPropertiesProperty", Integer.class, 23));
    	// try to add instance
        ontologyManager.createInstance(newClass, instance);

        try {
        	LocalOntologyRepository repo = new LocalOntologyRepository();
        	repo.setRepositoryLocation("src/test/resources");
			repo.saveOntology(ontologyManager, "test.owl");
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // verify, instance was correctly added
        Instance responseInstance = ontologyManager.getInstance(instance.getName());
        // verify, instance was added to given class
        // two properties should be returned - added in this test and ontology-related one (rdf:type)
        Assert.assertEquals(instance, responseInstance);
    }

    @Test
    public void testRemoveInstance() throws OntologyRuntimeException {
        // create a new instance in ontology
    	String className = "dummyClass_" + System.currentTimeMillis();
        String instanceName = "inst_" + className;
        OntologyClass baseClass = new OntologyClass(className);
        ontologyManager.createClass(baseClass);
        ontologyManager.createInstance(baseClass, new Instance(baseClass, instanceName));
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
