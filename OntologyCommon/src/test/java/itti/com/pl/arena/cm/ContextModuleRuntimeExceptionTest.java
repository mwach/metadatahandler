package itti.com.pl.arena.cm;

import itti.com.pl.ontology.common.exception.ContextModuleException;
import itti.com.pl.ontology.common.exception.ContextModuleRuntimeException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ContextModuleRuntimeExceptionTest {

    private static final String EXCP_PARAM_1 = "param 1";
    private static final String EXCP_PARAM_2 = "param 2";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testContextModuleRuntimeExceptionStringNull() throws ContextModuleException {

        expectedException.expect(ContextModuleRuntimeException.class);
        expectedException.expectMessage(ContextModuleRuntimeException.class.getSimpleName());

        // correctly constructed exception, null message and params
        throw new TestContextModuleRuntimeException(null, (Object[]) null);
    }

    @Test
    public void testContextModuleRuntimeExceptionStringNullParams() throws ContextModuleException {

        expectedException.expect(ContextModuleRuntimeException.class);
        expectedException.expectMessage(ContextModuleRuntimeException.class.getSimpleName());

        // correctly constructed exception, null message, not params
        throw new TestContextModuleRuntimeException(null, EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleRuntimeExceptionStringEmptyParams() throws ContextModuleException {

        expectedException.expect(ContextModuleRuntimeException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not params
        throw new TestContextModuleRuntimeException("", EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringNullThrowableEmptyParams() throws ContextModuleException {

        expectedException.expect(ContextModuleRuntimeException.class);
        expectedException.expectMessage(ContextModuleRuntimeException.class.getSimpleName());

        // correctly constructed exception, empty message, not params
        throw new TestContextModuleRuntimeException(null, (Throwable) null, EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringThrowableEmptyParams() throws ContextModuleException {

        expectedException.expect(ContextModuleRuntimeException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not params
        throw new TestContextModuleRuntimeException("", (Throwable) null, EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleExceptionStringThrowableValidParams() throws ContextModuleException {

        expectedException.expect(ContextModuleRuntimeException.class);
        expectedException.expectMessage("");

        // correctly constructed exception, empty message, not throw, params
        throw new TestContextModuleRuntimeException("", new RuntimeException(), EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleRuntimeExceptionStringThrowableValidExcp() throws ContextModuleException {

        String msg = "values %s %s";

        expectedException.expect(ContextModuleRuntimeException.class);
        expectedException.expectMessage(String.format(msg, EXCP_PARAM_1, EXCP_PARAM_2));

        // correctly constructed exception, empty message, not throw, params
        throw new TestContextModuleRuntimeException(msg, new RuntimeException(), EXCP_PARAM_1, EXCP_PARAM_2);
    }

    @Test
    public void testContextModuleRuntimeExceptionStringThrowableValidExcpNotFull() throws ContextModuleException {

        String msg = "values %s";

        expectedException.expect(ContextModuleRuntimeException.class);
        expectedException.expectMessage(String.format(msg, EXCP_PARAM_1));

        // correctly constructed exception, empty message, not throw, params
        throw new TestContextModuleRuntimeException(msg, new RuntimeException(), EXCP_PARAM_1, EXCP_PARAM_2);
    }

    private static class TestContextModuleRuntimeException extends ContextModuleRuntimeException {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public TestContextModuleRuntimeException(String message) {
            super(message);
        }

        public TestContextModuleRuntimeException(String message, Object... args) {
            super(message, args);
        }

        public TestContextModuleRuntimeException(String message, RuntimeException throwable, Object... args) {
            super(message, throwable, args);
        }

    }
}
