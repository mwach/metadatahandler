package itti.com.pl.ontology.core.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import itti.com.pl.ontology.common.bean.OntologyClass;
import itti.com.pl.ontology.common.bean.OntologyProperty;
import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.core.ontology.LocalOntologyRepository;
import itti.com.pl.ontology.core.ontology.Ontology;

public class OwlToJavaGenerator {

	public static void main(String[] args) throws OntologyException {
		OwlToJavaGenerator generator = new OwlToJavaGenerator();
		Ontology ontology = new LocalOntologyRepository("/var/local/metadatahandler").loadOntology("aao_service.owl");
		generator.generateJavaClass(ontology, "Service", "Service");
	}

	public void generateJavaClass(Ontology ontology, String javaClassName, String ontologyClassName, String outputFileName){
		String content = generateJavaClass(ontology, javaClassName, ontologyClassName);
		writeContentToFile(content, outputFileName);
	}

	public String generateJavaClass(Ontology ontology, String javaClassName, String ontologyClassName){

		String classContent = getTemplateContent("../core/src/main/resources/OwlToJavaStub.java");
		OntologyClass ontologyClass = ontology.getOntologyClass(ontologyClassName);
		List<String> properties = new ArrayList<>();
		List<String> constants = new ArrayList<>();
		List<String> methods = new ArrayList<>();
		for (OntologyProperty property : ontologyClass.getProperties()) {
			properties.add(property.getName());
		}

		Collections.sort(properties);
		
		for (String property : properties) {
			constants.add(getConstantName(property));
			methods.add(getMethodName(property));			
		}

		StringBuilder staticSB = new StringBuilder();
		int pos = 0;
		for (String string : constants) {
			staticSB.append(String.format("	private static final String %s = \"%s\";\n", string, properties.get(pos++)));
		}

		StringBuilder methodsSB = new StringBuilder();
		pos = 0;
		for (String method : methods) {
			methodsSB.append(String.format("	public void set%s(String %s){\n", method, properties.get(pos)));
			methodsSB.append(String.format("		setProperty(%s, %s);\n", constants.get(pos), properties.get(pos)));
			methodsSB.append("	}\n");

			methodsSB.append(String.format("	public String get%s(){\n", method));
			methodsSB.append(String.format("		return getProperty(%s);\n", constants.get(pos++)));
			methodsSB.append("	}\n\n");
		}

		classContent = classContent.replace("%CLASSNAME%", javaClassName);
		classContent = classContent.replace("%CONSTANTS%", staticSB.toString());
		classContent = classContent.replace("%METHODS%", methodsSB.toString());
		return classContent;
	}

	private String getTemplateContent(String stubFile) {
		StringBuilder content = new StringBuilder();
		try(InputStream is = new FileInputStream(stubFile)){
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while((line = br.readLine()) != null){
				content.append(line);
				content.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	private void writeContentToFile(String content, String fileName) {
		try(OutputStream os = new FileOutputStream(fileName)){
			os.write(content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getMethodName(String name) {
		String upper = name.toUpperCase();
		StringBuilder sb = new StringBuilder();
		sb.append(upper.charAt(0));
		sb.append(name.substring(1));
		return sb.toString();
	}

	private String getConstantName(String name) {
		String upper = name.toUpperCase();
		StringBuilder sb = new StringBuilder();
		for(int i=0 ; i<name.length() ; i++){
			char c = name.charAt(i);
			if(i>0 && c>= 'A' && c <= 'Z'){
				//check if next is upper as well
				if(((i-1)> 0 && name.charAt(i-1) >= 'A' && name.charAt(i-1) <= 'Z') || ((i+1) < name.length() && name.charAt(i+1) >= 'A' && name.charAt(i+1) <= 'Z')){
					
				}else{
					sb.append("_");
				}
			}
			sb.append(upper.charAt(i));
		}
		return sb.toString();
	}
}
