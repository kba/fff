package kba.fff.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kba.fff.fff_common.NS;

import org.joda.time.DateTime;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import eu.dm2e.grafeo.annotations.RDFClass;
import eu.dm2e.grafeo.annotations.RDFProperty;
import eu.dm2e.grafeo.gom.SerializablePojo;
import eu.dm2e.grafeo.jena.GrafeoImpl;
import eu.dm2e.grafeo.jena.SparqlSelect;


@RDFClass(NS.FFF.CLASS_OBSERVATION)
public class Observation extends SerializablePojo<Observation>{
	
	@RDFProperty(NS.DC.CREATED)
	private DateTime created;
	public DateTime getCreated() { return created; }
	public void setCreated(DateTime created) { this.created = created; }

	public static List<String> findObservationsMentioningURN(String endpoint, String urn) {
		ResultSet resultSet = new SparqlSelect.Builder()
			.select("?g1 ?g2")
			.where("OPTIONAL {\n" 
			     + "GRAPH ?g1 { <" + urn + "> ?p1 ?o1 . }\n" 
			     + "}\n"
				 + "OPTIONAL {\n"
				 + " GRAPH ?g2 { ?s2 ?p2 <" + urn + "> .}\n"
				 + " }\n")
			.endpoint(endpoint)
			.build()
			.execute();
		Set<String> graphSet = new HashSet<>();
		while (resultSet.hasNext()) {
			QuerySolution sol = resultSet.next();
			if (null != sol.get("g1")) {
				graphSet.add(sol.get("g1").toString());
			}
			if (null != sol.get("g2")) {
				graphSet.add(sol.get("g2").toString());
			}
		}
		return new ArrayList<>(graphSet);
	}
	public static List<String> findObservationsContainingStatements(String endpoint, GrafeoImpl g) {
		ResultSet resultSet = new SparqlSelect.Builder()
			.select("?observation")
			.where("GRAPH ?observation {\n"
				 + g.getNTriples()
				 + " }\n")
			.endpoint(endpoint)
			.build()
			.execute();
		Set<String> graphSet = new HashSet<>();
		while (resultSet.hasNext()) {
			QuerySolution sol = resultSet.next();
			graphSet.add(sol.get("observation").toString());
		}
		return new ArrayList<>(graphSet);
	}
	public static List<String> findAllObservations(String endpoint, String urn) {
		ResultSet resultSet = new SparqlSelect.Builder()
			.select("DISTINCT ?observation")
			.where("GRAPH ?meta {\n"
				 + "?observation <"+NS.RDF.TYPE+"> <"+NS.FFF.CLASS_OBSERVATION+"> ."
				 + " }\n")
			.endpoint(endpoint)
			.build()
			.execute();
		Set<String> graphSet = new HashSet<>();
		while (resultSet.hasNext()) {
			QuerySolution sol = resultSet.next();
			graphSet.add(sol.get("observation").toString());
		}
		return new ArrayList<>(graphSet);
	}

}
