package itti.com.pl.ontology.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Logging utility helper class
 * 
 * @author cm-admin
 * 
 */
public final class LogHelper {

    private LogHelper() {
    }

    static {
        initLogger();
    }

    /**
     * Initializes the logger
     */
    private static void initLogger() {
        try {
            LogManager.getLogManager().reset();
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("logging.properties");
            LogManager.getLogManager().readConfiguration(is);
        } catch (RuntimeException | IOException exc) {
            System.out.println(String.format("Could not initialize logger: %s", exc.getLocalizedMessage()));
        }
    }

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
        log(clazz, Level.SEVERE, throwable, method, message);
    }

    /**
     * Writes ERROR level message in the trace
     * 
     * @param clazz
     *            class throwing an exception
     * @param method
     *            name of the method throwing an exception
     * @param message
     *            message to be logged
     * @param args
     *            optional message arguments
     */
    public static void error(Class<?> clazz, String method, String message, Object... args) {
        log(clazz, Level.SEVERE, null, method, message, args);
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
    public static void warning(Class<?> clazz, String method, String msg, Object... args) {
        log(clazz, Level.WARNING, null, method, msg, args);
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
    public static void info(Class<?> clazz, String method, String msg, Object... args) {
        log(clazz, Level.INFO, null, method, msg, args);
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
    public static void debug(Class<?> clazz, String method, String msg, Object... args) {
        log(clazz, Level.FINE, null, method, msg, args);
    }

    /**
     * Generic method used to write message to the trace
     * 
     * @param clazz
     *            class throwing an exception
     * @param level
     *            trace level
     * @param exception
     *            true, if exception is logged, false otherwise
     * @param method
     *            name of the method throwing an exception
     * @param message
     *            message to be logged
     * @param args
     *            optional message arguments
     */
    private static void log(Class<?> clazz, Level level, Throwable exception, String method, String message, Object... args) {

        // get the associated logger
        Logger logger = Logger.getLogger(clazz.getPackage().getName());

        // optimization - don't waste time for formatting strings that will not
        // be logged
        if (logger.isLoggable(level)) {
            // add thread ID to all logs
            long threadId = Thread.currentThread().getId();
            String formattedMessage = String.format("[%s:%s][Thread:%d] %s", clazz.getName(), method, threadId, message);

            // exceptions first
            if (exception != null) {
                logger.log(level, formattedMessage, exception);
                // standard log messages
            } else {
                // check for NULL parameters
                for (int i = 0; i < args.length; i++) {
                    if (args[i] == null) {
                        args[i] = String.valueOf(null);
                    }
                }
                String logMessage = String.format(formattedMessage, args);
                logger.log(level, logMessage);
            }
        }
    }

}
