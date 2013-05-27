package de.ahus1.rest.general;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jackson.map.annotate.JsonView;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

import de.ahus1.model.general.HasId;
import de.ahus1.model.general.ListView;

/**
 * JAX-RS Example This class produces a RESTful service to read the contents of
 * the table. The child classes of this class should be annotated with
 * {@code @Stateless} to make CDI work with the base and the main class. At
 * least RestEASY implements an automatic handler for OPTIONS, so there is no
 * need to implement it here.
 * 
 * @param <ENTITY>
 *          class of the entity that is managed here
 * 
 * @author Alexander Schwartz 2012
 */
@Produces({ "application/json", "text/xml" })
@Consumes({ "application/json", "text/xml" })
public abstract class DefaultRestEndpoint<ENTITY extends HasId> {

  @Inject
  private HttpServletRequest httpServletRequest;

  @Inject
  private EntityManager em;

  @Inject
  private Validator validator;

  @Inject
  private Logger log;

  @Context
  private UriInfo uriInfo;

  private Class<ENTITY> entityClass;

  private Class<?> keyClass;

  /**
   * Get information about parameterized types.
   */
  @SuppressWarnings("unchecked")
  public DefaultRestEndpoint() {
    Class<?> c = getClass();
    while (c.getSuperclass() != DefaultRestEndpoint.class) {
      c = c.getSuperclass();
    }
    ParameterizedType myself = (ParameterizedType) c.getGenericSuperclass();
    this.entityClass = (Class<ENTITY>) myself.getActualTypeArguments()[0];
  }

