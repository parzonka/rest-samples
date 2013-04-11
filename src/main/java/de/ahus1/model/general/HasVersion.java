package de.ahus1.model.general;

/**
 * This is an interface to be used by all entities that have a version. The
 * version is used for tracking modifications on an entity (used for optimistic
 * locking). This is a Integer and not an int to enable us to leave it empty
 * when de-serializing an element.
 * 
 * @author Alexander Schwartz 2012
 * 
 */
public interface HasVersion {

  /**
   * Get the version of the entity.
   * 
   * @return numeric version identifier
   */
  Integer getVersion();

  /**
   * Set the version of the entity.
   * 
   * @param version
   *          numeric version identifier
   */
  void setVersion(Integer version);
}
