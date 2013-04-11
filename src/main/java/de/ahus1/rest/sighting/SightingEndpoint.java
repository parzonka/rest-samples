package de.ahus1.rest.sighting;

import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.annotate.JsonView;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import de.ahus1.model.sighting.Sighting;
import de.ahus1.rest.general.DefaultRestEndpoint;

/**
 * JAX-RS Example This class produces a RESTful service to read the contents of
 * the table.
 * 
 * @author Alexander Schwartz 2012
 * 
 */
@Stateless
@Path("/sighting")
public class SightingEndpoint extends DefaultRestEndpoint<Sighting> {

  @Override
  protected Class<?> getExtendedView() {
    return Sighting.Extended.class;
  }

  @Override
  @GET
  @Path("/{id:.+}")
  @JsonView(Sighting.Extended.class)
  public Response findById(@PathParam("id") String id, @Context Request request) {
    return super.findById(id, request);
  }

  @Override
  @POST
  @Path("/qbe")
  @JsonView(Sighting.Extended.class)
  public List<Sighting> queryByExample(Sighting entity) {
    return super.queryByExample(entity);
  }

  @Override
  protected void addSubCriteria(Criteria criteria, Sighting entity) {
    super.addSubCriteria(criteria, entity);
    if (entity.getVessel() != null) {
      if (entity.getVessel().getVesselId() != null) {
        criteria.createCriteria("vessel").add(
            Restrictions.eq("vesselId", entity.getVessel().getVesselId()));
      } else {
        criteria.createCriteria("vessel").add(
            Example.create(entity.getVessel()));
      }
    }
  }
}