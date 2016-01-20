package itti.com.pl.ontology.server.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import itti.com.pl.ontology.common.bean.InstanceProperty;

public class CriteriaUtils {

	private static final String KEY_AND = "&&";
	private static final String KEY_EQUAL = "=";

	public static List<InstanceProperty<?>> parseCriteria(String query) {

		List<InstanceProperty<?>> criteria = new ArrayList<>();

		if(StringUtils.isNotEmpty(query)){
			for (String criterium : query.split(KEY_AND)){
				criteria.add(parseCriterium(criterium));
			}
		}
		return criteria;
	}

	private static InstanceProperty<?> parseCriterium(String criterium) {
		String[] criteriumSplit = criterium.split(KEY_EQUAL);
		if(criteriumSplit.length != 2){
			throw new CriteriaException(String.format("Invalid format of the criterium: %s", criterium));
		}
		String key = criteriumSplit[0];
		String value = criteriumSplit[1];
		return new InstanceProperty<String>(key, value);
	}

}
