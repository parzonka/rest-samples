package de.ahus1.model.json;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.module.SimpleModule;
import org.joda.time.DateTimeZone;

import de.ahus1.model.general.Translation;

/**
 * Custom object mapper. Main use: serialize BigDecimal as a string (to avoid
 * float/double handle on receiver side)
 * 
 * @author Alexander Schwartz
 * 
 */
@Provider
public class CustomObjectMapper implements ContextResolver<ObjectMapper> {
  @Inject
  private HttpServletRequest httpServletRequest;

  @Override
  public ObjectMapper getContext(Class<?> type) {
    final ObjectMapper result = new ObjectMapper();
    Enumeration<Locale> languages = httpServletRequest.getLocales();

    SimpleModule module = new SimpleModule(getClass().getName(), new Version(1,
        0, 0, null))
        .addDeserializer(BigDecimal.class, new BigDecimalAmountDeserializer())
        .addSerializer(BigDecimal.class, new BigDecimalAmountSerializer())
        .addDeserializer(DateTimeZone.class, new DateTimeZoneDeserializer())
        .addSerializer(DateTimeZone.class, new DateTimeZoneSerializer())
        .addDeserializer(Translation.class, new TranslationDeserializer())
        .addSerializer(Translation.class, new TranslationSerializer(languages));
    result.registerModule(module);
    result.configure(Feature.WRITE_DATES_AS_TIMESTAMPS, false);
    return result;
  }
}
