package itti.com.pl.arena.cm.client;

/**
 * Properties to be used in client properties file
 * 
 * @author cm-admin
 * 
 */
public enum ClientPropertyNames {

    // URL of the broker
    brokerUrl,
    // port used by the client
    clientPort,
    // flag, if debug mode should be enabled (if 'true' client is going to wait for the response forever)
    debugMode,
    // how long (in ms) client should wait for the response
    responseWaitingTime, ;
}

/**
 * Defaults used by the client (can be overwritten by the properties)
 * 
 * @author cm-admin
 * 
 */
class ClientDefaults {
    public static final boolean DEFAULT_DEBUG_MODE = false;
    public static final int DEFAULT_WAITING_TIME = 5000;
    public static final int DEFAULT_CLIENT_PORT = 6655;
}
