package de.ahus1.rest.exception;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import de.ahus1.util.Localizer;

/**
 * Handling of exceptions occuring when entities already exist for the given ID.
 * 
 * @author Alexander Schwartz (2012)
 * 
 */
@Provider
public class EntityExistsExceptionMapper implements
    ExceptionMapper<EntityExistsException> {

  @Inject
  private Localizer localizer;

  /**
   * Convert the internal exception to something RESTful.
   * 
   * @param exception
   *          exception to be handled
   * @return a response of type 409 / conflict.
   */
  @Override
  public Response toResponse(EntityExistsException exception) {
    Map<String, String> responseObj = new HashMap<String, String>();

    responseObj.put("message", localizer.localize("error.entityExists"));

    return Response.status(Response.Status.CONFLICT).entity(responseObj)
        .build();
  }
}
