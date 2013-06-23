package de.ahus1.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Translate a given key to a translation (depending on current HTTP request's
 * locale).
 * 
 * @author Alexander Schwartz
 * 
 */
public class Localizer {

  @Inject
  private HttpServletRequest httpServletRequest;

  /**
   * Translate a key to a translated value.
   * 
   * @param key
   *          the to be translated
   * @return message
   */
  public String localize(String key) {
    try {
      ResourceBundle rb = ResourceBundle.getBundle("messages",
          new LocaleCookieWrapper(httpServletRequest).getLocale());
      return rb.getString(key);
    } catch (MissingResourceException e) {
      return "{" + key + "}";
    }
  }
}
