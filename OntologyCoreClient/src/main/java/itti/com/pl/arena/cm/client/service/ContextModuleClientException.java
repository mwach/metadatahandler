package itti.com.pl.arena.cm.client.service;

import itti.com.pl.ontology.common.exception.OntologyException;

/**
 * Client-side exception
 * 
 * @author cm-admin
 * 
 */
public class ContextModuleClientException extends OntologyException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ContextModuleClientException(String message, Throwable throwable, Object... args) {
        super(message, throwable, args);
    }

    public ContextModuleClientException(String message) {
        super(message);
    }
}
