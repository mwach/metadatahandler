package itti.com.pl.ontology.core.ontology;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import itti.com.pl.ontology.common.bean.Instance;
import itti.com.pl.ontology.common.bean.InstanceProperty;
import itti.com.pl.ontology.common.bean.OntologyClass;
import itti.com.pl.ontology.common.bean.OntologyProperty;
import itti.com.pl.ontology.common.bean.OntologyType;
import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.common.exception.OntologyRuntimeException;
import itti.com.pl.ontology.core.exception.ErrorMessages;

import org.junit.AfterClass;
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

        try {
        	LocalOntologyRepository repo = new LocalOntologyRepository();
        	repo.setRepositoryLocation("/tmp");
			repo.saveOntology(ontologyManager, "test.owl");
		} catch (OntologyException e) {
			e.printStackTrace();
		}

    	ontologyManager.shutdown();
    }

    @Test
    public void testCreateClass() throws OntologyRuntimeException {

    	String className = "testCreateClass";

    	OntologyClass ontologyClass = new OntologyClass(className);
    	ontologyClass.add(new OntologyProperty("dummyPropertyInt", OntologyType.Int));
    	ontologyClass.add(new OntologyProperty("dummyPropertyBoolean", OntologyType.Boolean));
    	ontologyClass.add(new OntologyProperty("dummyPropertyDate", OntologyType.Date));
    	ontologyClass.add(new OntologyProperty("dummyPropertyDateTime", OntologyType.DateTime));
    	ontologyClass.add(new OntologyProperty("dummyPropertyTime", OntologyType.Time));
    	ontologyClass.add(new OntologyProperty("dummyPropertyFloat", OntologyType.Float));
    	ontologyClass.add(new OntologyProperty("dummyPropertyString", OntologyType.String));
    	ontologyManager.createClass(ontologyClass);

        OntologyClass response = ontologyManager.getOntologyClass(className);
        assertEquals(ontologyClass, response);

        ontologyManager.removeClass(className);
        Exception exception = null;
        try{
        	ontologyManager.getOntologyClass(className);
        }catch(RuntimeException exc){
        	exception = exc;
        }
        assertEquals(exception.getClass(), OntologyRuntimeException.class);
        assertEquals(ErrorMessages.ONTOLOGY_CLASS_DOESNT_EXIST.getMessage(className), exception.getMessage());

    }

    @Test
    public void testAddInstance() throws OntologyRuntimeException, ParseException {

    	OntologyClass ontologyClass = new OntologyClass("taiwp");
    	ontologyClass.add(new OntologyProperty("taiwpInt", OntologyType.Int));
    	ontologyClass.add(new OntologyProperty("taiwpBoolean", OntologyType.Boolean));
    	ontologyClass.add(new OntologyProperty("taiwpDate", OntologyType.Date));
    	ontologyClass.add(new OntologyProperty("taiwpDateTime", OntologyType.DateTime));
    	ontologyClass.add(new OntologyProperty("taiwpTime", OntologyType.Time));
    	ontologyClass.add(new OntologyProperty("taiwpFloat", OntologyType.Float));
    	ontologyClass.add(new OntologyProperty("taiwpString", OntologyType.String));

    	ontologyManager.createClass(ontologyClass);

    	Instance instance = new Instance(ontologyClass, "taiwpInstance");
    	instance.addProperty(new InstanceProperty<Integer>("taiwpInt", 23));
    	instance.addProperty(new InstanceProperty<Boolean>("taiwpBoolean", false));
    	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
    	SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	instance.addProperty(new InstanceProperty<Date>("taiwpDate", sdfDate.parse(sdfDate.format(new Date()))));
    	instance.addProperty(new InstanceProperty<Date>("taiwpDateTime", sdfDateTime.parse(sdfDateTime.format(new Date()))));
    	instance.addProperty(new InstanceProperty<Date>("taiwpTime", sdfTime.parse(sdfTime.format(new Date()))));
    	instance.addProperty(new InstanceProperty<Float>("taiwpFloat", 3.14F));
    	instance.addProperty(new InstanceProperty<String>("taiwpString", "dummy"));
    	// try to add instance
        ontologyManager.createInstance(instance);

        // verify, instance was correctly added
        Instance responseInstance = ontologyManager.getInstance(instance.getName());
        // verify, instance was added to given class
        // two properties should be returned - added in this test and ontology-related one (rdf:type)
        assertEquals(instance, responseInstance);
    }

    @Test
    public void testRemoveInstance() throws OntologyRuntimeException {
        // create a new instance in ontology
    	String className = "dummyClass_" + System.currentTimeMillis();
        String instanceName = "inst_" + className;
        OntologyClass baseClass = new OntologyClass(className);
        ontologyManager.createClass(baseClass);
        ontologyManager.createInstance(new Instance(baseClass, instanceName));
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
