package de.ahus1.model.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTimeZone;

/**
 * Simple BigDecimal serializer. To ensure that this is never handled in the
 * frontend as a double value.
 * 
 * @author Alexander Schwartz
 * 
 */
public class DateTimeZoneDeserializer extends JsonDeserializer<DateTimeZone> {

  /**
   * Deserialize amount as a string.
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
  public DateTimeZone deserialize(JsonParser jparse,
      DeserializationContext context) throws IOException {
    String text = jparse.getText();
    if (text == null || text.trim().length() == 0) {
      return null;
    } else {
      return DateTimeZone.forID(text);
    }
  }
}
