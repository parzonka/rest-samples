package de.ahus1.rest.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.hibernate.exception.ConstraintViolationException;

import de.ahus1.util.Localizer;

/**
 * Handling of various persistence exceptions.
 * 
 * @author Alexander Schwartz (2012)
 * 
 */
@Provider
public class PersistenceExceptionHandler implements
    ExceptionMapper<PersistenceException> {

  @Inject
  private Logger log;

  @Inject
  private Localizer localizer;

  /**
   * Convert the internal exception to something RESTful.
   * 
   * @param exception
   *          exception to be handled
   * @return a response of type 400 / bad request, but tries to be more specific
   *         for some root causes.
   */
  @Override
  public Response toResponse(PersistenceException exception) {
    Map<String, String> responseObj = new HashMap<String, String>();

    String message = "error.unknown";

    Response.Status status = Response.Status.BAD_REQUEST;

    if (exception.getCause() instanceof ConstraintViolationException) {
      message = "error.entityExists";
      status = Response.Status.CONFLICT;
    } else if (exception instanceof OptimisticLockException) {
      message = "error.entityChanged";
      status = Response.Status.CONFLICT;
    } else {
      log.log(Level.SEVERE, "can't analyze", exception);
    }

    responseObj.put("general", localizer.localize(message));

    return Response.status(status).entity(responseObj).build();
  }
}
