package de.ahus1.model.sighting;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonView;

import de.ahus1.model.general.AbstractEntity;
import de.ahus1.model.general.IdView;
import de.ahus1.model.general.Translation;

/**
 * This olds information about vessels. Sample vessles are "Space Shuttle", or
 * "Klingon Warbird".
 * 
 * @author Alexander Schwartz 2012
 * 
 */
@XmlRootElement
@Entity
public class Vessel extends AbstractEntity {

  private static final long serialVersionUID = 1L;

  /**
   * Show all elements of this class.
   */
  public interface Extended extends AbstractEntity.Extended {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long vesselId;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "vesselName")
  private Translation vesselName;

  public Long getVesselId() {
    return vesselId;
  }

  public void setVesselId(Long vesselId) {
    this.vesselId = vesselId;
  }

  @JsonView(IdView.class)
  public Translation getVesselName() {
    return vesselName;
  }

  public void setVesselName(Translation vesselName) {
    this.vesselName = vesselName;
  }

}