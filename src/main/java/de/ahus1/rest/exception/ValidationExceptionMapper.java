package de.ahus1.rest.exception;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Handling of exceptions occuring when validating the payload with tbe bean
 * validation framework.
 * 
 * @author Alexander Schwartz (2012)
 * 
 */
@Provider
public class ValidationExceptionMapper implements
    ExceptionMapper<ValidationException> {

  @Override
  public Response toResponse(ValidationException exception) {
    Map<String, String> responseObj = new HashMap<String, String>();

    responseObj.put("message", exception.getMessage());

    return Response.status(Response.Status.BAD_REQUEST).entity(responseObj)
        .build();
  }
}
