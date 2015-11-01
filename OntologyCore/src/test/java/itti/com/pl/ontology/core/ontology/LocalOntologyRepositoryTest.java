package itti.com.pl.ontology.core.ontology;

import static org.junit.Assert.*;

import java.util.List;

import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.core.exception.ErrorMessages;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LocalOntologyRepositoryTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void setRepositoryCalled() throws OntologyException{
		LocalOntologyRepository lor = new LocalOntologyRepository();
		lor.setRepositoryLocation("src/test/resources");
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
}
