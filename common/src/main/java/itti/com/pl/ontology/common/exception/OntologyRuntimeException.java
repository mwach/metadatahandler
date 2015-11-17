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
     */
    public OntologyRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Base runtime exception class thrown by the ContextModule
     * 
     * @param message
     *            message
     */
    public OntologyRuntimeException(String message) {
        super(message);
    }
}
