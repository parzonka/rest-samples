package de.ahus1.model.sighting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonView;
import org.hibernate.annotations.Type;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import de.ahus1.model.general.AbstractEntity;
import de.ahus1.model.general.HasId;

/**
 * This olds information about sightings. Sample vessles are "Space Shuttle", or
 * "Klingon Warbird".
 * 
 * @author Alexander Schwartz 2012
 * 
 */
@XmlRootElement
@Entity
public class Sighting extends AbstractEntity implements HasId {

  private static final long serialVersionUID = 1L;

  /**
   * Show all elements of this class.
   */
  public interface Extended extends AbstractEntity.Extended {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long sightingId;

  // START SNIPPET: hibernatevalidator2
  @NotNull
  @Size(min = 2, message = "{sighting.memo.minLength}")
  private String sightingMemo;
  // END SNIPPET: hibernatevalidator2

  @ManyToOne
  @JoinColumn(name = "vesselId")
  private Vessel vessel;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTimeZoneAsString")
  private DateTimeZone sightingTimezone;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
  private LocalDateTime sightingDate;

  public Long getSightingId() {
    return sightingId;
  }

  public void setSightingId(Long sightingId) {
    this.sightingId = sightingId;
  }

  @JsonView(Extended.class)
  public String getSightingMemo() {
    return sightingMemo;
  }

  public void setSightingMemo(String sightingMemo) {
    this.sightingMemo = sightingMemo;
  }

  public Vessel getVessel() {
    return vessel;
  }

  public void setVessel(Vessel vessel) {
    this.vessel = vessel;
  }

  @Override
  public Object getId() {
    return getSightingId();
  }

  public DateTimeZone getSightingTimezone() {
    return sightingTimezone;
  }

  public void setSightingTimezone(DateTimeZone sightingTimezone) {
    this.sightingTimezone = sightingTimezone;
  }

  public LocalDateTime getSightingDate() {
    return sightingDate;
  }

  public void setSightingDate(LocalDateTime sightingDate) {
    this.sightingDate = sightingDate;
  }

}