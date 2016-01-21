package itti.com.pl.ontology.common.dto;

import itti.com.pl.ontology.common.dto.MetadataObject;

public class Device extends MetadataObject{

	private static final String DEVICEID = "deviceID";
	private static final String DEVICE_NAME = "deviceName";
	private static final String HARDWARE = "hardware";
	private static final String HAS_DEVICE_PROPERTIES = "hasDeviceProperties";
	private static final String HAS_DISPLAY_PROPERTIES = "hasDisplayProperties";
	private static final String HAS_ENCODING_PROPERTIES = "hasEncodingProperties";
	private static final String HASHWPROPERTIES = "hasHWProperties";
	private static final String HASMIMETYPES_UN_SUPPORTED = "hasMIMETypesUnSupported";
	private static final String HAS_SERVICE = "hasService";
	private static final String SOFTWARE = "software";
	private static final String STATUS = "status";
	private static final String USED_BY = "usedBy";


	public void setDeviceID(String deviceID){
		setProperty(DEVICEID, deviceID);
	}
	public String getDeviceID(){
		return getProperty(DEVICEID);
	}

	public void setDeviceName(String deviceName){
		setProperty(DEVICE_NAME, deviceName);
	}
	public String getDeviceName(){
		return getProperty(DEVICE_NAME);
	}

	public void setHardware(String hardware){
		setProperty(HARDWARE, hardware);
	}
	public String getHardware(){
		return getProperty(HARDWARE);
	}

	public void setHasDeviceProperties(String hasDeviceProperties){
		setProperty(HAS_DEVICE_PROPERTIES, hasDeviceProperties);
	}
	public String getHasDeviceProperties(){
		return getProperty(HAS_DEVICE_PROPERTIES);
	}

	public void setHasDisplayProperties(String hasDisplayProperties){
		setProperty(HAS_DISPLAY_PROPERTIES, hasDisplayProperties);
	}
	public String getHasDisplayProperties(){
		return getProperty(HAS_DISPLAY_PROPERTIES);
	}

	public void setHasEncodingProperties(String hasEncodingProperties){
		setProperty(HAS_ENCODING_PROPERTIES, hasEncodingProperties);
	}
	public String getHasEncodingProperties(){
		return getProperty(HAS_ENCODING_PROPERTIES);
	}

	public void setHasHWProperties(String hasHWProperties){
		setProperty(HASHWPROPERTIES, hasHWProperties);
	}
	public String getHasHWProperties(){
		return getProperty(HASHWPROPERTIES);
	}

	public void setHasMIMETypesUnSupported(String hasMIMETypesUnSupported){
		setProperty(HASMIMETYPES_UN_SUPPORTED, hasMIMETypesUnSupported);
	}
	public String getHasMIMETypesUnSupported(){
		return getProperty(HASMIMETYPES_UN_SUPPORTED);
	}

	public void setHasService(String hasService){
		setProperty(HAS_SERVICE, hasService);
	}
	public String getHasService(){
		return getProperty(HAS_SERVICE);
	}

	public void setSoftware(String software){
		setProperty(SOFTWARE, software);
	}
	public String getSoftware(){
		return getProperty(SOFTWARE);
	}

	public void setStatus(String status){
		setProperty(STATUS, status);
	}
	public String getStatus(){
		return getProperty(STATUS);
	}

	public void setUsedBy(String usedBy){
		setProperty(USED_BY, usedBy);
	}
	public String getUsedBy(){
		return getProperty(USED_BY);
	}


}
