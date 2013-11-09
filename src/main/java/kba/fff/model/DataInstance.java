package kba.fff.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kba.fff.fff_common.NS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import eu.dm2e.grafeo.annotations.RDFClass;
import eu.dm2e.grafeo.annotations.RDFProperty;
import eu.dm2e.grafeo.gom.SerializablePojo;
import eu.dm2e.grafeo.jena.SparqlSelect;


@RDFClass(NS.FFF.CLASS_DATA_INSTANCE)
public class DataInstance extends SerializablePojo<DataInstance>{
	
	static Logger log = LoggerFactory.getLogger(DataInstance.class.getName());
	
	@RDFProperty(value = NS.FFF.PROP_DATA_OBJECT, serializeAsURI = true)
	private DataObject dataObject;
	public DataObject getDataObject() { return dataObject; }
	public void setDataObject(DataObject dataObject) { this.dataObject = dataObject; }
	
	@RDFProperty(NS.FFF.PROP_DATA_LOCATION)
	private DataLocation dataLocation;
	public DataLocation getDataLocation() { return dataLocation; }
	public void setDataLocation(DataLocation dataLocation) { this.dataLocation = dataLocation; }

	public static List<String> findIdenticalDataInstances(String endpoint, DataInstance queryObj) {
		ResultSet resultSet = new SparqlSelect.Builder()
			.select("?datainstance")
			.where("GRAPH ?g {" +
					"?datainstance <" + NS.FFF.PROP_DATA_OBJECT + "> <" + queryObj.getDataObject().getId() + "> ." +
					"?datainstance <" + NS.FFF.PROP_DATA_LOCATION + "> ?loc ." +
					"?loc <" + NS.FFF.PROP_URI + "> ?value ." +
					"FILTER (REGEX(STR(?value), \"" + queryObj.getDataLocation().getUri() + "\", \"i\"))" +
					"}")
			.endpoint(endpoint)
			.build()
			.execute();
		Set<String> diSet = new HashSet<>();
		while (resultSet.hasNext()) {
			QuerySolution sol = resultSet.next();
			diSet.add(sol.get("datainstance").toString());
		}
		return new ArrayList<>(diSet);
	}

	public static List<String> findDataInstancesByURI(String endpoint, String uri) {
		ResultSet resultSet = new SparqlSelect.Builder()
			.select("?datainstance")
			.where("GRAPH ?g {" +
					"?datainstance <" + NS.FFF.PROP_DATA_LOCATION + "> ?loc ." +
					"?loc <" + NS.FFF.PROP_URI + "> ?value ." +
					"FILTER (REGEX(STR(?value), \"" + uri + "\", \"i\"))" +
					"}")
			.endpoint(endpoint)
			.build()
			.execute();
		Set<String> diSet = new HashSet<>();
		while (resultSet.hasNext()) {
			QuerySolution sol = resultSet.next();
			diSet.add(sol.get("datainstance").toString());
		}
		return new ArrayList<>(diSet);
	}
	
	public static List<String> findDataInstancesByPath(String endpoint, String path) {
		ResultSet resultSet = new SparqlSelect.Builder()
			.select("?datainstance")
			.where("GRAPH ?g {" +
					"?datainstance <" + NS.FFF.PROP_DATA_LOCATION + "> ?loc ." +
					"?loc <" + NS.FFF.PROP_FILE_PATH + "> ?value ." +
					"FILTER (REGEX(STR(?value), \"" + path + "\", \"i\"))" +
					"}")
			.endpoint(endpoint)
			.build()
			.execute();
		Set<String> diSet = new HashSet<>();
		while (resultSet.hasNext()) {
			QuerySolution sol = resultSet.next();
			diSet.add(sol.get("datainstance").toString());
		}
		return new ArrayList<>(diSet);
	}
	public static List<String> findInstanceByObject(String endpoint, String urn) {
		System.out.println(urn);
		final SparqlSelect query = new SparqlSelect.Builder()
			.select("?datainstance")
			.where("GRAPH ?g {" +
					"?datainstance <" + NS.FFF.PROP_DATA_OBJECT + "> <" + urn + ">" +
					"}")
			.endpoint(endpoint)
			.build();
		ResultSet resultSet = query.execute();
		log.debug(query.toString());
		Set<String> diSet = new HashSet<>();
		while (resultSet.hasNext()) {
			QuerySolution sol = resultSet.next();
			diSet.add(sol.get("datainstance").toString());
		}
		return new ArrayList<>(diSet);
	}
}
