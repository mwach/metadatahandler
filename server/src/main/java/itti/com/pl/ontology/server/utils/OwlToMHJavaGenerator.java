package itti.com.pl.ontology.server.utils;

import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.core.ontology.LocalOntologyRepository;
import itti.com.pl.ontology.core.ontology.Ontology;
import itti.com.pl.ontology.core.utils.OwlToJavaGenerator;
import itti.com.pl.ontology.server.Constants.Class;

public class OwlToMHJavaGenerator {

	private static final String OUTPUT_LOCATION = "/home/marcin/workspace/MetadataHandler/mh-common/src/main/java/itti/com/pl/ontology/common/dto/";

	public static void main(String[] args) throws OntologyException {
		
		OwlToJavaGenerator generator = new OwlToJavaGenerator();
		Ontology ontology = new LocalOntologyRepository("/var/local/metadatahandler").loadOntology("aao_service.owl");

		Class[] classesToGenerate = new Class[]{
				Class.Service, 
				Class.Device,
				Class.Network
		};

		for (Class outputClassName : classesToGenerate) {
			String outputFileName = String.format("%s%s.java", OUTPUT_LOCATION, outputClassName.getOntologyName());
			generator.generateJavaClass(ontology, outputClassName.getOntologyName(), outputClassName.getOntologyName(), outputFileName);
		}
	}
}
