package kba.fff.ws.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import kba.fff.fff_common.NS;
import kba.fff.ws.Config;
import kba.fff.ws.ConfigProp;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import eu.dm2e.grafeo.Grafeo;
import eu.dm2e.grafeo.jena.GrafeoImpl;
import eu.dm2e.grafeo.jena.SparqlSelect;

@Path("onto")
public class OntologyService  extends AbstractFffService {

	private final String rdfType = NS.OA.ANNOTATION;
	private final String uriTemplate = "urn:x-fff:anno:${randomString[10]}";
	
	/**
	 * @param rdfStr
	 * @param observationURN
	 * @return
	 */
	@POST
	public Response postOntology(String rdfStr,
			@QueryParam("urn") String urn,
			@QueryParam("observation") String observationURN,
			@QueryParam("base") String baseURI
			) {

		// parse input
		Grafeo g = new GrafeoImpl(rdfStr, true);

		// ensure URN
		if (null == urn) {
			urn = mintUriForTopBlankNode(g, rdfType, uriTemplate);
		}
		// TODO validate

		// ensure observation
		if (null == observationURN) {
			observationURN = this.createObservation();
		}
		// publish observation
		this.postObservation(g, observationURN);
		
//		g.addTriple(urn, baseURI, null)
        
		return Response.ok(g)
				.header("X-FFF-Observation", observationURN)
				.header("Location", urn)
				.build();
	}

	@GET
	@Path("findDataObjectsByTag")
	public Response findDataObjectsByTag(@QueryParam("tag") String tag, @QueryParam("exactMatch") boolean exactMatch) {
		
		String endpoint = Config.get(ConfigProp.ENDPOINT_QUERY);
		final SparqlSelect query = new SparqlSelect.Builder()
			.select("?target")
			.where("GRAPH ?g {\n" +
					"?anno <" + NS.OA.HAS_BODY + "> ?body .\n" +
					"?anno <" + NS.OA.HAS_TARGET + "> ?target .\n" +
					"?body <" + NS.CNT.CHARS + "> ?value .\n" + (
					exactMatch ? "FILTER (REGEX(STR(?value), \"^" + tag + "$\", \"i\"))\n" 
							    : "FILTER (REGEX(STR(?value), \"" + tag + "\", \"i\"))\n"
					) +
					"}")
			.endpoint(endpoint)
			.build();
		log.debug(query.toString());
		ResultSet resultSet = query.execute();
		Set<String> set = new HashSet<>();
		while (resultSet.hasNext()) {
			QuerySolution sol = resultSet.next();
			set.add(sol.get("target").toString());
		}
		ArrayList<String> list = new ArrayList<>(set);
		return Response.ok().entity(gson.toJson(list)).build();
	}

}
