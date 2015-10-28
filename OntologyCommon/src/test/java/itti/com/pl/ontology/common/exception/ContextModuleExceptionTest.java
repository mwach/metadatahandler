package itti.com.pl.ontology.common.exception;

import itti.com.pl.ontology.common.exception.OntologyException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ContextModuleExceptionTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testContextModuleExceptionStringNull() throws OntologyException {

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage(OntologyException.class.getSimpleName());

        // correctly constructed exception, null message and params
        throw new TestContextModuleException(null);
    }

    @Test
    public void testContextModuleExceptionStringEmptyParams() throws OntologyException {

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not params
        throw new TestContextModuleException("");
    }

    @Test
    public void testContextModuleExceptionStringNullThrowableEmptyParams() throws OntologyException {

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage(OntologyException.class.getSimpleName());

        // correctly constructed exception, empty message, not params
        throw new TestContextModuleException(null, null);
    }


    @Test
    public void testContextModuleExceptionStringThrowableValidParams() throws OntologyException {

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not throw, params
        throw new TestContextModuleException("", new RuntimeException());
    }


    private static class TestContextModuleException extends OntologyException {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public TestContextModuleException(String message) {
            super(message);
        }

        public TestContextModuleException(String message, Throwable throwable) {
            super(message, throwable);
        }

    }
}
