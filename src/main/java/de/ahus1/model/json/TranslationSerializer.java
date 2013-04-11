package de.ahus1.model.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import de.ahus1.model.general.Translation;

/**
 * Simple Translation serializer. Reduce shown translations to the ones needed.
 * 
 * @author Alexander Schwartz
 * 
 */
public class TranslationSerializer extends JsonSerializer<Translation> {

  private List<Locale> languages;

  /**
   * Use the languages provided to restrict shown translations.
   * 
   * @param languages
   *          languages to use
   */
  public TranslationSerializer(Enumeration<Locale> languages) {
    this.languages = new ArrayList<Locale>();
    while (languages.hasMoreElements()) {
      this.languages.add(languages.nextElement());
    }
  }

  /**
   * Serialize translation as a string.
   * 
   * @param value
   *          value to be serialized
   * @param jgen
   *          json generator
   * @param provider
   *          serialization provider
   * @throws IOException
   *           on problems
   */
  @Override
  public void serialize(Translation value, JsonGenerator jgen,
      SerializerProvider provider) throws IOException {
    if (provider.getSerializationView() != null
        && !Translation.Extended.class.isAssignableFrom(provider
            .getSerializationView())) {
      String t = null;
      for (Locale l : languages) {
        t = value.getTexts().get(l);
        if (t != null) {
          break;
        }
      }
      if (t == null) {
        for (Locale l : languages) {
          t = value.getTexts().get(new Locale(l.getCountry()));
          if (t != null) {
            break;
          }
        }
      }
      if (t == null) {
        t = value.getTexts().get(Locale.ENGLISH);
      }
      if (t == null && value.getTexts().size() > 0) {
        t = value.getTexts().entrySet().iterator().next().getValue();
      }
      jgen.writeString(t);
    } else {
      jgen.writeObject(value.getTexts());
    }
  }
}
