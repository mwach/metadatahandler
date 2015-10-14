package itti.com.pl.ontology.common.exception;

/**
 * Default exception thrown by the ContextModule All ContextModule exceptions should extend this one
 * 
 * @author mawa
 * 
 */
public abstract class OntologyException extends Exception {

    /**
     * Class UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Base exception class thrown by the ContextModule
     * 
     * @param message
     *            message
     * @param throwable
     *            exception
     * @param args
     *            list of message parameters
     */
    public OntologyException(String message, Throwable throwable, Object... args) {
        super(message == null ? OntologyException.class.getSimpleName() : String.format(message, args), throwable);
    }

    /**
     * Base exception class thrown by the ContextModule
     * 
     * @param message
     *            message
     * @param args
     *            list of message parameters
     */
    public OntologyException(String message, Object... args) {
        super(message == null ? OntologyException.class.getSimpleName() : String.format(message, args));
    }
}