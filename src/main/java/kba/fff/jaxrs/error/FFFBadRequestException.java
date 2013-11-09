package kba.fff.jaxrs.error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import kba.fff.fff_common.ErrorMsg;

public class FFFBadRequestException extends WebApplicationException{
	
//	private static final long	serialVersionUID	= 1L;

	public FFFBadRequestException(ErrorMsg err) {
		super(Response
				.status(Response.Status.BAD_REQUEST)
				.entity(err + "\n")
				.type(MediaType.TEXT_PLAIN)
				.build());
	}
	public FFFBadRequestException(ErrorMsg err, String message) {
		super(Response
				.status(Response.Status.BAD_REQUEST)
				.entity(err + "\n" + message)
				.type(MediaType.TEXT_PLAIN)
				.build());
	}
	public FFFBadRequestException(String message) {
		super(Response
				.status(Response.Status.BAD_REQUEST)
				.entity(ErrorMsg.INVALID_RDF + ": " + message)
				.type(MediaType.TEXT_PLAIN)
				.build());
	}

}
