package itti.com.pl.ontology.common.exception;

import itti.com.pl.ontology.common.exception.OntologyException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class OntologyExceptionTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testContextModuleExceptionStringEmptyParams() throws OntologyException {

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not params
        throw new OntologyException("");
    }

    @Test
    public void testContextModuleExceptionStringNullThrowableEmptyParams() throws OntologyException {

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage("dummy");

        // correctly constructed exception, empty message, not params
        throw new OntologyException("dummy", new RuntimeException());
    }
}
