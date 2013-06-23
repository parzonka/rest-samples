package de.ahus1.rest.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Handling of constraint violation exceptions that can be thrown by bean
 * validation. This will catch all exceptions; we try to be most specific here!
 * 
 * @author Alexander Schwartz (2012)
 * 
 */
// @Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {
  @Inject
  private Logger log;

  /**
   * Convert the internal exception to something RESTful.
   * 
   * @param exception
   *          exception to be handled
   * @return a response of type 400 / bad request.
   */
  @Override
  public Response toResponse(Exception exception) {
    Map<String, String> responseObj = new HashMap<String, String>();
    // TODO: I18N
    responseObj.put("message", exception.getLocalizedMessage());

    log.log(Level.SEVERE, "can't analyze", exception);

    return Response.status(Response.Status.BAD_REQUEST).entity(responseObj)
        .build();
  }
}
