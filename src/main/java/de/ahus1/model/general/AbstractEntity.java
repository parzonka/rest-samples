package de.ahus1.model.general;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.codehaus.jackson.map.annotate.JsonView;

/**
 * Base Entity with a Version.
 * 
 * @author Alexander Schwartz
 * 
 */
@MappedSuperclass
public class AbstractEntity implements Serializable, HasVersion {

  /**
   * This is the base interface with the version included.
   * 
   * @author Alexander Schwartz
   * 
   */
  public interface Extended extends IdView {
  }

  private static final long serialVersionUID = 1L;

  @Version
  private Integer version;

  @JsonView(Extended.class)
  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

}
