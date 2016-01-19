package itti.com.pl.ontology.core.ontology;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.core.exception.ErrorMessages;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@Ignore
public class LocalOntologyRepositoryTest {

	private static final String REPO_LOCATION = "src/test/resources/tmp";
	private static final String TEST_ONTOLOGY = "dummy.owl";

	@BeforeClass
	public static void beforeClass() throws IOException{
		new File(REPO_LOCATION).mkdir();
		new File(REPO_LOCATION, TEST_ONTOLOGY).createNewFile();
	}
	
	@AfterClass
	public static void afterClass() throws IOException{
		new File(REPO_LOCATION, TEST_ONTOLOGY).delete();
		new File(REPO_LOCATION).delete();
	}
	

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void setRepositoryCalled() throws OntologyException{
		LocalOntologyRepository lor = new LocalOntologyRepository(REPO_LOCATION);
		List<String> ontologies = lor.getListOfOntologies();
		assertEquals(1, ontologies.size());
		String ontologyFileName = ontologies.get(0);

		Ontology model = lor.loadOntology(ontologyFileName);
		assertNotNull(model);

		String savedFileName = String.format("copy_%s", ontologyFileName);
		lor.saveOntology(model, savedFileName);
		List<String> ontologiesAfter = lor.getListOfOntologies();
		assertEquals(2, ontologiesAfter.size());
		lor.removeOntology(savedFileName);

		List<String> ontologiesAfterAfter = lor.getListOfOntologies();
		assertEquals(1, ontologiesAfterAfter.size());
		assertEquals(ontologyFileName, ontologiesAfterAfter.get(0));
	}

	@Test
	public void setRepositoryNotCalled() throws OntologyException{
		expectedException.expect(OntologyException.class);
		expectedException.expectMessage(ErrorMessages.ONTOLOGY_REPO_UNDEFINED.getMessage());
		LocalOntologyRepository lor = new LocalOntologyRepository(null);
		lor.loadOntology(null);
	}

	@Test
	public void setFileNameNotProvided() throws OntologyException{
		expectedException.expect(OntologyException.class);
		expectedException.expectMessage(ErrorMessages.ONTOLOGY_EMPTY_FILE_NAME_PROVIDED.getMessage());
		LocalOntologyRepository lor = new LocalOntologyRepository("some repo");
		lor.loadOntology(null);
	}

	@Test
	public void loadOntologyFailed() throws OntologyException{
		
		String fileName = "dummyLoad";
		expectedException.expect(OntologyException.class);
		expectedException.expectMessage(ErrorMessages.ONTOLOGY_CANNOT_LOAD.getMessage(fileName));
		LocalOntologyRepository lor = new LocalOntologyRepository(REPO_LOCATION);
		lor.loadOntology(fileName);
	}

	@Test
	public void getListOfOntologiesFailed() throws OntologyException{
		
		String invalidRepo = "invalidRepo";
		expectedException.expect(OntologyException.class);
		expectedException.expectMessage(ErrorMessages.ONTOLOGY_REPO_CANNOT_ACCESS.getMessage(invalidRepo));
		LocalOntologyRepository lor = new LocalOntologyRepository(invalidRepo);
		lor.getListOfOntologies();
	}

	@Test
	public void removeOntologyFailed() throws OntologyException{
		
		String fileName = "dummyRemove";
		expectedException.expect(OntologyException.class);
		expectedException.expectMessage(ErrorMessages.ONTOLOGY_CANNOT_REMOVE.getMessage(fileName));
		LocalOntologyRepository lor = new LocalOntologyRepository(REPO_LOCATION);
		lor.removeOntology(fileName);
	}

	@Test
	public void saveOntologyFailed() throws OntologyException{
		
		String fileName = "dummySave";
		expectedException.expect(OntologyException.class);
		expectedException.expectMessage(ErrorMessages.ONTOLOGY_CANNOT_SAVE.getMessage(fileName));
		LocalOntologyRepository lor = new LocalOntologyRepository(REPO_LOCATION);
		lor.saveOntology(null, fileName);
	}

}
