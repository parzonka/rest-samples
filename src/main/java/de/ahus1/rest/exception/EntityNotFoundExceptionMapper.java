package de.ahus1.rest.exception;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import de.ahus1.util.Localizer;

/**
 * Handling of exceptions occuring when entities are not found.
 * 
 * @author Alexander Schwartz (2012)
 * 
 */
@Provider
public class EntityNotFoundExceptionMapper implements
    ExceptionMapper<EntityNotFoundException> {

  @Inject
  private Localizer localizer;

  /**
   * Convert the internal exception to something RESTful.
   * 
   * @param exception
   *          exception to be handled
   * @return a response of type 404 / not found.
   */
  @Override
  public Response toResponse(EntityNotFoundException exception) {
    Map<String, String> responseObj = new HashMap<String, String>();

    responseObj.put("general", localizer.localize("error.entityNotFound"));

    return Response.status(Response.Status.NOT_FOUND).entity(responseObj)
        .build();
  }
}
