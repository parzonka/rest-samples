package de.ahus1.model.json;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.TypeReference;

import de.ahus1.model.general.Translation;

/**
 * Simple Translation serializer. Reduce shown translations to the ones needed.
 * 
 * @author Alexander Schwartz
 * 
 */
public class TranslationDeserializer extends JsonDeserializer<Translation> {

  /**
   * Deserialize translation from a string.
   * 
   * @param jparse
   *          parser instance
   * @param context
   *          context for deserialization
   * @return BigDecimal with the correct value
   * @throws IOException
   *           on problems
   */
  @Override
  public Translation deserialize(JsonParser jparse,
      DeserializationContext context) throws IOException {
    JsonToken token = jparse.getCurrentToken();
    Translation t = new Translation();
    if (token == JsonToken.START_OBJECT) {
      Map<Locale, String> o = jparse
          .readValueAs(new TypeReference<Map<Locale, String>>() {
          });
      t.setTexts(o);
    }
    // otherwise it will be a VALUE_STRING -- this will be ignored completely

    return t;
  }
}
