package kba.fff.ws.service;

import java.util.ArrayList;
import java.util.List;

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
import kba.fff.model.DataInstance;
import kba.fff.ws.Config;
import kba.fff.ws.ConfigProp;
import eu.dm2e.grafeo.GResource;
import eu.dm2e.grafeo.GStatement;
import eu.dm2e.grafeo.jena.GResourceImpl;
import eu.dm2e.grafeo.jena.GrafeoImpl;

@Path("datainstance")
public class DataInstanceService  extends AbstractFffService {
	
	@POST
	public Response postDataInstance(String rdfStr, @QueryParam("observation") String observationURN) {
		GrafeoImpl g = new GrafeoImpl();
		g.readHeuristically(rdfStr);
		final GResourceImpl topBlank = g.findTopBlank(NS.FFF.CLASS_DATA_INSTANCE);
		if (null == topBlank) {
			throw new FFFBadRequestException(ErrorMsg.MISSING_TOP_NODE);
		}
		DataInstance queryObj = g.getObjectMapper().getObject(DataInstance.class, topBlank);
		// validate
		if (null == queryObj.getDataObject()) {
			throw new FFFBadRequestException("DataInstance needs fff:dataObject");
		}
		// align
		List<String> identicalDiList = DataInstance.findIdenticalDataInstances(Config.get(ConfigProp.ENDPOINT_QUERY), queryObj);
//		List<String> identicalDiList = new ArrayList<>();
		
		String dataInstanceId = identicalDiList.isEmpty() 
				? UriMinter.mintURI("urn:x-fff:datainstance:${randomString[10]}")
                : identicalDiList.get(0);
		if (identicalDiList.isEmpty()) {
			topBlank.rename(dataInstanceId);
			for (GStatement stmt : g.listStatements(topBlank, NS.FFF.PROP_DATA_LOCATION, null)) {
				GResource loc = stmt.getObject().resource();
				loc.rename(dataInstanceId + ":location:" + queryObj.getDataLocation().getUri());
			}

			// publish
			if (null == observationURN)  {
				Response r = jerseyClient.target(Config.get(ConfigProp.OBSERVATION_SERVICE))
                       .path("meta")
                       .request(MediaType.TEXT_PLAIN)
                       .post(null);
				observationURN = r.getLocation().toString();
			}
                
			Response obsResp = jerseyClient.target(Config.get(ConfigProp.OBSERVATION_SERVICE))
                                     .queryParam("urn", observationURN)
                                     .request(MediaType.TEXT_PLAIN)
                                     .post(g.getNTriplesEntity());
		log.error("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYYYYYYYYYYYYYY");
		log.error("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYYYYYYYYYYYYYY");
		log.error("YY~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYYYYYYYYYYYY");
		log.error("YYYYY~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYYYYYYYYY");
		log.error("YYYYYYYYYYYYYYYYYY");
		log.error("YYYYYYYYYYYYYYYYYY");
		log.error("YYYYYYYYYYYYYYYYYY");
		log.error("Y~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYYYYYYYYYYYYY");
		log.error("YYYYYYYYYYYYYYYYYY");
		log.error("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYYYYYYYYYYYYYY");
		log.error("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYYYYYYYYYYYYYY");
		log.error("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYYYYYYYYYYYYYY");
		log.error("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYYYYYYYYYYYYYY");
		log.error("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYYYYYYYYYYYYYY");
		log.error("YYYYYYYYYYYYYYYYYY");
		log.error("YYY~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYYYYYYYYYYY");
		log.error("YYYYYYYYYYYYYY~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYY");
		log.error("YYYYYYYYYYYY~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYY");
		log.error("Y~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYYYYYYYYYYYYY");
		log.error("YY~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~YYYYYYYYYYYYYYYY");
		log.error("YYYYYYYYYYYYYYYYYY");
			log.debug(obsResp.toString());
		}

		return Response.ok(g)
				.header("X-FFF-Observation", observationURN)
				.header("Location", dataInstanceId)
				.build();
	}
	
	@GET
	@Path("findByPath")
	public Response findDataInstanceByFilePath(@QueryParam("path") String path) {
		
		List<String> list = DataInstance.findDataInstancesByPath(Config.get(ConfigProp.ENDPOINT_QUERY), path);
		return Response.ok().entity(gson.toJson(list)).build();
	}
	
	@GET
	@Path("findByDataObject")
	public Response findInstanceByObject(@QueryParam("urn") String urn) {
		List<String> list = DataInstance.findInstanceByObject(Config.get(ConfigProp.ENDPOINT_QUERY), urn);
		return Response.ok().entity(gson.toJson(list)).build();
		
	}

}
