package itti.com.pl.ontology.server.ws;

import java.util.Map;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface OntologyCoreWS {

	public void addSwrlRule(@WebParam(name = "ruleName") String ruleName, @WebParam(name = "ruleContent") String ruleContent);

	public void removeSwrlRule(@WebParam(name = "ruleName") String ruleName);

	public void enableRule(@WebParam(name = "ruleName") String ruleName);

	public void disableRule(@WebParam(name = "ruleName") String ruleName);

	public Map<String, String> getSwrlRules();

	public void refineRules();

}