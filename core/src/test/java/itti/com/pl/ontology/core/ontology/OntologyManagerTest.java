package itti.com.pl.ontology.core.ontology;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class OntologyManagerTest {

    public static final String ONTOLOGY_REPOSITORY = "src/test/resources";
    public static final String ONTOLOGY_LOCATION = "TestOntology.owl";
    public static final String ONTOLOGY_NAMESPACE = "http://www.owl-ontologies.com/Ontology1350654591.owl#";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static OntologyManager ontologyManager;

    @BeforeClass
    public static void beforeClass() throws OntologyRuntimeException, OntologyException {

    	LocalOntologyRepository ontologyRepository = new LocalOntologyRepository(ONTOLOGY_REPOSITORY);
    	ontologyManager = (OntologyManager) ontologyRepository.loadOntology(ONTOLOGY_LOCATION);
    	ontologyManager.setNamespace(ONTOLOGY_NAMESPACE);
    }

    @AfterClass
    public static void afterClass() {

    	saveOntology();
    	ontologyManager.shutdown();
    }

    private static void saveOntology() {
        try {
        	LocalOntologyRepository repo = new LocalOntologyRepository("/tmp");
			repo.saveOntology(ontologyManager, "test.owl");
		} catch (OntologyException e) {
			e.printStackTrace();
		}
	}

	@Test
    public void createClass() throws OntologyRuntimeException {

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
    public void addInstance() throws OntologyRuntimeException, ParseException {

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
    public void removeInstance() throws OntologyRuntimeException {
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
    public void removeNonExistingInstance() throws OntologyRuntimeException {

        // try to delete an non-existing instance
        String instanceName = "dummyBuilding_tt_" + System.currentTimeMillis();

        expectedException.expect(OntologyRuntimeException.class);
        expectedException.expectMessage(String.format(ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND.getMessage(), instanceName));
        ontologyManager.removeInstance(instanceName);
    }

    @Test
    public void addSubclass() throws OntologyRuntimeException {

    	OntologyClass parent = new OntologyClass("addSubclassParentClass");
    	ontologyManager.createClass(parent);

    	OntologyClass child = new OntologyClass("addSubclassChildClass");
    	child.setParentClass(parent.getName());
    	ontologyManager.createClass(child);

    	String parentInstance = "instanceParentClass";
    	String childInstance = "instanceChildClass";
    	ontologyManager.createInstance(new Instance(parent, parentInstance));
    	ontologyManager.createInstance(new Instance(child, childInstance));

    	saveOntology();
    	assertEquals(parent.getName(), ontologyManager.getInstanceClass(parentInstance));
    	assertEquals(child.getName(), ontologyManager.getInstanceClass(childInstance));

    	assertEquals(1, ontologyManager.getNonDirectInstances(parent.getName()).size());
    	assertEquals(1, ontologyManager.getDirectInstances(parent.getName()).size());
    	assertEquals(childInstance, ontologyManager.getNonDirectInstances(parent.getName()).get(0));
    	assertEquals(parentInstance, ontologyManager.getDirectInstances(parent.getName()).get(0));
    	assertEquals(parent.getName(), ontologyManager.getOntologyClass(child.getName()).getParent());
    }

    @Test
    @Ignore
    public void runSwrlEngine(){
    	ontologyManager.runSwrlEngine();
    }

    @Test
    public void query(){

    	OntologyClass ontologyClass = new OntologyClass("query");
    	ontologyClass.add(new OntologyProperty("queryInt", OntologyType.Int));
    	ontologyClass.add(new OntologyProperty("queryBoolean", OntologyType.Boolean));

    	ontologyManager.createClass(ontologyClass);

    	Instance instanceA = new Instance(ontologyClass, "queryInstanceA");
    	instanceA.addProperty(new InstanceProperty<Integer>("queryInt", 23));
    	instanceA.addProperty(new InstanceProperty<Boolean>("queryBoolean", false));
    	ontologyManager.createInstance(instanceA);

    	Instance instanceB = new Instance(ontologyClass, "queryInstanceB");
    	instanceB.addProperty(new InstanceProperty<Integer>("queryInt", 26));
    	instanceB.addProperty(new InstanceProperty<Boolean>("queryBoolean", false));
    	ontologyManager.createInstance(instanceB);

    	List<String> result = null;

    	List<InstanceProperty<?>> criteriaA = new ArrayList<>();
    	criteriaA.add(new InstanceProperty<Integer>("queryInt", 21));
    	result = ontologyManager.query(criteriaA);
    	assertTrue(result.isEmpty());

    	List<InstanceProperty<?>> criteriaB = new ArrayList<>();
    	criteriaB.add(new InstanceProperty<Integer>("queryInt", 23));
    	result = ontologyManager.query(criteriaB);
    	assertEquals(1, result.size());
    	assertEquals(instanceA.getName(), result.get(0));

    	List<InstanceProperty<?>> criteriaC = new ArrayList<>();
    	criteriaC.add(new InstanceProperty<Boolean>("queryBoolean", false));
    	result = ontologyManager.query(criteriaC);
    	assertEquals(2, result.size());
    	assertTrue(result.contains(instanceA.getName()));
    	assertTrue(result.contains(instanceB.getName()));

    	List<InstanceProperty<?>> criteriaD = new ArrayList<>();
    	criteriaD.add(new InstanceProperty<Integer>("queryInt", 26));
    	criteriaD.add(new InstanceProperty<Boolean>("queryBoolean", false));
    	result = ontologyManager.query(criteriaD);
    	assertEquals(1, result.size());
    	assertEquals(instanceB.getName(), result.get(0));
    }

    @Test
    public void queryClassProperty(){

    	OntologyClass ontologyClass = new OntologyClass("queryClassProp");
    	ontologyClass.add(new OntologyProperty("queryIntClassProp", OntologyType.Int));
    	ontologyManager.createClass(ontologyClass);

    	OntologyClass ontologyClass2 = new OntologyClass("queryClassProp2");
    	ontologyClass2.add(new OntologyProperty("queryInstClassProp2", OntologyType.Class, "queryClassProp"));
    	ontologyManager.createClass(ontologyClass2);

    	Instance instanceA = new Instance(ontologyClass, "queryInstanceClassA");
    	instanceA.addProperty(new InstanceProperty<Integer>("queryIntClassProp", 23));
    	ontologyManager.createInstance(instanceA);

    	Instance instanceB = new Instance(ontologyClass2, "queryInstanceClassB");
    	instanceB.addProperty(new InstanceProperty<Instance>("queryInstClassProp2", instanceA));
    	ontologyManager.createInstance(instanceB);

    }
}
