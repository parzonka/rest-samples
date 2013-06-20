package de.ahus1.model.sighting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

import de.ahus1.model.general.AbstractEntity;
import de.ahus1.model.general.HasId;

/**
 * This holds information about available time zones.
 * 
 * @author Alexander Schwartz
 * 
 */
@XmlRootElement
@Entity
public class Timezone extends AbstractEntity implements HasId {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long timezoneId;

  @NotEmpty
  private String timezoneName;

  @Override
  public Object getId() {
    return getTimezoneId();
  }

  public Long getTimezoneId() {
    return timezoneId;
  }

  public void setTimezoneId(Long timezoneId) {
    this.timezoneId = timezoneId;
  }

  public String getTimezoneName() {
    return timezoneName;
  }

  public void setTimezoneName(String timezoneName) {
    this.timezoneName = timezoneName;
  }

}