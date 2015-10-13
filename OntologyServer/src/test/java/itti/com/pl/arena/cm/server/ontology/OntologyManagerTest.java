package itti.com.pl.arena.cm.server.ontology;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import itti.com.pl.ontology.server.exception.ErrorMessages;
import itti.com.pl.ontology.server.ontology.OntologyConstants;
import itti.com.pl.ontology.server.ontology.OntologyException;
import itti.com.pl.ontology.server.ontology.OntologyManager;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.BeanInitializationException;

public class OntologyManagerTest {

    public static final String ONTOLOGY_REPOSITORY = "src/test/resources";
    public static final String ONTOLOGY_LOCATION = "TestOntology.owl";
    public static final String ONTOLOGY_NAMESPACE = "http://www.owl-ontologies.com/Ontology1350654591.owl#";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static OntologyManager ontologyManager;

    @BeforeClass
    public static void beforeClass() {

        // to speed up tests, load ontology and then reuse it among tests
        ontologyManager = new OntologyManager();
        ontologyManager.setOntologyLocation(ONTOLOGY_LOCATION);
        ontologyManager.setOntologyRepository(ONTOLOGY_REPOSITORY);
        ontologyManager.setOntologyNamespace(ONTOLOGY_NAMESPACE);
        ontologyManager.init();
    }

    @AfterClass
    public static void afterClass() {
        ontologyManager.shutdown();
    }

    @Test
    public void testInitInvalidLocation() {
        // check exception, when no location was provided
        expectedException.expect(BeanInitializationException.class);
        expectedException.expectMessage(String.format(ErrorMessages.ONTOLOGY_CANNOT_LOAD.getMessage(), "null"));
        OntologyManager om = new OntologyManager();
        om.init();
    }

    @Test
    public void testCreateOwlClass() throws OntologyException {
        String className = "classsss";
        String instanceName = className + "inst";
        // verify class was added
        Assert.assertTrue(ontologyManager.createOwlClass(className));
        // now add some basic instance to newly create class
        ontologyManager.createSimpleInstance(className, instanceName, null);

        List<String> instances = ontologyManager.getInstances(className);
        // verify, instance was added to given class
        Assert.assertEquals(1, instances.size());
        Assert.assertEquals(instanceName, instances.get(0));
    }

    @Test
    public void testAddInstanceWithProperties() throws OntologyException {

        // select existing class having at least one known property
        String className = OntologyConstants.Parking.name();
        String propertyName = OntologyConstants.Object_has_GPS_x.name();
        String propertyValue = String.valueOf(123.456);
        Map<String, String[]> properties = new HashMap<>();
        properties.put(propertyName, new String[] { propertyValue });

        // try to add instance of parking
        String instanceName = "classs";
        // now add some basic instance to newly create class
        ontologyManager.createSimpleInstance(className, instanceName, properties);

        // verify, instance was correctly added
        Map<String, String[]> ontologyProperties = ontologyManager.getInstanceProperties(instanceName);
        // verify, instance was added to given class
        // two properties should be returned - added in this test and ontology-related one (rdf:type)
        Assert.assertEquals(2, ontologyProperties.size());
        // verify value of added property
        Assert.assertEquals(propertyValue, ontologyProperties.get("http://www.owl-ontologies.com/Ontology1350654591.owl#" + propertyName)[0]);
    }

    @Test
    public void testRemoveInstance() throws OntologyException {
        // create a new instance in ontology
        String instanceName = "dummyBuilding_" + System.currentTimeMillis();
        ontologyManager.createSimpleInstance(OntologyConstants.Building.name(), instanceName, null);
        // verify, instance was added to ontology
        assertNotNull(ontologyManager.getInstance(instanceName));
        // remove it
        ontologyManager.remove(instanceName);
        // verify, instance was removed from ontology
        assertNull(ontologyManager.getInstance(instanceName));
    }

    @Test
    public void testRemoveNonExistingInstance() throws OntologyException {

        // try to delete an non-existing instance
        String instanceName = "dummyBuilding_tt_" + System.currentTimeMillis();

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage(String.format(ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND.getMessage(), instanceName));
        ontologyManager.remove(instanceName);
    }

}
