package itti.com.pl.ontology.core.ontology;

import static org.junit.Assert.*;

import java.util.List;

import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.core.exception.ErrorMessages;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LocalOntologyRepositoryTest {

	private static final String REPO_LOCATION = "src/test/resources";
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void setRepositoryCalled() throws OntologyException{
		LocalOntologyRepository lor = new LocalOntologyRepository();
		lor.setRepositoryLocation(REPO_LOCATION);
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
		LocalOntologyRepository lor = new LocalOntologyRepository();
		lor.loadOntology(null);
	}

	@Test
	public void setFileNameNotProvided() throws OntologyException{
		expectedException.expect(OntologyException.class);
		expectedException.expectMessage(ErrorMessages.ONTOLOGY_EMPTY_FILE_NAME_PROVIDED.getMessage());
		LocalOntologyRepository lor = new LocalOntologyRepository();
		lor.setRepositoryLocation("some repo");
		lor.loadOntology(null);
	}

	@Test
	public void loadOntologyFailed() throws OntologyException{
		
		String fileName = "dummyLoad";
		expectedException.expect(OntologyException.class);
		expectedException.expectMessage(ErrorMessages.ONTOLOGY_CANNOT_LOAD.getMessage(fileName));
		LocalOntologyRepository lor = new LocalOntologyRepository();
		lor.setRepositoryLocation(REPO_LOCATION);
		lor.loadOntology(fileName);
	}

	@Test
	public void getListOfOntologiesFailed() throws OntologyException{
		
		String invalidRepo = "invalidRepo";
		expectedException.expect(OntologyException.class);
		expectedException.expectMessage(ErrorMessages.ONTOLOGY_REPO_CANNOT_ACCESS.getMessage(invalidRepo));
		LocalOntologyRepository lor = new LocalOntologyRepository();
		lor.setRepositoryLocation(invalidRepo);
		lor.getListOfOntologies();
	}

	@Test
	public void removeOntologyFailed() throws OntologyException{
		
		String fileName = "dummyRemove";
		expectedException.expect(OntologyException.class);
		expectedException.expectMessage(ErrorMessages.ONTOLOGY_CANNOT_REMOVE.getMessage(fileName));
		LocalOntologyRepository lor = new LocalOntologyRepository();
		lor.setRepositoryLocation(REPO_LOCATION);
		lor.removeOntology(fileName);
	}

	@Test
	public void saveOntologyFailed() throws OntologyException{
		
		String fileName = "dummySave";
		expectedException.expect(OntologyException.class);
		expectedException.expectMessage(ErrorMessages.ONTOLOGY_CANNOT_SAVE.getMessage(fileName));
		LocalOntologyRepository lor = new LocalOntologyRepository();
		lor.setRepositoryLocation(REPO_LOCATION);
		lor.saveOntology(null, fileName);
	}

}
