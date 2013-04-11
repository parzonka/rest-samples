package de.ahus1.model.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * Simple BigDecimal serializer. To ensure that this is never handled in the
 * frontend as a double value.
 * 
 * @author Alexander Schwartz
 * 
 */
public class BigDecimalAmountSerializer extends JsonSerializer<BigDecimal> {

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
  public void serialize(BigDecimal value, JsonGenerator jgen,
      SerializerProvider provider) throws IOException {
    NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
    nf.setGroupingUsed(false);
    nf.setMinimumFractionDigits(0);
    jgen.writeString(nf.format(value));
  }

}
