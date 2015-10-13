package itti.com.pl.ontology.common.exception;

/**
 * Base runtime exception thrown by the Context Module
 * 
 * @author cm-admin
 * 
 */
public class ContextModuleRuntimeException extends RuntimeException {

    /**
     * Class UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Base runtime exception class thrown by the ContextModule
     * 
     * @param message
     *            message
     * @param throwable
     *            throwable
     * @param args
     *            list of message parameters
     */
    public ContextModuleRuntimeException(String message, RuntimeException throwable, Object... args) {
        super(message == null ? ContextModuleRuntimeException.class.getSimpleName() : String.format(message, args), throwable);
    }

    /**
     * Base runtime exception class thrown by the ContextModule
     * 
     * @param message
     *            message
     * @param args
     *            list of message parameters
     */
    public ContextModuleRuntimeException(String message, Object... args) {
        super(message == null ? ContextModuleRuntimeException.class.getSimpleName() : String.format(message, args));
    }
}
