package kba.fff.ws;

import java.util.HashMap;
import java.util.Map;

public enum Config {
	
	INSTANCE;
	
	private Map<ConfigProp,String> configMap = new HashMap<>();
	
	public static String get(ConfigProp prop) { return INSTANCE.configMap.get(prop); }
	public static void set(ConfigProp prop, String val) { INSTANCE.configMap.put(prop, val); }
	public static void put(ConfigProp prop, String val) { INSTANCE.configMap.put(prop, val); }
	
	static {
		set(ConfigProp.ENDPOINT_QUERY, "http://localhost:9999/openrdf-sesame/repositories/fff");
		set(ConfigProp.ENDPOINT_UPDATE, "http://localhost:9999/openrdf-sesame/repositories/fff/statements");
		set(ConfigProp.OBSERVATION_SERVICE, "http://localhost:8080/rest/observation");
	}
	
}
