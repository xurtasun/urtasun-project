package edu.upc.eetac.dsa.urtasun.urtasun.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import edu.upc.eetac.dsa.urtasun.urtasun.api.model.UrtasunError;

@Provider
public class WebApplicationExceptionMapper implements
		ExceptionMapper<WebApplicationException> {
	@Override
	public Response toResponse(WebApplicationException exception) {
		UrtasunError error = new UrtasunError(
				exception.getResponse().getStatus(), exception.getMessage());
		return Response.status(error.getStatus()).entity(error)
				.type(MediaType.URTASUN_API_ERROR).build();
	}

}