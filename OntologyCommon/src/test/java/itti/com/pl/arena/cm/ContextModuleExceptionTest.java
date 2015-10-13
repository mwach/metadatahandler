package itti.com.pl.arena.cm;

import itti.com.pl.ontology.common.exception.ContextModuleException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ContextModuleExceptionTest {

    private static final String EXCP_PARAM_1 = "param 1";
    private static final String EXCP_PARAM_2 = "param 2";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testContextModuleExceptionStringNull() throws ContextModuleException {

        expectedException.expect(ContextModuleException.class);
        expectedException.expectMessage(ContextModuleException.class.getSimpleName());

        // correctly constructed exception, null message and params
        throw new TestContextModuleException(null, (Object[]) null);
    }

    @Test
    public void testContextModuleExceptionStringNullParams() throws ContextModuleException {

        expectedException.expect(ContextModuleException.class);
        expectedException.expectMessage(ContextModuleException.class.getSimpleName());

        // correctly constructed exception, null message, not params
        throw new TestContextModuleException(null, EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringEmptyParams() throws ContextModuleException {

        expectedException.expect(ContextModuleException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not params
        throw new TestContextModuleException("", EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringNullThrowableEmptyParams() throws ContextModuleException {

        expectedException.expect(ContextModuleException.class);
        expectedException.expectMessage(ContextModuleException.class.getSimpleName());

        // correctly constructed exception, empty message, not params
        throw new TestContextModuleException(null, (Throwable) null, EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringThrowableEmptyParams() throws ContextModuleException {

        expectedException.expect(ContextModuleException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not params
        throw new TestContextModuleException("", (Throwable) null, EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringThrowableValidParams() throws ContextModuleException {

        expectedException.expect(ContextModuleException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not throw, params
        throw new TestContextModuleException("", new RuntimeException(), EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringThrowableValidExcp() throws ContextModuleException {

        String msg = "values %s %s";

        expectedException.expect(ContextModuleException.class);
        expectedException.expectMessage(String.format(msg, EXCP_PARAM_1, EXCP_PARAM_2));

        // correctly constructed exception, empty message, not throw, params
        throw new TestContextModuleException(msg, new RuntimeException(), EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringThrowableValidExcpNotFull() throws ContextModuleException {

        String msg = "values %s";

        expectedException.expect(ContextModuleException.class);
        expectedException.expectMessage(String.format(msg, EXCP_PARAM_1));

        // correctly constructed exception, empty message, not throw, params
        throw new TestContextModuleException(msg, new RuntimeException(), EXCP_PARAM_1, EXCP_PARAM_2);
    }

    private static class TestContextModuleException extends ContextModuleException {

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
