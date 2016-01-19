package itti.com.pl.ontology.core.exception;

/**
 * Messages used by all exceptions, which may be thrown by this module
 * 
 * @author cm-admin
 * 
 */
public enum ErrorMessages {

    ONTOLOGY_CANNOT_LOAD(200, "Failed to load ontology '%s'"),
    ONTOLOGY_CANNOT_SAVE(201, "Failed to save ontology '%s'"),
    ONTOLOGY_CANNOT_REMOVE(202, "Failed to remove ontology '%s'"),
    ONTOLOGY_REPO_UNDEFINED(203, "Ontology repository is not defined."),
    ONTOLOGY_REPO_CANNOT_ACCESS(204, "Cannot access ontology repository: %s."),
    ONTOLOGY_EMPTY_FILE_NAME_PROVIDED(205, "Ontology file name was not provided"),

    ONTOLOGY_CLASS_DOESNT_EXIST(301, "Class '%s' not found in ontology"),
    ONTOLOGY_COULD_NOT_ADD_INSTANCE(302, "Could not add instance '%s' of class '%s' to the ontology"),
    ONTOLOGY_EMPTY_INSTANCE_NAME(303, "Empty instance name was provided"),
    ONTOLOGY_INSTANCE_NOT_FOUND(304, "Instance '%s' not found in the ontology"),
    ONTOLOGY_EMPTY_VALUE_PROVIDED(305, "Null or empty value provided for ontology variable '%s'"),
    ONTOLOGY_COULD_NOT_FIND_PARENT_CLASS(306, "Could not find class for instance '%s'"),
    ONTOLOGY_CLASS_WITH_PROPERTY_NOT_FOUND(307, "No instance found with given instance properry: %s"),
    ONTOLOGY_CLASS_ALREADY_EXIST(308, "Class '%s' already defined in ontology"),
    ONTOLOGY_INVALID_INSTANCE_CORE(309, "Value '%s' is neither an ontology instance, nor simple type"),
    ONTOLOGY_INSTANCE_ALREADY_EXIST(310, "Instance '%s' already defined in ontology"),
    ONTOLOGY_PROPERTY_NOT_FOUND_FOR_INSTANCE(311, "Property '%s' was not found for instance '%s'"),

    SWRL_CANNOT_ADD_RULE(800, "Cannot add SWRL rule to the ontology.Details: %s"),
    SWRL_EMPTY_RULE(801, "An attempt to add empty rule to ontology"),
    SWRL_ENGINE_FAILED(802, "Could not run SWRL engine. Details: %s"),
    SWRL_CANNOT_COLLECT_RULES(803, "Could not collect rule names from ontology. Details: %s"),
    SWRL_CANNOT_GET_RULE(804, "Cannot find SWRL rule identified by it's name: '%s'"), 
    
    REFLECTION_ISSUE_METHOD(900, "Reflection issue occured for method '%s'"), 
    REFLECTION_ISSUE_CLASS(901, "Reflection issue occured for instance '%s'"), 

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