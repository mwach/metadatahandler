package itti.com.pl.ontology.common.exception;

import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.common.exception.OntologyRuntimeException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ContextModuleRuntimeExceptionTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testContextModuleRuntimeExceptionStringNull() throws OntologyException {

        expectedException.expect(OntologyRuntimeException.class);
        expectedException.expectMessage(OntologyRuntimeException.class.getSimpleName());

        // correctly constructed exception, null message and params
        throw new TestContextModuleRuntimeException(null);
    }


    @Test
    public void testContextModuleExceptionStringNullThrowableEmptyParams() throws OntologyException {

        expectedException.expect(OntologyRuntimeException.class);
        expectedException.expectMessage(OntologyRuntimeException.class.getSimpleName());

        // correctly constructed exception, empty message, not params
        throw new TestContextModuleRuntimeException(null, null);
    }

    @Test
    public void testContextModuleExceptionStringThrowableValidParams() throws OntologyException {

        expectedException.expect(OntologyRuntimeException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not throw, params
        throw new TestContextModuleRuntimeException("", new RuntimeException());
    }


    private static class TestContextModuleRuntimeException extends OntologyRuntimeException {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public TestContextModuleRuntimeException(String message) {
            super(message);
        }

        public TestContextModuleRuntimeException(String message, RuntimeException throwable) {
            super(message, throwable);
        }

    }
}
