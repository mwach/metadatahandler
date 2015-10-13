package itti.com.pl.ontology.server.helpers;

import itti.com.pl.ontology.common.exception.ContextModuleException;
import itti.com.pl.ontology.server.exception.ErrorMessages;

/**
 * Exception thrown by the {@link SpringHelper} class
 * 
 * @author mawa
 * 
 */
public class SpringHelperException extends ContextModuleException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Exception, which may be thrown by the {@link SpringHelper} class
     * 
     * @param reason
     *            base exception
     * @param errorMsg
     *            exception details
     * @param params
     *            additional parameters used to construct errorMsg
     */
    public SpringHelperException(Throwable reason, ErrorMessages errorMsg, Object... params) {
        super(String.format(errorMsg.getMessage(), params), reason);
    }

    /**
     * Exception, which may be thrown by the {@link SpringHelper} class
     * 
     * @param errorMsg
     *            exception details
     * @param params
     *            additional parameters used to construct errorMsg
     */
    public SpringHelperException(ErrorMessages errorMsg, Object... params) {
        super(String.format(errorMsg.getMessage(), params));
    }
}
