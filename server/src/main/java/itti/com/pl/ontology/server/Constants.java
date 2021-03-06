package itti.com.pl.ontology.server;

/**
 * Names of the properties used by the application
 * Can be used in optional, external .properties file in order to overwrite default properties
 * @author marcin
 *
 */
public final class Constants {

	private Constants(){
	}

	/**
	 * Location of the ontology repository
	 * Default: /var/local/metadatahandler/
	 */
	public static final String ONTOLOGY_REPOSITORY = "ontologyRepository";

	/**
	 * Name of the OWL ontology file
	 * Default: MinimalOntology.owl
	 */
	public static final String ONTOLOGY = "ontology";

	/**
	 * Main ontology namespace
	 * Default: http://www.owl-ontologies.com/Ontology1350654591.owl#
	 */
	public static final String NAMESPACE = "namespace";

	/**
	 * Publishing address for the Metadata Handler web service
	 * Default: http://localhost:9000/metadataHandler
	 */
	public static final String ADDRESS = "address";

	/**
	 * Publishing address for the Metadata Handler Admin web service
	 * Default: http://localhost:9000/metadataHandlerAdmin
	 */
	public static final String ADDRESS_ADMIN = "addressAdmin";


	public static final String TSI_NODE_CLASS_NAME = "Service";

	public enum Property{
		TypeOfNode("Type_of_node");

		private String ontologyName;

		private Property(String ontologyName){
			this.ontologyName = ontologyName;
		}
		public String getOntologyName(){
			return ontologyName;
		}
	}
}
