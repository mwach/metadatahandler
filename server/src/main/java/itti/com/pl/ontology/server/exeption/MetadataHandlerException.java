package itti.com.pl.ontology.server.exeption;

/**
 * Default exception class used by the Metadata Handler
 * @author marcin
 *
 */
public class MetadataHandlerException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor class
	 * @param message message
	 * @param throwable throwable
	 */
	public MetadataHandlerException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
