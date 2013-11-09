package kba.fff.ws.service;

import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import kba.fff.client.FffClient;
import kba.fff.fff_common.ErrorMsg;
import kba.fff.fff_common.NS;
import kba.fff.jaxrs.error.FFFBadRequestException;
import eu.dm2e.grafeo.GResource;
import eu.dm2e.grafeo.Grafeo;

public abstract class AbstractFffService extends FffClient {
	
	@Context UriInfo uriInfo;
	@Context HttpHeaders httpHeaders;
	
	protected WebTarget targetService(String target) {
		return jerseyClient.target(URI.create(uriInfo.getBaseUri().toString() + target));
	}
	
	protected void assertResponseContainsLocation(Response r) {
		if (null == r.getHeaderString("Location")) {
			throw new BadRequestException("Response didn't contain 'Location' header'");
		}
	}
	protected void assertResponseIsOK(Response r) {
		if (r.getStatus() >= 400) {
			throw new BadRequestException();
		}
	}
	
	public String determineUrnFromInput(String urn, Grafeo g) {
		GResource topBlank = g.findTopBlank(NS.FFF.CLASS_OBSERVATION);
		if (null != topBlank) {
			assertValidURN(urn);
			topBlank.rename(urn);
		}
		if (null == urn) {
			GResource topRes = new ArrayList<GResource>(g.findByClass(NS.FFF.CLASS_OBSERVATION)).get(0);
			if (null == topRes)
				throw new FFFBadRequestException(ErrorMsg.INVALID_RDF);
			urn = topRes.getUri();
		}
		return urn;
	}

	public void assertValidURN(String urn) {
		if (null == urn || ! urn.startsWith("urn:x-fff")) {
			throw new FFFBadRequestException("'urn' =  " + urn);
		}
	}

}
