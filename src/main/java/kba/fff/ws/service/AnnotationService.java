package kba.fff.ws.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import kba.fff.fff_common.ErrorMsg;
import kba.fff.fff_common.NS;
import kba.fff.fff_common.UriMinter;
import kba.fff.jaxrs.error.FFFBadRequestException;
import kba.fff.ws.Config;
import kba.fff.ws.ConfigProp;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import eu.dm2e.grafeo.jena.GResourceImpl;
import eu.dm2e.grafeo.jena.GrafeoImpl;
import eu.dm2e.grafeo.jena.SparqlSelect;

@Path("annotation")
public class AnnotationService  extends AbstractFffService {
	
	/**
	 * @param rdfStr
	 * @param observationURN
	 * @return
	 */
	@POST
	public Response postAnnotation(String rdfStr, @QueryParam("observation") String observationURN) {
		GrafeoImpl g = new GrafeoImpl();
		g.readHeuristically(rdfStr);
		final GResourceImpl topBlank = g.findTopBlank(NS.OA.ANNOTATION);
		if (null == topBlank) {
			throw new FFFBadRequestException(ErrorMsg.MISSING_TOP_NODE);
		}
		// TODO validate
		String annoID = UriMinter.mintURI("urn:x-fff:anno:${randomString[10]}");
		topBlank.rename(annoID);
		// publish
		observationURN = (null == observationURN) 
				? jerseyClient.target(Config.get(ConfigProp.OBSERVATION_SERVICE))
						.path("meta")
						.request(MediaType.TEXT_PLAIN)
						.post(null)
						.getLocation()
						.toString()
                : observationURN;
            
		jerseyClient.target(Config.get(ConfigProp.OBSERVATION_SERVICE))
                                 .queryParam("urn", observationURN)
                                 .request(MediaType.TEXT_PLAIN)
                                 .post(g.getNTriplesEntity());
        
		return Response.ok(g)
				.header("X-FFF-Observation", observationURN)
				.header("Location", annoID)
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
