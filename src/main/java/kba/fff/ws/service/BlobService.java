package kba.fff.ws.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import kba.fff.client.plugin.ChecksumPlugin;
import kba.fff.fff_common.NS;
import kba.fff.fff_common.UriMinter;
import kba.fff.model.DataInstance;
import kba.fff.ws.Config;
import kba.fff.ws.ConfigProp;

import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import eu.dm2e.grafeo.GResource;
import eu.dm2e.grafeo.jena.GrafeoImpl;

@Path("blob")
public class BlobService  extends AbstractFffService {
	GridFS gridfs;
	
	public BlobService() {
		try {
			MongoClient mongo = new MongoClient("localhost");
			DB db = mongo.getDB("fff");
			this.gridfs = new GridFS(db);
		} catch (UnknownHostException e) {
			log.debug(""+e);
		}
	}
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({"text/turtle"})
	public Response postFile(
			@FormDataParam("blob") InputStream is,
			@FormDataParam("blob") FormDataContentDisposition blobHeader
			) {
		
//		if (httpHeaders.getHeaderString("Expect") != null && httpHeaders.getHeaderString("Expect").equals("100-continue")) {
//			return Response.status(100).build();
//		}
		ChecksumPlugin checksumPlugin = new ChecksumPlugin();
		
		GrafeoImpl g = new GrafeoImpl();
		GResource dataObjectRes = g.createBlank();
		GResource dataInstanceRes = g.createBlank();
		GResource dataLocationRes = g.createBlank();
		g.addTriple(dataObjectRes, NS.A, NS.FFF.CLASS_DATA_OBJECT);
		g.addTriple(dataInstanceRes, NS.A, NS.FFF.CLASS_DATA_INSTANCE);
		g.addTriple(dataLocationRes, NS.A, NS.FFF.CLASS_DATA_LOCATION);
		g.addTriple(dataLocationRes, NS.A, NS.FFF.CLASS_HTTP_LOCATION);
		
		String tempURN = UriMinter.mintURI("urn:x-fff:temp:${randomString[20]}");

		GridFSInputFile gridfsInFile = this.gridfs.createFile(is);
		gridfsInFile.setFilename(tempURN);
		gridfsInFile.save();
		log.debug("LENGTH IS" + gridfsInFile.getLength());
		
		GridFSDBFile gridfsFile = this.gridfs.findOne(tempURN);
		try {
			checksumPlugin.analyzeInputStream(g, dataObjectRes, gridfsFile.getInputStream());
		} catch (IOException e1) {
			throw new BadRequestException(e1);
		}

		Response rDO = this.targetService("dataobject")
                         .request("text/turtle")
                         .post(g.getNTriplesEntity());
		
		assertResponseIsOK(rDO);
		assertResponseContainsLocation(rDO);
		
		String urnDO = rDO.getHeaderString("Location");
		gridfsFile.put("filename", urnDO);
		gridfsFile.save();
		dataObjectRes.rename(urnDO);

		g.addTriple(dataInstanceRes, NS.FFF.PROP_DATA_OBJECT, dataObjectRes);
		g.addTriple(dataLocationRes, NS.FFF.PROP_URI, urnDO);
		g.addTriple(dataInstanceRes, NS.FFF.PROP_DATA_LOCATION, dataLocationRes);
		g.addTriple(dataInstanceRes, NS.DC.EXTENT, g.literal(gridfsFile.getLength()));

		Response rDI = this.targetService("datainstance")
                         .request("text/turtle")
                         .post(g.getNTriplesEntity());
		
		assertResponseIsOK(rDI);
		assertResponseContainsLocation(rDI);
		String urnDI = rDI.getHeaderString("Location");
		dataInstanceRes.rename(urnDI);

		log.debug(g.getTerseTurtle());

		return Response
				.status(200)
				.entity(g)
				.header("Location", this.targetService("blob?urn="+urnDO))
				.build();
	}
	
	@GET
	public Response getFile(@QueryParam("urn") String urn) {
		final GridFSDBFile gridfsFile = this.gridfs.findOne(urn);
		if (null == gridfsFile) {
			throw new NotFoundException();
		}
		ContentDisposition disp = ContentDisposition
				.type("attachment")
				.fileName(gridfsFile.getFilename())
				.build();
		return Response
				.ok(gridfsFile.getInputStream())
				.header("Content-Disposition",disp).build();
	}
	
	@GET
	@Path("findByDataObject")
	public Response findInstanceByObject(@QueryParam("urn") String urn) {
		List<String> list = DataInstance.findInstanceByObject(Config.get(ConfigProp.ENDPOINT_QUERY), urn);
		return Response.ok().entity(gson.toJson(list)).build();
		
	}

}
