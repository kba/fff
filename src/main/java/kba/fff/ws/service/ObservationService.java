package kba.fff.ws.service;

import java.net.URI;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import kba.fff.fff_common.UriMinter;
import kba.fff.jaxrs.PATCH;
import kba.fff.model.Observation;
import kba.fff.ws.Config;
import kba.fff.ws.ConfigProp;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dm2e.grafeo.Grafeo;
import eu.dm2e.grafeo.jena.GrafeoImpl;

@Path("observation")
public class ObservationService extends AbstractFffService {
	
	Logger log = LoggerFactory.getLogger(getClass().getName());
	
	/**
	 * Get the data within a single observation
	 * @param urn
	 * @return
	 */
	@GET
	public Response getObservation(@QueryParam("urn") String urn) {
		
		if (null != urn) {
			assertValidURN(urn);
			Grafeo g = new GrafeoImpl();
			g.readFromEndpoint(Config.get(ConfigProp.ENDPOINT_QUERY), urn);
			if (g.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			return Response.ok(g).build();
		} else {
			List<String> graphList = Observation.findAllObservations(Config.get(ConfigProp.ENDPOINT_QUERY), urn);
			if (graphList.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			return Response.ok(gson.toJson(graphList)).build();
		}
	}

	/**
	 * Post data into a observation.
	 * @param urn
	 * @param rdfString
	 * @return
	 */
	@POST
	public Response postObservation(@QueryParam("urn") String urn, String rdfString) {
		
		assertValidURN(urn);
		Grafeo g = new GrafeoImpl();
		g.readHeuristically(rdfString);
		g.postToEndpoint(Config.get(ConfigProp.ENDPOINT_UPDATE), urn);
		
		return Response.status(201).location(URI.create(urn)).entity(g).build();
	}
	
	/**
	 * Put data into an observation.
	 * @param urn
	 * @param rdfString
	 * @return
	 */
	@PUT
	public Response putObservation(@QueryParam("urn") String urn, String rdfString) {
		
		assertValidURN(urn);
		Grafeo g = new GrafeoImpl();
		g.readHeuristically(rdfString);
		g.putToEndpoint(Config.get(ConfigProp.ENDPOINT_UPDATE), urn);
		
		return Response.status(201).location(URI.create(urn)).entity(g).build();
	}
	
	/**
	 * Create a new Observation
	 * @return 202 on success
	 */
	@POST
	@Path("meta")
	public Response createNewObservation() {
		Observation ob = new Observation();
		ob.setCreated(DateTime.now());
		final String obId = UriMinter.mintURI("urn:x-fff:observation:${timestamp}/${randomString[4]}");
		ob.setId(obId);
		ob.getGrafeo().putToEndpoint(Config.get(ConfigProp.ENDPOINT_UPDATE), obId + "/meta");
		return Response.status(202).location(URI.create(obId)).entity(ob.getGrafeo()).build();
	}
	
	/**
	 * Retrieve an Observation
	 * @param urn
	 * @return
	 */
	@GET
	@Path("meta")
	public Response getObservationMeta(@QueryParam("urn") String urn) {
		assertValidURN(urn);
		Grafeo g = new GrafeoImpl();
		g.readFromEndpoint(Config.get(ConfigProp.ENDPOINT_QUERY), urn + "/meta");
		if (g.isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(g).build();
	}

	/**
	 * Replace an Observation
	 * @param urn
	 * @param rdfString
	 * @return
	 */
	@PUT
	@Path("meta")
	public Response putObservationMeta(@QueryParam("urn") String urn, String rdfString) {
		Grafeo g = new GrafeoImpl();
		g.readHeuristically(rdfString);
		urn = determineUrnFromInput(urn, g);
		String urn_meta = urn + "/meta";
		g.putToEndpoint(Config.get(ConfigProp.ENDPOINT_UPDATE), urn_meta);
		return Response.status(201).location(URI.create(urn_meta)).entity(g).build();
	}
	
	/**
	 * Add to an observation
	 * @param urn
	 * @param rdfString
	 * @return
	 */
	@PATCH
	@Path("meta")
	public Response patchObservationMeta(@QueryParam("urn") String urn, String rdfString) {
		Grafeo g = new GrafeoImpl();
		g.readHeuristically(rdfString);
		urn = determineUrnFromInput(urn, g);
		String urn_meta = urn + "/meta";
		g.postToEndpoint(Config.get(ConfigProp.ENDPOINT_UPDATE), urn_meta);
		g.readFromEndpoint(Config.get(ConfigProp.ENDPOINT_QUERY), urn_meta);
		return Response.status(201).location(URI.create(urn_meta)).entity(g).build();
	}

	/**
	 * GET /about
	 * List all observations that contain a certain URN
	 * @param urn
	 * @return
	 */
	@GET 
	@Path("about")
	public Response getObservationsAbout(@QueryParam("urn") String urn) {
		assertValidURN(urn);

		List<String> graphList = Observation.findObservationsMentioningURN(Config.get(ConfigProp.ENDPOINT_QUERY), urn);
		if (graphList.isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(gson.toJson(graphList)).build();
	}

	/**
	 * GET /data-about
	 * Create a unified graph of all observations about a URN
	 * @param urn
	 * @return
	 */
	@GET 
	@Path("data-about")
	public Response getDataAbout(@QueryParam("urn") String urn) {
		assertValidURN(urn);
		GrafeoImpl g = new GrafeoImpl();
		List<String> graphList = Observation.findObservationsMentioningURN(Config.get(ConfigProp.ENDPOINT_QUERY), urn);
		for (String graphName : graphList) {
			g.readFromEndpoint(Config.get(ConfigProp.ENDPOINT_QUERY), graphName);
		}
		int status = graphList.isEmpty() ? 402 : 200;
		return Response.status(status).entity(g).build();
	}

	/**
	 * POST /stating
	 * @param rdfString
	 * @return
	 */
	@POST 
	@Path("stating")
	public Response getObservationsContaintStatements(String rdfString) {
		GrafeoImpl g = new GrafeoImpl();
		g.readHeuristically(rdfString);
		List<String> graphList = Observation.findObservationsContainingStatements(Config.get(ConfigProp.ENDPOINT_QUERY), g);
		return Response.ok(gson.toJson(graphList)).build();
	}

}