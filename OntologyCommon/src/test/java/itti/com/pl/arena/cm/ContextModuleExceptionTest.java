package itti.com.pl.arena.cm;

import itti.com.pl.ontology.common.exception.OntologyException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ContextModuleExceptionTest {

    private static final String EXCP_PARAM_1 = "param 1";
    private static final String EXCP_PARAM_2 = "param 2";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testContextModuleExceptionStringNull() throws OntologyException {

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage(OntologyException.class.getSimpleName());

        // correctly constructed exception, null message and params
        throw new TestContextModuleException(null, (Object[]) null);
    }

    @Test
    public void testContextModuleExceptionStringNullParams() throws OntologyException {

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage(OntologyException.class.getSimpleName());

        // correctly constructed exception, null message, not params
        throw new TestContextModuleException(null, EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringEmptyParams() throws OntologyException {

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not params
        throw new TestContextModuleException("", EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringNullThrowableEmptyParams() throws OntologyException {

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage(OntologyException.class.getSimpleName());

        // correctly constructed exception, empty message, not params
        throw new TestContextModuleException(null, (Throwable) null, EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringThrowableEmptyParams() throws OntologyException {

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not params
        throw new TestContextModuleException("", (Throwable) null, EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringThrowableValidParams() throws OntologyException {

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not throw, params
        throw new TestContextModuleException("", new RuntimeException(), EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringThrowableValidExcp() throws OntologyException {

        String msg = "values %s %s";

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage(String.format(msg, EXCP_PARAM_1, EXCP_PARAM_2));

        // correctly constructed exception, empty message, not throw, params
        throw new TestContextModuleException(msg, new RuntimeException(), EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringThrowableValidExcpNotFull() throws OntologyException {

        String msg = "values %s";

        expectedException.expect(OntologyException.class);
        expectedException.expectMessage(String.format(msg, EXCP_PARAM_1));

        // correctly constructed exception, empty message, not throw, params
        throw new TestContextModuleException(msg, new RuntimeException(), EXCP_PARAM_1, EXCP_PARAM_2);
    }

    private static class TestContextModuleException extends OntologyException {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public TestContextModuleException(String message) {
            super(message);
        }

        public TestContextModuleException(String message, Object... args) {
            super(message, args);
        }

        public TestContextModuleException(String message, Throwable throwable, Object... args) {
            super(message, throwable, args);
        }

    }
}
