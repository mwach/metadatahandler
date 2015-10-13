package itti.com.pl.ontology.common.exception;

/**
 * Messages used by all exceptions, which may be thrown by this module
 * 
 * @author cm-admin
 * 
 */
public enum ErrorMessages {

    DATE_TIME_HELPER_EMPTY_TIMESTAMP("Empty timestamp format provided"),
    DATE_TIME_HELPER_PARSER_FAILURE("Could not parse provided timestamp: '%d' using provided format: '%s'. Details: %s"),

    IO_HELPER_INTERRUPT_EXCEPTION("Could not read all the data from input stream because of interrupted thread. Details: %s"),
    IO_HELPER_IO_EXCEPTION("Could not read all the data from input stream. Details: %s"),
    IO_HELPER_INVLAID_ENCODING("Could not convert binary data into string because of invalid encoding: '%s'. Details: %s"),
    IO_HELPER_COULD_NOT_CLOSE_STREAM("Could not close data stream. Details: %s"),
    IO_HELPER_COULD_NOT_HTTP_CONNECTION("Could not close HTTP connection. Details: %s"),
    IO_HELPER_NO_INPUT_FILE_PROVIDED("Input file name not specified"),
    IO_HELPER_COULD_NOT_READ_DATA_FROM_FILE("Could not read data from the file: %s. Details: %s"),
    IO_HELPER_NO_OUTPUT_FILE_PROVIDED("Output file name not specified"),
    IO_HELPER_NO_OUTPUT_DATA_PROVIDED("No data to be saved in '%s' was provided"),
    IO_HELPER_COULD_NOT_WRITE_DATA_TO_FILE("Could not write data to file: %s. Details: %s"),
    NETWORK_HELPER_CANNOT_OBTAIN_IP("Could not determine IP address of the host: %s"),
    NETWORK_HELPER_CANNOT_DO_HTTP_REQUEST("Http request failed for URL: '%s'. Method: '%s. Details: %s"),
    PROPERTIES_HELPER_NO_FILE_PROVIDED("Empty properties file name provided"),
    PROPERTIES_HELPER_COULD_NOT_PARSE("Failed to load properties file '%s' Details: '%s'"),
    STRING_HELPER_CANNOT_ENCODE("Could not encode string '%s' using default charsed. Details: %s"),

    JSON_HELPER_CANNOT_SERIALIZE("Cannot serialize given object: %s into object. Details: %s"),
    JSON_HELPER_CANNOT_DESERIALIZE("Cannot deserialize given string: %s into object. Details: %s"),
    JSON_HELPER_CANNOT_GET_PROPERTY_VALUE_NULL_INPUT("Could not retrieve property value. Null or empty input string provided: %s"),
    JSON_HELPER_CANNOT_GET_PROPERTY_VALUE_NULL_PROP("Could not retrieve property value. Null or empty property name provided: %s"),

    LOCATION_HELPER_NULL_LOCATION("Null or empty location string provided"),
    LOCATION_HELPER_INVALID_FORMAT("Invalid format of location string: '%s'. Should be: 'longitude,latitude'"),
    LOCATION_HELPER_COULD_NOT_PARSE("Could not parse provided param: '%s' into double"), 
    
    ;

    /*
     * Descriptive error description
     */
    private String message;

    /**
     * Default constructor
     * 
     * @param message
     *            message to be displayed
     */
    private ErrorMessages(String message) {
        this.message = message;
    }

    /**
     * Returns message
     * 
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