  /**
   * Create a new primary key instance form the serialized primary key given as
   * parameter.
   * 
   * @param id
   *          serialized unique identifier
   * @return initialized primary key object
   */
  protected Object constructPK(String id) {
    try {
      if (keyClass == null) {
        for (SingularAttribute<? super ENTITY, ?> a : em.getMetamodel()
            .managedType(entityClass).getSingularAttributes()) {
          if (a.isId()) {
            keyClass = a.getJavaType();
          }
        }
      }
      return keyClass.getConstructor(String.class).newInstance(id);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    } catch (SecurityException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns the java class of the entity to work around type erasure of java
   * generics.
   * 
   * @return entity class
   */
  protected Class<ENTITY> getEntityClass() {
    return entityClass;
  }

  /**
   * Returns the extended view class to be used when showing entities.
   * 
   * @return class to be used for entities. Use null if there is no JSONView
   *         restriction in place.
   */
  protected Class<?> getExtendedView() {
    return null;
  }

  /**
   * Execute a query by example with the entity given as a parameter.
   * 
   * @param entity
   *          example entity
   * @return a list of entities matching the query
   */
  @SuppressWarnings("unchecked")
  @POST
  @Path("/qbe")
  public List<ENTITY> queryByExample(ENTITY entity) {
    // START SNIPPET: hibernatebasic3
    Session session = (Session) em.getDelegate();
    Example example = Example.create(entity).enableLike(MatchMode.ANYWHERE)
        .ignoreCase();
    Criteria criteria = session.createCriteria(entity.getClass()).add(example);
    // END SNIPPET: hibernatebasic3
    addSubCriteria(criteria, entity);

    if (criteria.list().isEmpty()) {
      return null;
    } else {
      pullByJsonView(criteria.list(), getExtendedView());
      return criteria.list();
    }
  }

  /**
   * Override this class to add more subcriteria to your query.
   * 
   * @param criteria
   *          where to add the new criteria
   * @param entity
   *          the sample provided by the user
   */
  protected void addSubCriteria(Criteria criteria, ENTITY entity) {
  }

  /**
   * Delete a single entity.
   * 
   * @param id
   *          unique identifier for the entity
   */
  @DELETE
  @Path("/{id:.+}")
  public void deleteById(@PathParam("id") String id) {
    ENTITY result = em.find(getEntityClass(), constructPK(id));
    if (result == null) {
      throw new EntityNotFoundException();
    }
    em.remove(result);
    em.flush();
  }

  /**
   * Retrieve a single entity from the database.
   * 
   * @param id
   *          primary key
   * @param request
   *          context for this request
   * @return the Entity, 302 if not modified
   */
  @GET
  @Path("/{id:.+}")
  public Response findById(@PathParam("id") String id, @Context Request request) {
    ENTITY result = em.find(getEntityClass(), constructPK(id));

    if (result == null) {
      throw new EntityNotFoundException();
    }

    pullByJsonView(result, getExtendedView());

    return Response.ok(result).build();
  }

  /**
   * Create an empty entity that can later be used to add a new entity.
   * 
   * @return the Entity
   */
  @POST
  @Path("/new")
  public ENTITY newEntity() {
    ENTITY result = createNewEntity();
    return result;
  }

  /**
   * This can be overriden in derived classes to do something more
   * sophisticated. For example: set default values, created nested elements.
   * 
   * @return the Entity
   */
  protected ENTITY createNewEntity() {
    try {
      return getEntityClass().newInstance();
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException("can't create new instance", e);
    }
  }

  /**
   * Map a query parameter to the path of the criteria query. Childs only need
   * to override this for non-simple attributes.
   * 
   * @param root
   *          root object
   * @param key
   *          the element to be compared
   * @return a part of criteria query
   */
  protected javax.persistence.criteria.Path<Object> mapQueryParameter(
      Root<ENTITY> root, String key) {
    return root.get(key);
  }

  /**
   * Bind read-only entities connected to this object. We'll traverse the object
   * and find all {@link ManyToOne}, {@link OneToOne} and {@link OneToMany}
   * annotations. TODO: All entities today need to implement the {@link HasId}
   * interface. In the future this may become obsolete when using JPA's
   * Metamodel.
   * 
   * @param object
   *          object to be traversed
   */
  protected void bindReadOnlyEntities(Object object) {
    if (object instanceof Collection<?>) {
      Collection<?> c = (Collection<?>) object;
      for (Object o : c) {
        bindReadOnlyEntities(o);
      }
    } else {
      for (Field f : object.getClass().getDeclaredFields()) {
        ManyToOne mto = f.getAnnotation(ManyToOne.class);
        OneToOne oto = f.getAnnotation(OneToOne.class);
        OneToMany otm = f.getAnnotation(OneToMany.class);
        CascadeType[] ct = null;
        if (mto != null) {
          ct = mto.cascade();
        }
        if (oto != null) {
          ct = oto.cascade();
        }
        if (otm != null) {
          ct = otm.cascade();
        }
        if (mto == null || ct == null) {
          continue;
        }
        String getMethodName = "get"
            + f.getName().substring(0, 1).toUpperCase(Locale.ENGLISH)
            + f.getName().substring(1);
        try {
          Method get = object.getClass().getMethod(getMethodName);
          Object o = get.invoke(object);
          if (o != null) {
            if (ct != null && ct.length > 0) {
              // assume that any cascade is worthwile to traverse
              bindReadOnlyEntities(o);
            } else {
              // fresh it from database
              HasId id = (HasId) o;
              Object r = em.find(o.getClass(), id.getId());
              String setMethodName = "set"
                  + f.getName().substring(0, 1).toUpperCase(Locale.ENGLISH)
                  + f.getName().substring(1);
              Method set = object.getClass().getMethod(setMethodName,
                  o.getClass());
              set.invoke(object, r);
            }
          }
        } catch (ReflectiveOperationException e) {
          throw new RuntimeException("can't access method " + getMethodName, e);
        }
      }
    }
  }

  /**
   * Traverse object to pull lazy hibernate entities. There has been the idea of
   * an extended entity manager that also exists outside of the transaction
   * scope (see Java Magazin 06/2013). This sounded quite tempting, failed to
   * work to join the transaction scope correctly.
   * 
   * @param object
   *          object to be traversed
   * @param view
   *          the json view this should be restricted to
   */
  protected void pullByJsonView(Object object, Class<?> view) {
    if (object == null) {
      return;
    } else if (object instanceof Collection) {
      Collection<?> c = (Collection<?>) object;
      Iterator<?> i = c.iterator();
      while (i.hasNext()) {
        pullByJsonView(i.next(), view);
      }
    } else if (object instanceof Map<?, ?>) {
      Map<?, ?> m = (Map<?, ?>) object;
      Iterator<?> i = m.entrySet().iterator();
      while (i.hasNext()) {
        Entry<?, ?> e = (Entry<?, ?>) i.next();
        pullByJsonView(e.getKey(), view);
        pullByJsonView(e.getValue(), view);
      }
    } else if (object.getClass().isArray()) {
      Object[] a = (Object[]) object;
      for (int i = 0; i < a.length; ++i) {
        pullByJsonView(a[i], view);
      }
    } else if (object != null) {
      try {
        ManagedType<?> type = em.getMetamodel().managedType(object.getClass());
        for (Attribute<?, ?> a : type.getAttributes()) {
          String getMethodName = "get"
              + a.getName().substring(0, 1).toUpperCase(Locale.ENGLISH)
              + a.getName().substring(1);
          Method m;
          try {
            m = object.getClass().getMethod(getMethodName);
          } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
          }
          JsonView jsonView = m.getAnnotation(JsonView.class);
          if (jsonView != null && view != null) {
            boolean found = false;
            for (Class<?> v : jsonView.value()) {
              if (v.isAssignableFrom(view)) {
                found = true;
                break;
              }
            }
            if (!found) {
              continue;
            }
          }
          try {
            Object o = m.invoke(object, new Object[] {});
            if (o != null) {
              pullByJsonView(o, view);
            }
          } catch (ReflectiveOperationException e) {
            throw new RuntimeException("can't traverse object", e);
          }
        }

      } catch (IllegalArgumentException e) {
        // this is not a managed type.
        return;
      }
    }

  }

  /**
   * Return all known instances of the entity.
   * 
   * @return a list of entities
   */
  @GET
  @JsonView(ListView.class)
  public Response listAll() {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<ENTITY> q = cb.createQuery(getEntityClass());
    Root<ENTITY> root = q.from(getEntityClass());
    /*
     * FIXME: context is only filled once and not on every request. This is a
     * regression. JUnit test needed.
     * https://issues.jboss.org/browse/RESTEASY-573
     */
    /*
     * workaround: get hold of a http servlet context and read the parameters
     * using Seam Solder.
     */
    /*
     * MultivaluedMap<String, String> qp = uriInfo.getQueryParameters();
     */
    Predicate con = null;
    Enumeration<String> keys = httpServletRequest.getParameterNames();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      String value = httpServletRequest.getParameter(key);
      log.info("query key:" + key + " value:" + value);
      Predicate p = cb.equal(mapQueryParameter(root, key), value);
      if (con != null) {
        con = cb.and(con, p);
      } else {
        con = p;
      }
    }

    if (con != null) {
      q.where(con);
    }
    final List<ENTITY> results = em.createQuery(q).getResultList();

    pullByJsonView(results, ListView.class);
    /**
     * The serialization to XML will not work with an empty array. Also
     * returning null here will deliver a 204 (no content) message to the
     * client.
     */
    if (results.isEmpty()) {
      Response response = Response.noContent().build();
      return response;
    } else {
      GenericEntity<Collection<ENTITY>> entity = new GenericEntity<Collection<ENTITY>>(
          results, new ParameterizedTypeImpl(new Class[] { getEntityClass() },
              null, List.class)) {
      };
      Response response = Response.ok(entity).build();
      return response;
    }
  }

