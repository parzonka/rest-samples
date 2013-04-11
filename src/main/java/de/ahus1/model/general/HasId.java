package de.ahus1.model.general;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * This is an interface to be used by all entities that have a primary key. This
 * can later be used to retrieve it.
 * 
 * @author Alexander Schwartz 2012
 * 
 */
public interface HasId {

  /**
   * Get the primary key of the entity.
   * 
   * @return numeric version identifier
   */
  @JsonIgnore
  Object getId();
}
