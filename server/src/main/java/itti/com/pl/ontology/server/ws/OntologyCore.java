package itti.com.pl.ontology.server.ws;

import itti.com.pl.ontology.core.ontology.Ontology;

import java.util.Map;

import javax.jws.WebService;

@WebService(endpointInterface = "itti.com.pl.ontology.server.ws.OntologyCoreWS")
public class OntologyCore implements OntologyCoreWS {

	private Ontology ontology = null;

	/**
	 * @param ontology
	 */
	public void setOntology(Ontology ontology) {
		this.ontology = ontology;
	}

	@Override
	public void addSwrlRule(String ruleName, String ruleContent) {
		ontology.addSwrlRule(ruleName, ruleContent);		
	}

	@Override
	public void removeSwrlRule(String ruleName) {
		ontology.removeSwrlRule(ruleName);
	}

	@Override
	public void enableRule(String ruleName) {
		ontology.enableSwrlRule(ruleName);
	}

	@Override
	public void disableRule(String ruleName) {
		ontology.disableSwrlRule(ruleName);
	}

	@Override
	public Map<String, String> getSwrlRules() {
		return ontology.getSwrlRules();
	}

	@Override
	public void refineRules() {
		ontology.runSwrlEngine();
	}


	

}