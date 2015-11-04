package itti.com.pl.ontology.common.exception;

import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.common.exception.OntologyRuntimeException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class OntologyRuntimeExceptionTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testContextModuleRuntimeExceptionStringNull() throws OntologyException {

        expectedException.expect(OntologyRuntimeException.class);
        expectedException.expectMessage("dummy");

        // correctly constructed exception, null message and params
        throw new OntologyRuntimeException("dummy", new RuntimeException());
    }

    @Test
    public void testContextModuleExceptionStringThrowableValidParams() throws OntologyException {

        expectedException.expect(OntologyRuntimeException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not throw, params
        throw new OntologyRuntimeException("", new RuntimeException());
    }
}
