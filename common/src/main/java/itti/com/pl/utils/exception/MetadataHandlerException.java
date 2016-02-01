package itti.com.pl.utils.exception;

public class MetadataHandlerException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetadataHandlerException(String message, Throwable throwable){
		super(message, throwable);
	}

	public MetadataHandlerException(String message) {
		super(message);
	}
}
