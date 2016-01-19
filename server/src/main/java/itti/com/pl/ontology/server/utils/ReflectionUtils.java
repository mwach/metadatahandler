package itti.com.pl.ontology.server.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import itti.com.pl.ontology.common.bean.Instance;
import itti.com.pl.ontology.common.bean.InstanceProperty;
import itti.com.pl.ontology.common.dto.MetadataObject;
import itti.com.pl.ontology.core.exception.ErrorMessages;
import itti.com.pl.ontology.server.exeption.MetadataHandlerException;

public final class ReflectionUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

	private static final Collection<String> excludedMethods = Arrays.asList("getClass", "getName", "getType");

	private ReflectionUtils(){}

	public static void populateInstance(Instance outputInstance, Object dataObject) {

		for (Method method : dataObject.getClass().getMethods()) {
			if(method.getName().startsWith("get") && !excludedMethods.contains(method.getName())){
				try {
					Object value = method.invoke(dataObject, (Object[])null);
					if(value != null){
						StringBuilder name = new StringBuilder(method.getName());
						name.delete(0, 3);
						name.setCharAt(0, name.toString().toLowerCase().charAt(0));
						outputInstance.addProperty(new InstanceProperty<String>(name.toString(), (String)value));
					}
				} catch (Exception e) {
					LOGGER.warn(String.format("Reflection issue occured for method %s", method.getName()), e);
					throw new MetadataHandlerException(
							ErrorMessages.REFLECTION_ISSUE_METHOD
									.getMessage(method.getName()));
				}
			}
		}
	}

	public static void populateInstanceFromMap(Instance outputInstance, Map<String, String> properties) {

		for (Entry<String, String> entry : properties.entrySet()) {
			outputInstance.addProperty(new InstanceProperty<String>(entry.getKey(), entry.getValue()));
		}
	}

	public static void populateObject(Object dataObject, Instance inputInstance) {
		for (InstanceProperty<?> property: inputInstance.getProperties()) {
			StringBuilder propertyName = new StringBuilder(property.getName());
			propertyName.setCharAt(0, property.getName().substring(0, 1).toUpperCase().charAt(0));
			propertyName.insert(0, "set");
			try {
				for (Method method : dataObject.getClass().getMethods()){
					if(propertyName.toString().equals(method.getName())){
						if(property.getValues().get(0) instanceof OWLIndividual){
							method.invoke(dataObject, ((OWLIndividual)property.getValues().get(0)).getLocalName());
						}else{
							method.invoke(dataObject, property.getValues().get(0));
						}
						break;
					}
				}
			}catch(Exception exc){
				LOGGER.warn(String.format("Reflection issue occured for method %s", propertyName.toString()), exc);
				throw new MetadataHandlerException(
						ErrorMessages.REFLECTION_ISSUE_METHOD
								.getMessage(propertyName.toString()));
			}
		}

	}

	public static MetadataObject createInstance(String name) {
		String classPath = "itti.com.pl.ontology.common.dto." + name;
		Object instance = null;
		try {
			instance = Class.forName(classPath).newInstance();
		} catch (Exception exc) {
			LOGGER.warn(String.format("Reflection issue occured for instance %s", name), exc);
			throw new MetadataHandlerException(
					ErrorMessages.REFLECTION_ISSUE_CLASS
							.getMessage(name));
		}
		return (MetadataObject) instance;
	}
}
