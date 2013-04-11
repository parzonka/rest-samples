package de.ahus1.rest.sighting;

import java.util.HashMap;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

import de.ahus1.model.general.Translation;
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
@Path("/vessel")
public class VesselEndpoint extends DefaultRestEndpoint<Vessel> {

  @Override
  protected Class<?> getExtendedView() {
    return Vessel.Extended.class;
  }

  @Override
  protected Vessel createNewEntity() {
    Vessel v = super.createNewEntity();
    Translation t = new Translation();
    v.setVesselName(t);
    HashMap<Locale, String> m = new HashMap<Locale, String>();
    t.setTexts(m);
    m.put(Locale.ENGLISH, null);
    return v;
  }

}