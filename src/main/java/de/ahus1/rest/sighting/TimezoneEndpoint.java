package de.ahus1.rest.sighting;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

import de.ahus1.model.sighting.Timezone;
import de.ahus1.model.sighting.Vessel;
import de.ahus1.rest.general.DefaultRestEndpoint;

/**
 * JAX-RS Example This class produces a RESTful service to read the contents of
 * the table.
 * 
 * @author Alexander Schwartz 2012
 * 
 */
@Stateless
@Path("/timezone")
public class TimezoneEndpoint extends DefaultRestEndpoint<Timezone> {

  @Override
  protected Class<?> getExtendedView() {
    return Vessel.Extended.class;
  }

}