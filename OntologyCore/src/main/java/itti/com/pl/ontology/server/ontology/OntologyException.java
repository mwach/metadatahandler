package itti.com.pl.ontology.server.ontology;

import itti.com.pl.ontology.common.exception.ContextModuleException;
import itti.com.pl.ontology.server.exception.ErrorMessages;

/**
 * Exception thrown by the Ontology module
 * 
 * @author cm-admin
 * 
 */
public class OntologyException extends ContextModuleException {

    /**
     * ID of the class
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default exception
     * 
     * @param message
     *            message
     * @param args
     *            optional message arguments
     */
    public OntologyException(ErrorMessages message, Object... args) {
        super(message.getMessage(), args);
    }

    /**
     * Default exception
     * 
     * @param throwable
     *            throwable
     * @param message
     *            message
     * @param args
     *            optional message arguments
     */
    public OntologyException(ErrorMessages message, Throwable throwable, Object... args) {
        super(message.getMessage(), throwable, args);
    }
}
