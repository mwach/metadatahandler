package itti.com.pl.ontology.common.exception;

/**
 * Base runtime exception thrown by the Context Module
 * 
 * @author cm-admin
 * 
 */
public class OntologyRuntimeException extends RuntimeException {

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
    public OntologyRuntimeException(String message, RuntimeException throwable, Object... args) {
        super(message == null ? OntologyRuntimeException.class.getSimpleName() : String.format(message, args), throwable);
    }

    /**
     * Base runtime exception class thrown by the ContextModule
     * 
     * @param message
     *            message
     * @param args
     *            list of message parameters
     */
    public OntologyRuntimeException(String message, Object... args) {
        super(message == null ? OntologyRuntimeException.class.getSimpleName() : String.format(message, args));
    }
}
