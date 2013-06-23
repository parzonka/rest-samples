package de.ahus1.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Watches out for i18next cookie to identify proper Locale.
 * 
 * @author Alexander Schwartz 2013
 * 
 */
public class LocaleCookieWrapper extends HttpServletRequestWrapper {

  /**
   * Wrap the given request.
   * 
   * @param request
   *          HttpServletRequest to be wrapped
   */
  public LocaleCookieWrapper(HttpServletRequest request) {
    super(request);
  }

  @Override
  public Locale getLocale() {
    // the i18next cookie has precendence
    for (Cookie c : getCookies()) {
      if ("i18next".equals(c.getName())) {
        return new Locale(c.getValue());
      }
    }
    return super.getLocale();
  }

  @Override
  public Enumeration<Locale> getLocales() {
    // the i18next cookie has precendence
    List<Locale> locales = new ArrayList<Locale>();
    for (Cookie c : getCookies()) {
      if ("i18next".equals(c.getName())) {
        locales.add(new Locale(c.getValue()));
      }
    }
    locales.addAll(Collections.list(super.getLocales()));
    return Collections.enumeration(locales);
  }
}
