package de.ahus1.model.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * Simple BigDecimal serializer. To ensure that this is never handled in the
 * frontend as a double value.
 * 
 * @author Alexander Schwartz
 * 
 */
public class BigDecimalAmountDeserializer extends JsonDeserializer<BigDecimal> {

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
  public BigDecimal deserialize(JsonParser jparse,
      DeserializationContext context) throws IOException {
    String text = jparse.getText();
    if (text == null || text.trim().length() == 0) {
      return null;
    } else {
      DecimalFormat nf = (DecimalFormat) (DecimalFormat
          .getInstance(Locale.ENGLISH));
      nf.setParseBigDecimal(true);
      try {
        return (BigDecimal) nf.parse(jparse.getText().trim());
      } catch (ParseException e) {
        throw new RuntimeException("can't parse");
      }
    }
  }
}
