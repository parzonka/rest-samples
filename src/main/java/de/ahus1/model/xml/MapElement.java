package de.ahus1.model.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Map element for returning hashmaps as XML.
 * 
 * @author Alexander Schwartz 2012
 * 
 */
@XmlRootElement
public class MapElement {
  @XmlElement
  private String key;
  @XmlElement
  private Object value;

  /**
   * Empty constructor.
   */
  public MapElement() {
  } // Required by JAXB

  /**
   * Parameterized constructor.
   * 
   * @param key
   *          key
   * @param value
   *          value
   */
  public MapElement(String key, Object value) {
    this.key = key;
    this.value = value;
  }
}
