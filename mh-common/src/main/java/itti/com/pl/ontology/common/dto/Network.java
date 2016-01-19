package itti.com.pl.ontology.common.dto;

import itti.com.pl.ontology.common.dto.MetadataObject;

public class Network extends MetadataObject{

	private static final String HAS_CONTACT_INFORMATION = "hasContactInformation";
	private static final String NETWORK_ADDRESS = "networkAddress";
	private static final String NETWORK_CAPACITY = "networkCapacity";
	private static final String NETWORK_DESCRIPTION = "networkDescription";
	private static final String NETWORK_FUNCTIONAL_TYPE = "networkFunctionalType";
	private static final String NETWORKID = "networkID";
	private static final String NETWORK_NAME = "networkName";
	private static final String NETWORK_RANGE = "networkRange";
	private static final String NETWORK_TOPOLOGY = "networkTopology";
	private static final String NETWORK_TYPE = "networkType";


	public void setHasContactInformation(String hasContactInformation){
		setProperty(HAS_CONTACT_INFORMATION, hasContactInformation);
	}
	public String getHasContactInformation(){
		return getProperty(HAS_CONTACT_INFORMATION);
	}

	public void setNetworkAddress(String networkAddress){
		setProperty(NETWORK_ADDRESS, networkAddress);
	}
	public String getNetworkAddress(){
		return getProperty(NETWORK_ADDRESS);
	}

	public void setNetworkCapacity(String networkCapacity){
		setProperty(NETWORK_CAPACITY, networkCapacity);
	}
	public String getNetworkCapacity(){
		return getProperty(NETWORK_CAPACITY);
	}

	public void setNetworkDescription(String networkDescription){
		setProperty(NETWORK_DESCRIPTION, networkDescription);
	}
	public String getNetworkDescription(){
		return getProperty(NETWORK_DESCRIPTION);
	}

	public void setNetworkFunctionalType(String networkFunctionalType){
		setProperty(NETWORK_FUNCTIONAL_TYPE, networkFunctionalType);
	}
	public String getNetworkFunctionalType(){
		return getProperty(NETWORK_FUNCTIONAL_TYPE);
	}

	public void setNetworkID(String networkID){
		setProperty(NETWORKID, networkID);
	}
	public String getNetworkID(){
		return getProperty(NETWORKID);
	}

	public void setNetworkName(String networkName){
		setProperty(NETWORK_NAME, networkName);
	}
	public String getNetworkName(){
		return getProperty(NETWORK_NAME);
	}

	public void setNetworkRange(String networkRange){
		setProperty(NETWORK_RANGE, networkRange);
	}
	public String getNetworkRange(){
		return getProperty(NETWORK_RANGE);
	}

	public void setNetworkTopology(String networkTopology){
		setProperty(NETWORK_TOPOLOGY, networkTopology);
	}
	public String getNetworkTopology(){
		return getProperty(NETWORK_TOPOLOGY);
	}

	public void setNetworkType(String networkType){
		setProperty(NETWORK_TYPE, networkType);
	}
	public String getNetworkType(){
		return getProperty(NETWORK_TYPE);
	}


}
