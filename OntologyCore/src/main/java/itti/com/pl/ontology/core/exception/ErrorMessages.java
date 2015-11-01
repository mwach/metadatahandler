package itti.com.pl.ontology.core.exception;

/**
 * Messages used by all exceptions, which may be thrown by this module
 * 
 * @author cm-admin
 * 
 */
public enum ErrorMessages {

    ONTOLOGY_CANNOT_LOAD(200, "Failed to load ontology '%s'"),
    ONTOLOGY_CLASS_DOESNT_EXIST(201, "Class '%s' not found in ontology"),
    ONTOLOGY_COULD_NOT_ADD_INSTANCE(202, "Could not add instance '%s' of class '%s' to the ontology"),
    ONTOLOGY_EMPTY_INSTANCE_NAME(203, "Empty instance name was provided"),
    ONTOLOGY_INSTANCE_NOT_FOUND(204, "Instance '%s' not found in the ontology"),
    ONTOLOGY_EMPTY_FILE_NAME_PROVIDED(205, "Ontology file name was not provided"),
    ONTOLOGY_EMPTY_PARKING_ID_OBJECT(206, "Null parkingId was provided"),
    ONTOLOGY_INSTANCE_IS_NOT_A_PARKING(207, "Provided object '%s' does not represent a parking lot"),
    ONTOLOGY_EMPTY_VALUE_PROVIDED(208, "Null or empty value provided for ontology variable '%s'"),
    ONTOLOGY_COULD_NOT_FIND_PARENT_CLASS(209, "Could not find class for instance '%s'"),
    ONTOLOGY_CANNOT_SAVE(210, "Failed to save ontology '%s'. Details: %s"),
    ONTOLOGY_REPO_UNDEFINED(211, "Ontology repository is not defined."),
    ONTOLOGY_REPO_CANNOT_ACCESS(212, "Cannot access ontology repository: %s."),
    ONTOLOGY_CLASS_WITH_PROPERTY_NOT_FOUND(213, "No instance found with given instance properry: %s"),
    ONTOLOGY_CLASS_ALREADY_EXIST(214, "Class '%s' already defined in ontology"),
    ONTOLOGY_CANNOT_REMOVE(215, "Failed to remove ontology '%s'"),

    SWRL_CANNOT_ADD_RULE(800, "Cannot add SWRL rule to the ontology.Details: %s"),
    SWRL_EMPTY_RULE(801, "An attempt to add empty rule to ontology"),
    SWRL_ENGINE_FAILED(802, "Could not run SWRL engine. Details: %s"),
    SWRL_CANNOT_COLLECT_RULES(803, "Could not collect rule names from ontology. Details: %s"),
    SWRL_CANNOT_GET_RULE(804, "Cannot find SWRL rule identified by it's name: '%s'"), 
    ;

    /*
     * Unique ID of the error
     */
    private int id;

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
    private ErrorMessages(int id, String message) {
        this.id = id;
        this.message = message;
    }

    /**
     * Returns ID
     * 
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns message
     * 
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns message
     * 
     * @return message
     */
    public String getMessage(Object...args) {
        return String.format(message, args);
    }
}