  /**
   * Save a given entity.
   * 
   * @param id
   *          unqiue identifer of the entity
   * @param entity
   *          payload to be saved; IDs must match!
   * @param request
   *          context of the request
   * @return the entity that has been saved
   */
  @PUT
  @Path("/{id:.+}")
  public Response update(@PathParam("id") String id, ENTITY entity,
      @Context Request request) {
    matchIdAndEntity(constructPK(id), entity);

    ENTITY oldEntry = em.find(getEntityClass(), constructPK(id));

    if (oldEntry == null) {
      throw new EntityNotFoundException();
    }

    bindReadOnlyEntities(entity);
    entity = em.merge(entity);

    em.flush();

    return Response.noContent().build();
  }

  /**
   * Save a given entity.
   * 
   * @param entity
   *          payload to be saved; IDs must match!
   * @return the entity that has been saved
   */
  @POST
  @Path("")
  public Response add(ENTITY entity) {
    bindReadOnlyEntities(entity);

    em.persist(entity);
    em.flush();

    UriBuilder locationBuilder = uriInfo.getBaseUriBuilder();
    locationBuilder.path(this.getClass());
    URI childLocation = locationBuilder.path("{id}").build(entity.getId());

    return Response.status(Response.Status.CREATED).location(childLocation)
        .build();
  }

  /**
   * When an entity is saved the ID is passed as a URL parameter. The unique
   * identifier is also given in the payload of the object. There is a risk that
   * the ID given in the payload doesn't match the ID in the URL. In this case
   * there should be an exception.
   * 
   * @param key
   *          primary key given as url parameter
   * @param entity
   *          entity as received in the payload
   */
  private void matchIdAndEntity(Object key, ENTITY entity) {
    if (entity.getId() == null) {
      throw new IllegalArgumentException();
    } else if (!entity.getId().equals(key)) {
      throw new IllegalArgumentException();
    }
  }

}
