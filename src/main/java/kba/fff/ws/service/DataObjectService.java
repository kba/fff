package kba.fff.ws.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import kba.fff.fff_common.ErrorMsg;
import kba.fff.fff_common.NS;
import kba.fff.fff_common.UriMinter;
import kba.fff.jaxrs.error.FFFBadRequestException;
import kba.fff.model.DataObject;
import kba.fff.ws.Config;
import kba.fff.ws.ConfigProp;
import eu.dm2e.grafeo.Grafeo;
import eu.dm2e.grafeo.jena.GResourceImpl;
import eu.dm2e.grafeo.jena.GrafeoImpl;

@Path("dataobject")
public class DataObjectService extends AbstractFffService{
	
	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
	public Response postDataObject(String rdfStr, @QueryParam("observation") String observationURN) {
		Grafeo g  = new GrafeoImpl();
		g.readHeuristically(rdfStr);
		log.debug(g.getTerseTurtle());
		final GResourceImpl topBlank = g.findTopBlank(NS.FFF.CLASS_DATA_OBJECT);
		if (null == topBlank) {
			throw new FFFBadRequestException(ErrorMsg.MISSING_TOP_NODE);
		}
		DataObject queryObj = g.getObjectMapper().getObject(DataObject.class, topBlank);
		List<String> identicalDObjs = DataObject.findIdenticalDataObjects(Config.get(ConfigProp.ENDPOINT_QUERY), queryObj);
		String dataObjectId = identicalDObjs.isEmpty() 
				? UriMinter.mintURI("urn:x-fff:dataobject:${randomString[10]}")
                : identicalDObjs.get(0);
		int i = 9;
		if (identicalDObjs.isEmpty()) {
			topBlank.rename(dataObjectId);
//			for (GStatement chkStmt : g.listStatements(topBlank, NS.FFF.PROP_CHECKSUM, null)) {
//				GResource chk = chkStmt.getObject().resource();
//				String newId = UriMinter.mintURI(dataObjectId + ":checksum:${timestamp}:" + chk.get(NS.FFF.PROP_CHECKSUM_VALUE));
//				chk.rename(newId);
//			}
//			for (GStatement byteRegionStmt : g.listStatements(null, NS.FFF.PROP_BYTE_REGION, null)) {
//				GResource byteregion = byteRegionStmt.getObject().resource();
//				String newId = UriMinter.mintURI("urn:x-fff:byte-region:${uuid}");
//				byteregion.rename(newId);
//			}

			// TODO lookup if we find it
			//		log.debug("fnork");
			//		log.debug(Config.get(ConfigProp.ENDPOINT_UPDATE));
			observationURN =  (null == observationURN) 
					? jerseyClient.target(Config.get(ConfigProp.OBSERVATION_SERVICE))
							.path("meta")
							.request(MediaType.TEXT_PLAIN)
							.post(null)
							.getLocation()
							.toString()
                    : observationURN;

             Response obsResp = jerseyClient.target(Config.get(ConfigProp.OBSERVATION_SERVICE))
				  				      .queryParam("urn", observationURN)
									  .request(MediaType.TEXT_PLAIN)
									  .post(g.getNTriplesEntity());
             if (obsResp.getStatus() >= 300) {
                     throw new FFFBadRequestException("Observation service returned " + obsResp.readEntity(String.class));
             }

		}
		log.debug(g.getTerseTurtle());
        
//		g.postToEndpoint(Config.get(ConfigProp.ENDPOINT_UPDATE), graph);
//		return Response.accepted().entity(g.getNTriples()).build();
		return Response.ok(g)
				.header("X-FFF-Observation", observationURN)
				.header("Location", dataObjectId)
				.build();
	}
	
	@GET
	@Path("findByChecksum/{checksum}")
	public Response findDataObjectsByChecksum(@PathParam("checksum") String checksum) {
		final String endpoint = Config.get(ConfigProp.ENDPOINT_QUERY);
		List<String> doList = DataObject.findByChecksum(endpoint, checksum, null);
		return Response.ok(gson.toJson(doList)).build();
	}


}
