package de.ahus1.model.general;

import java.util.Locale;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the translations.
 * 
 * @author Alexander Schwartz
 * 
 */
@XmlRootElement
@Entity
public class Translation extends AbstractEntity {

  private static final long serialVersionUID = 1L;

  /**
   * Show all elements of this class.
   */
  public interface Extended extends AbstractEntity.Extended {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long translationId;

  @ElementCollection
  @CollectionTable(name = "Text", joinColumns = @JoinColumn(name = "translationId"))
  @MapKeyColumn(name = "textLanguage")
  @Column(name = "textString")
  private Map<Locale, String> texts;

  public Long getTranslationId() {
    return translationId;
  }

  public void setTranslationId(Long utId) {
    this.translationId = utId;
  }

  public Map<Locale, String> getTexts() {
    return texts;
  }

  public void setTexts(Map<Locale, String> texts) {
    this.texts = texts;
  }

}