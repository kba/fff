package kba.fff.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import kba.fff.fff_common.ErrorMsg;
import kba.fff.fff_common.UriMinter;
import kba.fff.jaxrs.error.FFFBadRequestException;
import kba.fff.ws.Config;
import kba.fff.ws.ConfigProp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import eu.dm2e.grafeo.GResource;
import eu.dm2e.grafeo.Grafeo;
import eu.dm2e.grafeo.jena.GrafeoImpl;

public class FffClient {
	protected Gson gson = new Gson();
	protected Logger log = LoggerFactory.getLogger(getClass().getName());
	protected Client jerseyClient = ClientBuilder.newClient();

	public String createObservation() {
		return jerseyClient.target(Config.get(ConfigProp.OBSERVATION_SERVICE))
                           .path("meta")
                           .request(MediaType.TEXT_PLAIN)
                           .post(null)
                           .getLocation()
                           .toString();
	}
	public void postObservation(Grafeo g, String observationURN) {
		jerseyClient.target(Config.get(ConfigProp.OBSERVATION_SERVICE))
                    .queryParam("urn", observationURN)
                    .request(MediaType.TEXT_PLAIN)
                    .post(g.getNTriplesEntity());
	}

	
	public String mintUriForTopBlankNode(Grafeo g, final String type, final String template) {
		final GResource topBlank = g.findTopBlank(type);
		if (null == topBlank) {
			throw new FFFBadRequestException(ErrorMsg.MISSING_TOP_NODE);
		}
		String annoID = UriMinter.mintURI(template);
		topBlank.rename(annoID);
		return annoID;
	}

}
