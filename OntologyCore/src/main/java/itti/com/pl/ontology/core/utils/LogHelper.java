package itti.com.pl.ontology.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging utility helper class
 * 
 * @author cm-admin
 * 
 */
public final class LogHelper {

    private LogHelper() {
    }

//    static {
//        initLogger();
//    }

    /**
     * Initializes the logger
     */
//    private static void initLogger() {
//        try {
//            LogManager.getLogManager().reset();
//            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("logging.properties");
//            LogManager.getLogManager().readConfiguration(is);
//        } catch (RuntimeException | IOException exc) {
//            System.out.println(String.format("Could not initialize logger: %s", exc.getLocalizedMessage()));
//        }
//    }

    /**
     * Writes information about exception in the trace
     * 
     * @param clazz
     *            class throwing an exception
     * @param method
     *            name of the method throwing an exception
     * @param message
     *            message to be logged
     * @param throwable
     *            exception
     */
    public static void exception(Class<?> clazz, String method, String message, Throwable throwable) {

    	Logger logger = LoggerFactory.getLogger(clazz);
    	if(logger.isErrorEnabled()){
            String formattedMessage = String.format("[%s:%s][Thread:%d] %s", clazz.getName(), method, Thread.currentThread().getId(), message);
    		logger.error(formattedMessage, throwable);
    	}
    }


    /**
     * Writes WARNING level message in the trace
     * 
     * @param clazz
     *            class throwing an exception
     * @param method
     *            name of the method throwing an exception
     * @param msg
     *            message to be logged
     * @param args
     *            optional message arguments
     */
    public static void warning(Class<?> clazz, String method, String message, Object... args) {
    	Logger logger = LoggerFactory.getLogger(clazz);
    	if(logger.isWarnEnabled()){
            String formattedMessage = String.format("[%s:%s][Thread:%d] %s", clazz.getName(), method, Thread.currentThread().getId(), message);
    		logger.warn(formattedMessage);
    	}
    }

    /**
     * Writes INFO level message in the trace
     * 
     * @param clazz
     *            class throwing an exception
     * @param method
     *            name of the method throwing an exception
     * @param msg
     *            message to be logged
     * @param args
     *            optional message arguments
     */
    public static void info(Class<?> clazz, String method, String message, Object... args) {
    	Logger logger = LoggerFactory.getLogger(clazz);
    	if(logger.isInfoEnabled()){
            String formattedMessage = String.format("[%s:%s][Thread:%d] %s", clazz.getName(), method, Thread.currentThread().getId(), message);
    		logger.info(formattedMessage);
    	}
    }

    /**
     * Writes DEBUG level message in the trace
     * 
     * @param clazz
     *            class throwing an exception
     * @param method
     *            name of the method throwing an exception
     * @param msg
     *            message to be logged
     * @param args
     *            optional message arguments
     */
    public static void debug(Class<?> clazz, String method, String message, Object... args) {
    	Logger logger = LoggerFactory.getLogger(clazz);
    	if(logger.isDebugEnabled()){
            String formattedMessage = String.format("[%s:%s][Thread:%d] %s", clazz.getName(), method, Thread.currentThread().getId(), message);
    		logger.debug(formattedMessage);
    	}
    }

	public static void error(Class<?> clazz, String method,
			String message, Object... args) {
    	Logger logger = LoggerFactory.getLogger(clazz);
    	if(logger.isErrorEnabled()){
            String formattedMessage = String.format("[%s:%s][Thread:%d] %s", clazz.getName(), method, Thread.currentThread().getId(), message);
    		logger.error(formattedMessage);
    	}
	}

}
