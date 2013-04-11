package de.ahus1.rest.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Handling of constraint violation exceptions that can be thrown by bean
 * validation.
 * 
 * @author Alexander Schwartz (2012)
 * 
 */
@Provider
public class ConstraintViolationExceptionMapper implements
    ExceptionMapper<ConstraintViolationException> {

  /**
   * Convert the internal exception to something RESTful.
   * 
   * @param exception
   *          exception to be handled
   * @return a response of type 400 / bad request.
   */
  @SuppressWarnings("unchecked")
  @Override
  public Response toResponse(ConstraintViolationException exception) {
    Map<String, Object> responseObj = new HashMap<String, Object>();

    for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
      Object element = responseObj;
      Object parent = null;
      String parentName = null;

      for (Node n : violation.getPropertyPath()) {
        Object newParent = element;

        String name = n.getName();
        if (n.getIndex() != null) {
          ArrayList<Map<String, Object>> array;
          if (element instanceof ArrayList) {
            array = (ArrayList<Map<String, Object>>) element;
          } else {
            array = new ArrayList<Map<String, Object>>();
            ((Map<String, Object>) parent).put(parentName, array);
          }
          while (array.size() < n.getIndex() + 1) {
            array.add(null);
          }
          if (array.get(n.getIndex()) == null) {
            Map<String, Object> newElement = new HashMap<String, Object>();
            array.set(n.getIndex(), newElement);
            element = newElement;
          } else {
            element = array.get(n.getIndex());
          }
        }

        if (element instanceof Map) {
          Map<String, Object> map = (Map<String, Object>) element;
          if (map.get(name) == null) {
            Map<String, Object> newElement = new HashMap<String, Object>();
            map.put(name, newElement);
            element = newElement;
          } else {
            element = ((Map<String, Object>) element).get(name);
          }
        }

        parent = newParent;
        parentName = name;
      }
      ((Map<String, Object>) element).put("message", violation.getMessage());
    }

    return Response.status(Response.Status.BAD_REQUEST).entity(responseObj)
        .build();
  }
}
