package de.ahus1.model.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTimeZone;

/**
 * Simple BigDecimal serializer. To ensure that this is never handled in the
 * frontend as a double value.
 * 
 * @author Alexander Schwartz
 * 
 */
public class DateTimeZoneSerializer extends JsonSerializer<DateTimeZone> {

  /**
   * Serialize amount as a string.
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
  public void serialize(DateTimeZone value, JsonGenerator jgen,
      SerializerProvider provider) throws IOException {
    jgen.writeString(value.getID());
  }

}
